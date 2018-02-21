package fr.evolya.domokit.ctrl;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.ctrl.ModuleSecurity.OnSecurityAlertCleared;
import fr.evolya.domokit.ctrl.ModuleSecurity.OnSecurityAlertRaised;
import fr.evolya.domokit.ctrl.ModuleSecurity.OnSecurityLevelChanged;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.iface.IFeature;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.Room;
import fr.evolya.domokit.gui.panels.PanelLogs;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.AsynchOperation;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;
import fr.evolya.javatoolkit.events.fi.EventProvider;
import fr.evolya.javatoolkit.iot.arduino.ArduinoEvents;
import fr.evolya.javatoolkit.time.Timer;

@EventProvider({
	// When the level of security changed
	OnSecurityLevelChanged.class,
	// When a security alert occures
	OnSecurityAlertRaised.class,
	// When a security alert is cleared
	OnSecurityAlertCleared.class
})

public class ModuleSecurity {
	
	@FunctionalInterface
	public static interface OnSecurityLevelChanged {
		public void onSecurityLevelChanged(int level, String label);
	}
	
	@FunctionalInterface
	public static interface OnSecurityAlertRaised {
		public void onSecurityAlertRaised(String label);
	}
	
	@FunctionalInterface
	public static interface OnSecurityAlertCleared {
		public void onSecurityAlertCleared(String label);
	}

	public static final Logger LOGGER = Logs.getLogger("Security");
	
	@Inject
	public App app;
	
	@Inject
	public View480x320 view;
	
	@Inject
	public AppConfiguration conf;
	
	private boolean locking = false;

	private int securityLevel;
	private String securityLabel;
	private String alertLabel;
	private String warningMsg;

	private int securityThreats = 0;
	
	private Color alertHighlightColor = new Color(255, 100, 100);
	
	public ModuleSecurity() {
	}
	
	public void setSecurityLevel(int level, String label) {
		if (securityLevel == level && label.equals(securityLabel)) return;
		securityLevel = level;
		securityLabel = label;
		view.panelStatus.setCartoucheLevel(level);
		view.panelStatus.setTitle(label);
		app.notify(OnSecurityLevelChanged.class, level, label);
		updateStatusPanel();
		if (level == 0) {
			securityThreats  = 0;
			clearWarning();
			clearAlert("Intrusion !");
			clearAlert("!!! Alarm !!!");
			Timer.stop("delayToEnterPassword");
		}
	}
	
	public int getSecurityLevel() {
		return securityLevel;
	}
	
	public String getSecurityLevelLabel() {
		return securityLabel;
	}
	
	private List<String> alerts = new ArrayList<>();
	
	public void alert(String label) {
		
		synchronized (alerts) {
			// Already thrown
			if (label.equals(alertLabel)) return;
			// Cleanup
			if (alerts.contains(label)) {
				alerts.remove(label);
			}
			// Replace existing
			if (alertLabel != null) {
				alerts.add(alertLabel);
			}
			alertLabel = label;
		}
		// Log
		LOGGER.log(Logs.INFO, "ALERT THROWN: " + label);
		// Event
		app.notify(OnSecurityAlertRaised.class, label);
		// Run update in EDT
		updateStatusPanel();
		// Update job
		Timer.startRepeat("alert", 1000, (i) -> {
			EventQueue.invokeLater(() -> {
				boolean peer = i % 2 == 0;
				rotateAlert();
				view.panelStatus.setBorderColor(peer ? Color.RED : alertHighlightColor);
			});
		});
	}
	
	public void rotateAlert() {
		if (alerts.isEmpty()) return;
		synchronized (alerts) {
			if (alertLabel != null) alerts.add(alertLabel);
			alertLabel = alerts.remove(0);
		}
		updateStatusPanel();
	}
	
	public void clearAlert(String label) {
		
		synchronized (alerts) {
			// Current alert
			if (label.equals(alertLabel)) {
				alertLabel = null;
				if (!alerts.isEmpty()) {
					alertLabel = alerts.remove(0);
				}
			}
			// Stagged alert
			else if (alerts.contains(label)) {
				alerts.remove(label);
			}
			// Not found
			else {
				return;
			}
		}
		// Log
		LOGGER.log(Logs.INFO, "ALERT CLEARED: " + label);
		if (alertLabel != null) {
			LOGGER.log(Logs.INFO, "ALERT RESTORED: " + alertLabel);
		}
		// Event
		app.notify(OnSecurityAlertCleared.class, label);
		// Timer
		if (alertLabel == null) {
			Timer.stop("alert");
		}
		// Run update in EDT
		updateStatusPanel();
	}
	
	public void clearAlerts() {

		// Events
		if (alertLabel != null) {
			app.notify(OnSecurityAlertCleared.class, alertLabel);
			LOGGER.log(Logs.INFO, "ALERT CLEARED: " + alertLabel);
		}
		alerts.stream().forEach((alert) -> {
			LOGGER.log(Logs.INFO, "ALERT CLEARED: " + alert);
			app.notify(OnSecurityAlertCleared.class, alert);
		});
		// Clear
		synchronized (alerts) {
			alertLabel = null;
			alerts.clear();
		}
		// Timer
		Timer.stop("alert");
		// Run update in EDT
		updateStatusPanel();
	}
	
	public String getAlert() {
		synchronized (alerts) {
			return alertLabel;
		}
	}
	
	public boolean isAlertActive(String label) {
		synchronized (alerts) {
			if (label.equals(alertLabel)) return true;
			return alerts.contains(label);
		}
	}
	
	public void showWarning(String message) {
		warningMsg = message;
		view.panelStatus.setMessage(message);
		updateStatusPanel();
	}
	
	public void clearWarning() {
		warningMsg = null;
		updateStatusPanel();
		EventQueue.invokeLater(() -> view.panelStatus.setMessage("-"));
	}
	
	public String getWarning() {
		return warningMsg;
	}
	
	public void updateStatusPanel() {
		if (!EventQueue.isDispatchThread()) {
			EventQueue.invokeLater(() -> updateStatusPanel());
			return;
		}
		if (alertLabel != null) {
			view.panelStatus.setBorderColor(Color.RED);
			view.panelStatus.setTitle(alertLabel);
		}
		else if (warningMsg != null) {
			view.panelStatus.setBorderColor(Color.YELLOW);
			view.panelStatus.setMessage(warningMsg);
		}
		else if (securityLevel > 1) {
			view.panelStatus.setBorderColor(Color.ORANGE);
			view.panelStatus.setTitle(securityLabel);
		}
		else if (securityLevel > 0) {
			view.panelStatus.setBorderColor(Color.YELLOW);
			view.panelStatus.setTitle(securityLabel);
		}
		else if (securityLevel == 0) {
			view.panelStatus.setBorderColor(Color.GREEN);
			view.panelStatus.setTitle(securityLabel);
		}
		view.panelStatus.setCartoucheLevel(securityLevel);
		view.panelStatus.repaint();
	}
	
	@BindOnEvent(GuiIsReady.class)
	@EventArgClassFilter(View480x320.class)
	public void start() {
		setSecurityLevel(0, "Unlocked");
	}

	public boolean isLocked() {
		return securityLevel > 0;
	}

	@GuiTask
	public void unlock() {
		if (!isLocked()) return;
		Map<String, String> passwords = Configuration.getInstance().getPasswords();
		view.showPinCard((code) -> {
			// CANCEL
			if (code == null) {
				clearWarning();
        		view.showDefaultCard();
        	}
			// GOOD PASSWORD
			else if (passwords.containsKey(code)) {
				String log = "Successfull unlock with code '" + passwords.get(code) + "'";
				LOGGER.log(Logs.INFO, log);
				view.appendLog(log, PanelLogs.SUCCESS);
				setSecurityLevel(0, "Unlocked");
			}
			// BAD PASSWORD
			else {
				showWarning("Invalid password");
				view.cardPin.clear();
			}
		});
	}

	public void lock() {
		if (isLocked()) return;
		if (isLocking()) return;
		view.showConfirmDialogCard(
				"<html>You are going to arm the secure system, choose the area to secure:</html>",
				new String[]{ "*HOUSE", "*WORKSHOP", "Cancel" },
				(choice) -> {
					// Cancel
					if ("Cancel".equals(choice)) {
						view.showDefaultCard();
						locking = false;
						return;
					}
					// Protected mode
					if ("WORKSHOP".equals(choice)) {
						LOGGER.log(Logs.INFO, "Enabling secure mode: protected");
						setSecurityLevel(1, "Protected");
						return;
					}
					// Lock mode
					LOGGER.log(Logs.INFO, "Enabling secure mode: locked");
					view.buttonMap.setEnabled(false);
					view.buttonLogs.setEnabled(false);
					view.buttonSettings.setEnabled(false);
					view.buttonLogs.setEnabled(false);
					locking = true;
					view.showCountdownCard(
							"<html>Enabling secure mode, <b>please close the building before leaving</b>...</html>",
							(int) app.get(AppConfiguration.class).getPropertyInt("Security.DelayToLeaveBeforeLock"),
							(cancel) -> {
								locking = false;
								if ("Locking building, please leave now".equals(getWarning())) {
									clearWarning();
								}
								if (cancel) {
									LOGGER.log(Logs.INFO, "Cancel secure mode");
									view.buttonMap.setEnabled(true);
									view.buttonLogs.setEnabled(true);
									view.buttonSettings.setEnabled(true);
									view.buttonLogs.setEnabled(true);
									view.showDefaultCard();
								}
								else {
									LOGGER.log(Logs.INFO, "Secure mode enabled!");
									setSecurityLevel(2, "Locked");
								}
							});
				}
		);
		
	}

	private boolean isLocking() {
		return locking;
	}

	@BindOnEvent(ArduinoEvents.OnDisconnected.class)
	@GuiTask
	public void arduinoDisconnected() {
		alert("RF433 disconnected!");
		showWarning("All security systems are disabled !");
	}
	
	@BindOnEvent(ArduinoEvents.OnConnected.class)
	@GuiTask
	public void arduinoConnected() {
		clearAlert("RF433 disconnected!");
		clearWarning();
	}

	public boolean isLocked(Room room) {
		if (!isLocked()) return false;
		if (getSecurityLevel() >= 2) return true;
		if ("protected".equals(room.getCategory())) return true;
		return false;
	}
	
	@BindOnEvent(OnSecurityAlertRaised.class)
	public void onSecurityAlertRaised(String alert) {
		view.appendLog("Security alert: " + alert, PanelLogs.ALERT);
	}
	
	@BindOnEvent(OnSecurityAlertCleared.class)
	public void onSecurityAlertCleared(String alert) {
		view.appendLog("Alert cleared: " + alert, PanelLogs.SUCCESS);
	}

	@AsynchOperation
	public void notifySecurityThreat(Device device, Room room, IFeature emitter) {
		new Thread(() -> {
			String log = "Warning: suspicious activity in room " + 
				room.getIdentifier() + " (sensor " + device.getClass().getSimpleName()
				+ " " + device.getIdentifier() + ")";
			view.appendLog(log, PanelLogs.WARNING);
			LOGGER.log(Logs.INFO, log);
			securityThreats++;
			if (securityThreats == conf.getPropertyInt("Security.ThreatsThresholdForIntrusion")) {
				alert("Intrusion !");
				if (Timer.isActive("delayToEnterPassword")) return;
				Timer.startCountdown(
						"delayToEnterPassword",
						(int) conf.getPropertyInt("Security.DelayToEnterCodeBeforeAlarm"),
						(remaining) -> {
					if (remaining == 0) {
						showWarning("Police was called !");
						alert("!!! Alarm !!!");
						EventQueue.invokeLater(() -> {
							view.cardMap.getMap()
								.getComponents(Room.class, (r) -> true)
								.forEach((r) -> r.setBorderColor(Color.RED));
							view.cardMap.repaint();
						});
					}
					else EventQueue.invokeLater(() -> 
						showWarning("Enter the code to unlock : " + remaining 
								+ " seconds remaining"));
				});
			}
		}).start();
	}
	
}

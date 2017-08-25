package fr.evolya.domokit;

import java.awt.Color;
import java.awt.EventQueue;
import java.util.logging.Logger;

import fr.evolya.domokit.SecurityMonitor.OnSecurityLevelChanged;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.features.Rf433SecurityTrigger;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;
import fr.evolya.javatoolkit.events.fi.EventProvider;
import fr.evolya.javatoolkit.iot.arduino.ArduinoEvents;
import fr.evolya.javatoolkit.time.Timer;

@EventProvider({OnSecurityLevelChanged.class})
public class SecurityMonitor {

	public static final Logger LOGGER = Logs.getLogger("Security");
	
	@Inject
	public App app;
	
	@Inject
	public View480x320 view;
	
	private boolean locking = false;

	private int securityLevel;
	private String securityLabel;
	private String alertLabel;
	private String warningMsg;
	
	public SecurityMonitor() {
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
			view.cardMap.getMap()
				.forEachFeatures(Rf433SecurityTrigger.class, (device, feature) ->
					feature.resetCounter());
		}
	}
	
	public int getSecurityLevel() {
		return securityLevel;
	}
	
	public String getSecurityLevelLabel() {
		return securityLabel;
	}
	
	public void showAlert(String label) {
		if (label.equals(alertLabel)) return;
		alertLabel = label;
		view.panelStatus.setTitle(label);
		updateStatusPanel();
	}
	
	public void clearAlert() {
		alertLabel = null;
		updateStatusPanel();
	}
	
	public String getAlert() {
		return alertLabel;
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
	
	@FunctionalInterface
	public static interface OnSecurityLevelChanged {
		public void onSecurityLevelChanged(int level, String label);
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
		String password = "12345";
		view.showPinCard((code) -> {
			// CANCEL
			if (code == null) {
				clearWarning();
        		view.showDefaultCard();
        	}
			// GOOD PASSWORD
			else if (code.equals(password)) {
				clearWarning();
				if ("Intrusion detected!!".equals(getAlert())) {
					clearAlert();
				}
				if ("!!! Alarm !!!".equals(getAlert())) {
					clearAlert();
				}
				setSecurityLevel(0, "Unlocked");
				Timer.stopCountdown("delayToEnterPassword");
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
				new String[]{ "*All", "*Partial", "Cancel" },
				(choice) -> {
					// Cancel
					if ("Cancel".equals(choice)) {
						view.showDefaultCard();
						locking = false;
						return;
					}
					// Protected mode
					if ("Partial".equals(choice)) {
						LOGGER.log(Logs.INFO, "Enabling secure mode: protected");
						setSecurityLevel(1, "Protected");
						return;
					}
					// Lock mode
					LOGGER.log(Logs.INFO, "Enabling secure mode: locked");
					view.buttonMap.setEnabled(false);
					view.buttonLogs.setEnabled(false);
					view.buttonSettings.setEnabled(false);
					locking = true;
					showWarning("Locking building, please leave now");
					view.showCountdownCard("<html>Enabling secure mode, <b>please close the building before leaving</b>...</html>", 7, (cancel) -> {
						locking = false;
						if ("Locking building, please leave now".equals(getWarning())) {
							clearWarning();
						}
						if (cancel) {
							LOGGER.log(Logs.INFO, "Cancel secure mode");
							view.buttonMap.setEnabled(true);
							view.buttonLogs.setEnabled(true);
							view.buttonSettings.setEnabled(true);
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
	
	public void notifySecurityTrigger(Device device, String commandName) {

		if (!"TRIGGER".equals(commandName.toUpperCase())) return;
		
		if (!isLocked()) {
			device.setState(Device.State.INTERMEDIATE);
			view.cardMap.repaint();
			// TODO Faire mieux en timer
			Timer.startCountdown("resetDevice" + device.hashCode(), 10, (remaining) -> {
				if (remaining == 0) {
					device.setState(Device.State.IDLE);
					EventQueue.invokeLater(() -> view.cardMap.repaint());
				}
			});
			return;
		}
		
		int count = device.getFirstFeature(Rf433SecurityTrigger.class)
				.addSuspiciousTrigger();
		LOGGER.log(Logs.WARNING, "Suspicious trigger on device '" + device + "' (command " + commandName + ") -> " + count);
		EventQueue.invokeLater(() -> {
			if (count >= 3) {
				device.setState(Device.State.ALERT);
				showAlert("Intrusion detected!!");
				Timer.startCountdown("delayToEnterPassword", 10, (remaining) -> {
					EventQueue.invokeLater(() -> 
						showWarning("Enter the code to unlock : " + remaining + " seconds remaining"));
					if (remaining == 0) {
						showAlert("!!! Alarm !!!");
						showWarning("Police was called !");
					}
				});
			}
			else {
				device.setState(Device.State.INTERMEDIATE);
			}
			view.cardMap.repaint();
		});
	}
	
	@BindOnEvent(ArduinoEvents.OnDisconnected.class)
	@GuiTask
	public void arduinoDisconnected() {
		showAlert("Arduino disconnected!");
		showWarning("All security systems are disabled !");
	}
	
	@BindOnEvent(ArduinoEvents.OnConnected.class)
	@GuiTask
	public void arduinoConnected() {
		if ("Arduino disconnected!".equals(getAlert())) {
			clearAlert();
			clearWarning();
		}
	}
	
}

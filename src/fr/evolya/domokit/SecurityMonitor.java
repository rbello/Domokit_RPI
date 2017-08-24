package fr.evolya.domokit;

import java.awt.Color;
import java.util.logging.Logger;

import fr.evolya.domokit.SecurityMonitor.OnSecurityLevelChanged;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.features.Rf433Emitter;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.panels.PanelStatusStateManager.State;
import fr.evolya.domokit.gui.panels.PanelStatusStateManager.WarningState;
import fr.evolya.domokit.io.Rf433Controller;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CommandReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;
import fr.evolya.javatoolkit.events.fi.EventProvider;

@EventProvider({OnSecurityLevelChanged.class})
public class SecurityMonitor {

	public static final Logger LOGGER = Logs.getLogger("Security");
	
	@Inject
	public App app;
	
	@Inject
	public View480x320 view;
	
	private boolean locking = false;

	private State currentState = null;

	public SecurityMonitor() {
	}
	
	public void setState(State level) {
		if (level == null) throw new NullPointerException("Given level is null");
		if (!view.panelStatus.setState(level)) {
			System.out.println("Rejected: " + level);
			return;
		}
		State state = view.panelStatus.getStateByCategory("SecurityLevel");
		if (state != null && state.equals(currentState )) {
			return;
		}
		LOGGER.log(Logs.INFO, "Security level changed to "+ level.getStateName() + " (" + level.getPriority() + ")");
		app.notify(OnSecurityLevelChanged.class, level);
		resetStatus();
	}
	
	private void resetStatus() {
		// TODO Auto-generated method stub
	}

	public static class StateDefault extends State {
		public String getStateName() {
			return "Unlocked";
		}
		public boolean isAllwaysOnTop() {
			return false;
		}
		@Override
		public String getMessage() {
			return "Everything OK";
		}
		@Override
		public String getStateCategory() {
			return "SecurityLevel";
		}
		@Override
		public Color getColor() {
			return Color.GREEN;
		}
		@Override
		public int getPriority() {
			return 0;
		}
	}
	
	public static class StateLocked extends State {
		public String getStateName() {
			return "Locked";
		}
		public boolean isAllwaysOnTop() {
			return false;
		}
		@Override
		public String getMessage() {
			return "Home is locked";
		}
		@Override
		public String getStateCategory() {
			return "SecurityLevel";
		}
		@Override
		public Color getColor() {
			return Color.YELLOW;
		}
		@Override
		public int getPriority() {
			return 1;
		}
	}
	
	@FunctionalInterface
	public static interface OnSecurityLevelChanged {
		public void onSecurityLevelChanged(State level);
	}
	
	@BindOnEvent(GuiIsReady.class)
	@EventArgClassFilter(View480x320.class)
	public void start() {
		
		view.panelStatus.createCategory("Information", 5);
		view.panelStatus.createCategory("SecurityLevel", 1);
		
		setState(new StateDefault());
		
	}

	public String getCurrentInfoMessage() {
		return "Default";
	}

	public boolean isLocked() {
		return !(view.panelStatus.getStateByCategory("SecurityLevel") instanceof StateDefault);
	}

	@GuiTask
	public void unlock() {
		if (!isLocked()) return;
		String password = "12345";
		view.showPinCard((code) -> {
			// CANCEL
			if (code == null) {
        		view.showDefaultCard();
        		resetStatus();
        	}
			// GOOD PASSWORD
			else if (code.equals(password)) {
				setState(new StateDefault());
				view.buttonMap.setEnabled(true);
				view.buttonLogs.setEnabled(true);
				view.buttonSettings.setEnabled(true);
				view.setButtonLockIcon(false);
				view.showDefaultCard()
					.setReadonly(false);
			}
			// BAD PASSWORD
			else {
				setState(new WarningState("Bad password", 6));
				view.cardPin.clear();
			}
		});
	}

	public void lock() {
		if (isLocked()) return;
		if (isLocking()) return;
		locking = true;
		view.showConfirmDialogCard(
				"<html>You are going to arm the secure system, are you sure ?</html>",
				new String[]{ "*Yes", "No" },
				(choice) -> {
					if ("Yes".equals(choice)) {
						LOGGER.log(Logs.INFO, "Enabling secure mode");
						view.buttonMap.setEnabled(false);
						view.buttonLogs.setEnabled(false);
						view.buttonSettings.setEnabled(false);
						view.showCountdownCard("<html>Enabling secure mode, <b>please close the building before leaving</b>...</html>", 7, (cancel) -> {
							locking = false;
							if (cancel) {
								LOGGER.log(Logs.INFO, "Cancel secure mode");
								view.buttonMap.setEnabled(true);
								view.buttonLogs.setEnabled(true);
								view.buttonSettings.setEnabled(true);
								view.showDefaultCard();
							}
							else {
								LOGGER.log(Logs.INFO, "Secure mode enabled!");
								setState(new StateLocked());
								view.setButtonLockIcon(true);
								view.showDefaultCard().setReadonly(true);
							}
						});
					}
					else {
						view.showDefaultCard();
						locking = false;
					}
				}
		);
		
	}

	private boolean isLocking() {
		return locking;
	}
	
	/**
	 * Execute commands on devices using RF433
	 */
	@BindOnEvent(OnRf433CommandReceived.class)
	@GuiTask
	public void onRf433CommandReceived(Device device, Rf433Emitter command, int code, Rf433Controller ctrl) {
		if ("ON".equals(command.getCommandName())) {
			device.setBackground(Color.GREEN);
		}
		if ("OFF".equals(command.getCommandName())) {
			device.setBackground(Color.RED);
		}
		view.cardMap.repaint();
	};
	
	
	
}

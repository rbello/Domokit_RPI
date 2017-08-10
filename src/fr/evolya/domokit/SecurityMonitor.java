package fr.evolya.domokit;

import java.util.logging.Logger;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.panels.PanelStatus.State;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class SecurityMonitor {

	public static final Logger LOGGER = Logs.getLogger("Security");
	
	@Inject
	public App app;
	
	@Inject
	public View480x320 view;

	private Level level;

	private boolean locking;
	
	public static enum Level {
		
		ZERO   (0, "Unlocked", State.NORMAL),
		ONE    (1, "Locked", State.WARNING),
		TWO    (2, "Suspiscious", State.WARNING),
		THREE  (3, "Intrusion detected", State.ALERT),
		FOUR   (4, "Alert", State.ALERT);
		
		public final int level;
		public final String label;
		public final State state;

		private Level(int level, String label, State state) {
			this.level = level;
			this.label = label;
			this.state = state;
		}

	}
	
	@BindOnEvent(GuiIsReady.class)
	public void start() {
		setSecurityLevel(Level.ZERO);
	}

	protected void setSecurityLevel(Level level) {
		if (this.level == level) return;
		LOGGER.log(Logs.INFO, "Security level changed to "+ level.label + " (" + level.level + ")");
		this.level = level;
		resetStatus();
	}

	public String getCurrentInfoMessage() {
		return "Default";
	}

	public boolean isLocked() {
		return this.level != Level.ZERO;
	}

	@GuiTask
	public void unlock() {
		if (!isLocked()) return;
		String password = "12345";
		view.showPinCard(password, (code) -> {
			// CANCEL
			if (code == null) {
        		view.showDefaultCard();
        		resetStatus();
        	}
			// GOOD PASSWORD
			else if (code.equals(password)) {
				setSecurityLevel(Level.ZERO);
				view.buttonMap.setEnabled(true);
				view.buttonLogs.setEnabled(true);
				view.buttonSettings.setEnabled(true);
				view.setButtonLockIcon(false);
				view.showDefaultCard()
					.setReadonly(false);
			}
			// BAD PASSWORD
			else {
				view.panelStatus.setInfoText("Bad password")
					.setState(State.WARNING);
				view.cardPin.clear();
			}
		});
	}

	public void resetStatus() {
		view.panelStatus
			.setState(level.state)
			.setCartoucheLevel(level.level)
			.setMainText(level.label)
			.setInfoText(getCurrentInfoMessage());
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
								view.buttonMap.setEnabled(false);
								view.buttonLogs.setEnabled(false);
								view.buttonSettings.setEnabled(false);
								view.showDefaultCard();
							}
							else {
								LOGGER.log(Logs.INFO, "Secure mode enabled!");
								setSecurityLevel(Level.ONE);
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
	
}

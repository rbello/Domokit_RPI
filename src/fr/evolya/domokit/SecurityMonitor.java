package fr.evolya.domokit;

import fr.evolya.domokit.gui.SmallView;
import fr.evolya.domokit.gui.PanelStatus.State;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class SecurityMonitor {

	@Inject
	public App app;
	
	@Inject
	public SmallView view;

	private Level level;
	
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
		view.showConfirmDialogCard(
				"Please confirm the locking ?",
				new String[]{ "*Yes", "No" },
				(choice) -> {
					if ("Yes".equals(choice)) {
						view.buttonMap.setEnabled(false);
						view.buttonLogs.setEnabled(false);
						view.buttonSettings.setEnabled(false);
						view.showCountdownCard("Message TODO", 5, (cancel) -> {
							if (cancel) {
								view.buttonMap.setEnabled(false);
								view.buttonLogs.setEnabled(false);
								view.buttonSettings.setEnabled(false);
								view.showDefaultCard();
							}
							else {
								setSecurityLevel(Level.ONE);
								view.setButtonLockIcon(true);
								view.showDefaultCard().setReadonly(true);
							}
						});
					}
					else {
						view.showDefaultCard();
					}
				}
		);
		
	}
	
}

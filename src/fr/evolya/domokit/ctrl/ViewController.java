package fr.evolya.domokit.ctrl;

import fr.evolya.domokit.ctrl.ModuleSecurity.OnSecurityLevelChanged;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.app.event.WindowCloseIntent;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class ViewController {

	@Inject
	public App app;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(ApplicationBuilding.class)
	//Not a @GuiTask
	public void build(App app) {
		
		// Exit
		view.cardSettings.buttonExit.addActionListener(e -> {
			view.showConfirmDialogCard("Confirm EXIT order?", new String[] {"Yes", "*No"}, str -> {
				if ("No".equals(str)) {
					view.showCard("Settings");
					return;
				}
				app.notify(WindowCloseIntent.class, app, this, e);
			});
		});
		
		// Reboot
		view.cardSettings.buttonReboot.addActionListener(e -> {
			view.showConfirmDialogCard("Confirm REBOOT order?", new String[] {"Yes", "*No"}, str -> {
				if ("No".equals(str)) {
					view.showCard("Settings");
					return;
				}
				try {
					Process p = Runtime.getRuntime().exec("sudo reboot");
					int result = p.waitFor();
					if (result != 0) throw new Exception("return " + result);
				}
				catch (Exception ex) {
					app.get(ModuleSecurity.class).showWarning("Failure: "
							+ ex.getMessage());
				}
			});
		});
		
		// Shutdown
		view.cardSettings.buttonShutdown.addActionListener(e -> {
			view.showConfirmDialogCard("Confirm SHUTDOWN order?", new String[] {"Yes", "*No"}, str -> {
				if ("No".equals(str)) {
					view.showCard("Settings");
					return;
				}
				try {
					Process p = Runtime.getRuntime().exec("sudo shutdown -h now");
					int result = p.waitFor();
					if (result != 0) throw new Exception("return " + result);
				}
				catch (Exception ex) {
					app.get(ModuleSecurity.class).showWarning("Failure: "
							+ ex.getMessage());
				}
			});
		});
		
		// Logs
		view.cardSettings.buttonLogs.addActionListener(e -> {
			view.showCard("Logs");
		});
		
		// Lock button
		view.buttonLock.addActionListener(e -> {
			if (app.get(ModuleSecurity.class).isLocked()) {
				app.get(ModuleSecurity.class).unlock();
			}
			else {
				app.get(ModuleSecurity.class).lock();
			}
		});
		
	}
	
	@BindOnEvent(ApplicationStarted.class)
	@GuiTask
	public void showViewWhenApplicationStarted(App app) {
		view.setVisible(true);
		app.notify(GuiIsReady.class, view, app);
	}
	
	@BindOnEvent(WindowCloseIntent.class)
	@GuiTask
	public void closeViewWhenApplicationStop(App app) {
		view.setVisible(false);
		view.dispose();
		app.stop();
	}
	
	@BindOnEvent(OnSecurityLevelChanged.class)
	@GuiTask
	public void makeGuiReadonlyWhenLocked(int level, String label) {
		// Buttons
		boolean enabled = (level == 0);
		view.buttonMap.setEnabled(enabled);
		view.buttonLogs.setEnabled(enabled);
		view.buttonLogs.setEnabled(enabled);
		view.buttonSettings.setEnabled(enabled);
		view.setButtonLockIcon(!enabled);
		view.showDefaultCard().setReadonly(!enabled);
	}
	
}

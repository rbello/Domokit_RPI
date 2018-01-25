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
			app.notify(WindowCloseIntent.class, app, this, e);
		});
		
		// Reboot
		view.cardSettings.buttonReboot.addActionListener(e -> {
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
		view.buttonPrinter.setEnabled(enabled);
		view.buttonNetwork.setEnabled(enabled);
		view.buttonSettings.setEnabled(enabled);
		view.setButtonLockIcon(!enabled);
		view.showDefaultCard().setReadonly(!enabled);
	}
	
}

package fr.evolya.domokit;

import fr.evolya.domokit.ctrl.ModuleArduino;
import fr.evolya.domokit.ctrl.ModuleMap;
import fr.evolya.domokit.ctrl.ModuleRf433;
import fr.evolya.domokit.ctrl.ModuleSecurity;
import fr.evolya.domokit.ctrl.ViewController;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.debug.PanelStatusDebugView;
import fr.evolya.domokit.gui.debug.Rf433DebugView;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.gui.swing.DarkSwingApp;

public class Main {

	public static void main(String[] args) {

		App.setLogFileOutput("./logs/log_%d-%M-%y_%h-%m-%s.txt", Logs.ALL);

		App app = new DarkSwingApp(args);
		
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "HouseStation")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config/app.properties")
			
			.setProperty("Security.ThreatsThresholdForIntrusion", "2")
			.setProperty("Security.DelayToEnterCodeBeforeAlarm", "40")
			.setProperty("Security.DelayToLeaveBeforeLock", "40")
			.setProperty("Map.DeviceHighlightDelay", "20");

		// Arduilink
		app.add(ModuleArduino.class);

		// RF433
		app.add(ModuleRf433.class);
		
		// View
		app.add(View480x320.class);
		app.add(ModuleMap.class);
		app.add(ViewController.class);
		
		// Business object
		app.add(ModuleSecurity.class);
		
		// Network monitor
//		app.add(ModuleNetworkWatcher.class);

		// Debug views
		if (app.getDebugLevel() > 2) {
			app.add(Rf433DebugView.class);
			app.add(PanelStatusDebugView.class);
		}
		
		app.start();
		
	}

}

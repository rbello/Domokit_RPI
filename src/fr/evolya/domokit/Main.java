package fr.evolya.domokit;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.ViewController;
import fr.evolya.domokit.io.ArduinoLink;
import fr.evolya.domokit.io.Rf433Controller;
import fr.evolya.domokit.io.Rf433DebugView;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.gui.swing.SwingApp;

public class Main {

	public static void main(String[] args) {

		int debug = App.init(args);
		
		App app = new SwingApp();
		
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "HouseStation")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config/app.properties");
		
		// Arduilink
		app.add(ArduinoLink.class);

		// RF433
		app.add(Rf433Controller.class);
		app.inject(Rf433Controller.class, "dispatcher", App.class);
		app.when(ApplicationStarted.class).execute((appli) -> {
			new Thread(app.get(Rf433Controller.class)).start();
		});
		
		// View
		app.add(View480x320.class);
		app.add(ViewController.class);
		
		// Business object
		app.add(SecurityMonitor.class);

		if (debug > 0) {
			app.add(Rf433DebugView.class);
		}
		
		app.start();
		
	}

}

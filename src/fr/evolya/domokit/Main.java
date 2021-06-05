package fr.evolya.domokit;

import fr.evolya.domokit.ctrl.ViewController;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.features.FileConfigurationFeature;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.gui.swing.DarkSwingApp;

public class Main {

	public static void main(String[] args) {

		App.setLogFileOutput("./logs/log_%d-%M-%y_%h-%m-%s.txt", Logs.ALL);

		App app = new DarkSwingApp(args);
		
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "JenkinsPiMonitoring")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config/app.properties");

		// View
		app.add(View480x320.class);
		app.add(ViewController.class);
		app.add(FileConfigurationFeature.class);
		
		app.start();
		
	}

}

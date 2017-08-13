package fr.evolya.domokit;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.ViewController;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.gui.swing.SwingApp;

public class Main {

	public static void main(String[] args) {

		App.init();
		
		App app = new SwingApp();
		
		app.setLogLevel(Logs.ALL);
		
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "HouseStation")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config.properties");
		
		app.add(ArduinoLink.class);
		app.add(SecurityMonitor.class);

		app.add(View480x320.class);
		app.add(ViewController.class);
		
		app.when(ApplicationBuilding.class).executeOnGui((appli) -> {
			app.get(View480x320.class).cardMap.setMap(Configuration.getInstance().getMap());
		});
		
		app.when(GuiIsReady.class).executeOnGui((gui, appli) -> {
			((View480x320)gui).showCard("Map");
		});
		
		app.start();
		
	}

}

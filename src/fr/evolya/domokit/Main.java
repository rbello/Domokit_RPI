package fr.evolya.domokit;

import fr.evolya.domokit.gui.SmallView;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.gui.swing.SwingApp;
import fr.evolya.javatoolkit.gui.swing.map.IMap;
import fr.evolya.javatoolkit.gui.swing.map.buildings.BuildingMap;

public class Main {

	public static void main(String[] args) {

		App.init();
		
		App app = new SwingApp();
		
		app.setLogLevel(Logs.ALL);
		
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "HouseStation")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config.properties");
		
		app.add(SmallView.class);
		app.add(NetworkWatcher.class);
		app.add(ArduinoLink.class);
		app.add(SecurityMonitor.class);
		
		IMap map = new BuildingMap(60, 50);
		
		app.when(ApplicationBuilding.class).executeOnGui((appli) -> {
			app.get(SmallView.class).cardMap.setMap(map);
		});
		app.when(GuiIsReady.class).executeOnGui((gui, appli) -> {
			((SmallView)gui).showCard("Map");
		});
		
		app.start();
		
	}

}

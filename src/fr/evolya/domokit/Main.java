package fr.evolya.domokit;

import fr.evolya.domokit.gui.SmallView;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.gui.swing.SwingApp;
import fr.evolya.javatoolkit.gui.swing.map.IMap;
import fr.evolya.javatoolkit.gui.swing.map.MapPanel;
import fr.evolya.javatoolkit.gui.swing.map.buildings.BuildingMap;

public class Main {

	public static void main(String[] args) {

		// Look&Feel et autre
		App.init();
		
		// On fabrique une application
		App app = new SwingApp();
		
		// Debug ALL
		app.setLogLevel(Logs.ALL);
		
		// On lui donne un nom et une version
		app.get(AppConfiguration.class)
			.setProperty("App.Name", "HouseStation")
			.setProperty("App.Version", "1.0.0")
			.setProperty("Config.File", "./config.properties");
		
		// On ajoute une vue
		app.add(SmallView.class);
		// Le module de surveillance du réseau
		app.add(NetworkWatcher.class);
		// Le module de surveillance du réseau
		app.add(ArduinoLink.class);
		
		IMap map = new BuildingMap(60, 50);
		
		app.when(ApplicationBuilding.class).executeOnGui((appli) -> {
			app.get(SmallView.class).getPanelMain("Map", MapPanel.class).setMap(map);
		});
		app.when(GuiIsReady.class).executeOnGui((gui, appli) -> {
			((SmallView)gui).showCard("Map");
		});
		
		// On la lance
		app.start();
		
	}

}

package fr.evolya.domokit;

import fr.evolya.domokit.gui.SmallView;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent.Orientation;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent.Position;
import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.domokit.gui.map.simple.Door;
import fr.evolya.domokit.gui.map.simple.Map;
import fr.evolya.domokit.gui.map.simple.Room;
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
		
		app.add(SmallView.class);
		//app.add(NetworkWatcher.class);
		app.add(ArduinoLink.class);
		app.add(SecurityMonitor.class);
		
		IMap map = new Map();
		
		map.addComponent(new Room(0, 0, 4, 4, "Salle de bain"));
		map.addComponent(new Room(4, 0, 6, 4, "Garage"));
		map.addComponent(new Room(10, 0, 4, 4, "Chambre 1"));
		map.addComponent(new Room(14, 0, 5, 10, "Salon"));
		map.addComponent(new Room(0, 4, 2, 2, "WC"));
		map.addComponent(new Room(2, 4, 12, 2, "Couloir"));
		map.addComponent(new Room(0, 6, 4, 4, "Chambre 3"));
		map.addComponent(new Room(4, 6, 5, 4, "Chambre 2"));
		map.addComponent(new Room(9, 6, 5, 4, "Cuisine"));
		
		map.getRoomByName("Couloir", Room.class)
			.setBorderRightWidth(0)
			.addBorderElement(new Door())
				.setOrientation(Orientation.RIGHT)
				.setPosition(Position.CENTER);
		
		//map.addComponent();
		
		app.when(ApplicationBuilding.class).executeOnGui((appli) -> {
			app.get(SmallView.class).cardMap.setMap(map);
		});
		app.when(GuiIsReady.class).executeOnGui((gui, appli) -> {
			((SmallView)gui).showCard("Map");
		});
		
		app.start();
		
	}

}

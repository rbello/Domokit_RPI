package fr.evolya.domokit.config;

import java.io.File;

import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.javatoolkit.xmlconfig.XmlConfig;

public class Configuration {

	private IMap map;
	private static Configuration INSTANCE;

	public Configuration(XmlConfig cfg) {
		INSTANCE = this;
		this.map = cfg.getBean("map", IMap.class);
		if (this.map == null)
			throw new NullPointerException("No 'map' bean defined in config file");
	}

	public static synchronized Configuration getInstance() {
		if (INSTANCE != null) return INSTANCE;
		File file = new File("./config/map.xml");
		XmlConfig cfg = new XmlConfig();
		try {
			cfg.addConfiguration(file);
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
		return new Configuration(cfg);
	}

	public IMap getMap() {
//		IMap map = new Map();
//		
//		map.addComponent(new Room(0, 0, 4, 4, "Salle de bain"));
//		map.addComponent(new Room(4, 0, 6, 4, "Garage"));
//		map.addComponent(new Room(10, 0, 4, 4, "Chambre 1"));
//		map.addComponent(new Room(14, 0, 5, 10, "Salon"));
//		map.addComponent(new Room(0, 4, 2, 2, "WC"));
//		map.addComponent(new Room(2, 4, 12, 2, "Couloir"));
//		map.addComponent(new Room(0, 6, 4, 4, "Chambre 3"));
//		map.addComponent(new Room(4, 6, 5, 4, "Chambre 2"));
//		map.addComponent(new Room(9, 6, 5, 4, "Cuisine"));
//		
//		map.getComponentByName("Couloir", Room.class)
//			.setBorderRightWidth(0)
//			.addBorderElement(new Door())
//				.setOrientation(Orientation.RIGHT)
//				.setPosition(Position.CENTER);
//		
//		new Tile(2, 2, "Name")
//			.setBackground(Color.RED)
//			.setIcon(Icons.HOME)
//			.addTo(map);
//		
//		new Tile(4, 4, "Name")
//			.setBackground(Color.BLUE)
//			.setIcon(Icons.WIFI)
//			.addTo(map);
//		
//		new Tile(6, 6, "Name")
//			.setBackground(Color.ORANGE)
//			.setIcon(Icons.CONNECTIVITY)
//			.addTo(map);
		
		return map;
	}

}

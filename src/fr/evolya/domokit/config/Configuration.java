package fr.evolya.domokit.config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.xmlconfig.XmlConfig;

public class Configuration {

	private static Configuration INSTANCE;

	private IMap map;
	private Map<String, String> passwords;

	public Configuration(XmlConfig cfg) {
		// Map
		if (!cfg.hasBean("map")) {
			throw new NullPointerException("Missing 'map' bean defined in config file");
		}
		this.map = cfg.getBean("map", IMap.class);
		
		// Passwords
		if (!cfg.hasBean("passwords")) {
			throw new NullPointerException("Missing 'passwords' bean defined in config file");
		}
		HashMap<?, ?> passwords = cfg.getBean("passwords", HashMap.class);
		this.passwords = new HashMap<>();
		for (Object key : passwords.keySet()) {
			Object value = passwords.get(key);
			if (!(key instanceof String) || !(value instanceof String)) {
				throw new IllegalArgumentException("All keys and values of 'passwords' bean must have type string");
			}
			this.passwords.put((String)key, (String)value);
		}
	}

	public static synchronized Configuration getInstance() {
		if (INSTANCE != null) {
			return INSTANCE;
		}
		try {
			// TODO Déplacer le chemin dans le main la config
			INSTANCE = new Configuration(new XmlConfig(new File("./config/map.xml")));
			return INSTANCE;
		}
		catch (Exception e) {
			App.LOGGER.log(Logs.ERROR, "Unable to load XML configuration file", e);
			throw new RuntimeException(e);
		}
	}

	public IMap getMap() {
		return map;
	}
	
	public Map<String, String> getPasswords() {
		return passwords;
	}

}

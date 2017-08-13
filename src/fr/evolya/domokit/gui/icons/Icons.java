package fr.evolya.domokit.gui.icons;

import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;

import javax.swing.ImageIcon;

public enum Icons {
	
	SEARCH			("search", "001", "050"),
	MESSAGE			("message", "009", "045"),
	HOME			("home", "038", "021"),
	PLAY			("play-button", "024", "003"),
	EXCLAMATION		("exclamation-button", "025", "002"),
	EQUALS			("equal", "008", "046"),
	CONNECTIVITY	("connectivity", "041", "016"),
	WIFI			("wifi-signal", "026", "001"),
	SETTINGS		("settings", "005", "047");
	
	private String name;
	
	private String file16x16;
	private String file24x24;

	private static HashMap<String, Image> CACHE;
	
	public static final String BASE = "/fr/evolya/domokit/gui/icons/";

	private Icons(String name, String file16x16, String file24x24) {
		this.name = name;
		this.file16x16 = String.format(BASE + "16x16/%s-%s.png", file16x16, name);
		this.file24x24 = String.format(BASE + "24x24/%s-%s.png", file24x24, name);
	}
	
	public String getName() {
		return name;
	}

	public String getFile16x16() {
		return file16x16;
	}

	public String getFile24x24() {
		return file24x24;
	}
	
	public Image getImage(Size size) {
		return getImage(size, false);
	}

	public Image getImage(Size size, boolean cache) {
		if (!cache) {
			return loadImage(getPath(size));
		}
		if (CACHE == null) {
			CACHE = new HashMap<String, Image>();
		}
		String key = name + " " + size;
		if (!CACHE.containsKey(key)) {
			CACHE.put(key, loadImage(getPath(size)));
		}
		return CACHE.get(key);
	}
	
	private String getPath(Size size) {
		switch (size) {
		case SIZE16X16 : return file16x16;
		case SIZE24X24 : return file24x24;
		}
		throw new UnsupportedOperationException("Invalid icon size: " + size);
	}

	public static Image loadImage(String filename) {
		try {
			return Toolkit.getDefaultToolkit().getImage(
					Icons.class.getResource(filename));
		}
		catch (Throwable t) {
			System.out.println("Unable to load icon " + filename + ": " + t.getMessage());
			return null;
		}
	}
	
	public static enum Size {
		SIZE16X16, SIZE24X24
	}

	public ImageIcon getIcon(Size size) {
		return new ImageIcon(getImage(size));
	}
	
	public ImageIcon getIcon(Size size, boolean cache) {
		return new ImageIcon(getImage(size, cache));
	}

	public static ImageIcon getIcon(Icons icon, Size size) {
		return icon.getIcon(size);
	}
	
	public static ImageIcon getIcon(Icons icon, Size size, boolean cache) {
		return icon.getIcon(size, cache);
	}
	
	public static Image getImage(Icons icon, Size size) {
		return icon.getImage(size);
	}
	
	public static Image getImage(Icons icon, Size size, boolean cache) {
		return icon.getImage(size, cache);
	}
	
}

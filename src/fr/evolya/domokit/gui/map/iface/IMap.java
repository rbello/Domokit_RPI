package fr.evolya.domokit.gui.map.iface;

import java.awt.Graphics;

import fr.evolya.domokit.gui.map.MapPanel;

public interface IMap {

	void paint(Graphics graphic, MapPanel panel);

	void addComponent(IMapComponent component);

//	IAbsolutePositionningComponent getRoomByName(String name);
	
	<T extends IAbsolutePositionningComponent> T getRoomByName(String name, Class<T> type);

}

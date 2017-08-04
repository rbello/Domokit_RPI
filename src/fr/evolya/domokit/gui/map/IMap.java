package fr.evolya.domokit.gui.map;

import java.awt.Graphics;

public interface IMap {

	void paint(Graphics graphic, MapPanel panel);

	void addComponent(IMapComponent component);

}

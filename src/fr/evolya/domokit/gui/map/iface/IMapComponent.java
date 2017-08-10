package fr.evolya.domokit.gui.map.iface;

import java.awt.Graphics;
import java.awt.Point;

import fr.evolya.domokit.gui.map.MapPanel;

public abstract interface IMapComponent {

	String getIdentifier();

	void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft);

}

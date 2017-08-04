package fr.evolya.domokit.gui.map;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

public interface IMapComponent {

	void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft);

	Rectangle getBounds();

}

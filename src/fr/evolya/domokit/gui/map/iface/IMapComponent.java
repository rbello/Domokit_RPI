package fr.evolya.domokit.gui.map.iface;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.MapPanel;

public abstract interface IMapComponent {
	
	// Bounds
	Rectangle getBounds();
//	int getX();
//	int getY();
//	int getWidth();
//	int getHeight();
	void setX(int x);
	void setY(int y);
	void setWidth(int w);
	void setHeight(int h);

	// Identifier
	IMapComponent setIdentifier(String label);
	String getIdentifier();

	// Repaint
	void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft);
	
	// Parents
	void setParent(IMapComponent parent);
	IMapComponent getParent();
	
}

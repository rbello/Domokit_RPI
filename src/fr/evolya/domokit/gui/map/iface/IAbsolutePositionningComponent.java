package fr.evolya.domokit.gui.map.iface;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

public interface IAbsolutePositionningComponent extends IMapComponent {
	
	// Background color
	void setBackground(Color red);
	Color getBackground();
	
	// TODO Déplacer dans une interface spécifique aux host ?
	void addBorderElement(IBorderPositionningComponent component);
	
	boolean isInside(int x, int y);
	boolean isInside(int x, int y, double ratio, Point topLeft);

	void addTo(IMap map);
	
	Rectangle getTargetBounds(double ratio, Point topLeft);
	
}
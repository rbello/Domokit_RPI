package fr.evolya.domokit.gui.map.iface;

import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.Rectangle;

public interface IAbsolutePositionningComponent extends IMapComponent {
	
	Rectangle getBounds();
	
//	int getBorderTopWidth();
//	int getBorderRightWidth();
//	int getBorderBottomWidth();
//	int getBorderLeftWidth();
//	
//	IAbsolutePositionningComponent setBorderTopWidth(int value);
//	IAbsolutePositionningComponent setBorderRightWidth(int value);
//	IAbsolutePositionningComponent setBorderBottomWidth(int value);
//	IAbsolutePositionningComponent setBorderLeftWidth(int value);


	Component getGraphic();

	// TODO Déplacer dans une interface spécifique aux host ?
	IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component);
	
	boolean isInside(int x, int y, double ratio, Point topLeft);

	IAbsolutePositionningComponent setBackground(Color red);

}

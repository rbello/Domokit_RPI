package fr.evolya.domokit.gui.map.iface;

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

	IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component);
	
}

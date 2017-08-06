package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.JPanel;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;

public class Room extends AbstractAbsolutePositionningComponent {
	
	private int borderTopWidth = 3;
	private int borderRightWidth = 3;
	private int borderBottomWidth = 3;
	private int borderLeftWidth = 3;

	public Room(Rectangle bounds, String componentLabel) {
		super(bounds, componentLabel);
	}
	
	public Room(int x, int y, int width, int height, String componentLabel) {
		super(new Rectangle(x, y, width, height), componentLabel);
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		int x = (int) (bounds.x * ratio + topLeft.x);
		int y = (int) (bounds.y * ratio + topLeft.y);
		int w = (int) (bounds.width * ratio);
		int h = (int) (bounds.height * ratio);
		graphic.setColor(backgroundColor == null ? panel.getBackground() : backgroundColor);
		graphic.fillRect(x, y, w, h);
		graphic.setColor(panel.getForeground());
		// Top border
		graphic.fillRect(x - borderLeftWidth, y - borderTopWidth, w + borderRightWidth, borderTopWidth);
		// Bottom border
		graphic.fillRect(x - borderLeftWidth, y + h, w + borderRightWidth, borderBottomWidth);
		// Left border
		graphic.fillRect(x - borderLeftWidth, y, borderLeftWidth, h);
		// Right border
		graphic.fillRect(x + w - borderLeftWidth, y, borderRightWidth, h);
	}
	
	public int getBorderTopWidth() {
		return borderTopWidth;
	}

	public IAbsolutePositionningComponent setBorderTopWidth(int borderTopWidth) {
		this.borderTopWidth = borderTopWidth;
		return this;
	}

	public int getBorderRightWidth() {
		return borderRightWidth;
	}

	public IAbsolutePositionningComponent setBorderRightWidth(int borderRightWidth) {
		this.borderRightWidth = borderRightWidth;
		return this;
	}

	public int getBorderBottomWidth() {
		return borderBottomWidth;
	}

	public IAbsolutePositionningComponent setBorderBottomWidth(int borderBottomWidth) {
		this.borderBottomWidth = borderBottomWidth;
		return this;
	}

	public int getBorderLeftWidth() {
		return borderLeftWidth;
	}

	public IAbsolutePositionningComponent setBorderLeftWidth(int borderLeftWidth) {
		this.borderLeftWidth = borderLeftWidth;
		return this;
	}

	@Override
	public Component getGraphic() {
		return new JPanel();
	}

}

package fr.evolya.domokit.gui.map.simple;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;

public class Room implements IAbsolutePositionningComponent {
	
	protected final Rectangle bounds;
	protected final String label;
	
	private int borderTopWidth = 3;
	private int borderRightWidth = 3;
	private int borderBottomWidth = 3;
	private int borderLeftWidth = 3;

	public Room(Rectangle bounds, String componentLabel) {
		this.bounds = bounds;
		this.label = componentLabel;
	}
	
	public Room(int x, int y, int width, int height, String componentLabel) {
		this(new Rectangle(x, y, width, height), componentLabel);
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		int x = (int) (bounds.x * ratio + topLeft.x);
		int y = (int) (bounds.y * ratio + topLeft.y);
		int w = (int) (bounds.width * ratio);
		int h = (int) (bounds.height * ratio);
		graphic.setColor(panel.getBackground());
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
	
	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public String getIdentifier() {
		return label;
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
	public IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component) {
		// TODO Auto-generated method stub
		component.setParent(this);
		return component;
	}

}

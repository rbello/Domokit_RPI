package fr.evolya.domokit.gui.map.simple;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;

public class Room extends AbstractAbsolutePositionningComponent {
	
	private int borderTopWidth = 3;
	private int borderRightWidth = 3;
	private int borderBottomWidth = 3;
	private int borderLeftWidth = 3;
	
	public Room() {
		super();
	}

	public Room(Rectangle bounds, String componentLabel) {
		super(bounds, componentLabel);
	}
	
	public Room(int x, int y, int width, int height, String componentLabel) {
		super(new Rectangle(x, y, width, height), componentLabel);
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		Rectangle b = getTargetBounds(ratio, topLeft);
		graphic.setColor(background == null ? panel.getBackground() : background);
		graphic.fillRect(b.x, b.y, b.width, b.height);
		graphic.setColor(panel.getForeground());
		// Top border
		graphic.fillRect(b.x - borderLeftWidth, b.y - borderTopWidth, b.width + borderRightWidth, borderTopWidth);
		// Bottom border
		graphic.fillRect(b.x - borderLeftWidth, b.y + b.height, b.width + borderRightWidth, borderBottomWidth);
		// Left border
		graphic.fillRect(b.x - borderLeftWidth, b.y, borderLeftWidth, b.height);
		// Right border
		graphic.fillRect(b.x + b.width - borderLeftWidth, b.y, borderRightWidth, b.height);
		
		graphic.drawString(getIdentifier(), b.x - borderLeftWidth + 4, b.y + 12);
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

}

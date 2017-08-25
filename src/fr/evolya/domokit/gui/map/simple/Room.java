package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMapComponent;

public class Room extends AbstractAbsolutePositionningComponent implements IMapContainer {
	
	private int borderTopWidth = 3;
	private int borderRightWidth = 3;
	private int borderBottomWidth = 3;
	private int borderLeftWidth = 3;
	
	private Color borderColor = null;
	
	private String category = null;
	
	private List<IMapComponent> components = new ArrayList<>();
	
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
		
		graphic.setColor(borderColor == null ? panel.getForeground() : borderColor);
		// Top border
		graphic.fillRect(b.x - borderLeftWidth, b.y - borderTopWidth, b.width + borderRightWidth, borderTopWidth);
		// Bottom border
		graphic.fillRect(b.x - borderLeftWidth, b.y + b.height - borderBottomWidth, b.width + borderRightWidth, borderBottomWidth);
		// Left border
		graphic.fillRect(b.x - borderLeftWidth, b.y, borderLeftWidth, b.height);
		// Right border
		graphic.fillRect(b.x + b.width - borderLeftWidth, b.y, borderRightWidth, b.height);
		
		// Label
		graphic.setColor(panel.getForeground());
		graphic.drawString(getIdentifier(), b.x - borderLeftWidth + 4, b.y + 12);
		
		// Sub-components
		for (IMapComponent component : components) {
			component.paint(graphic, panel, ratio, topLeft);
		}
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

	public Color getBorderColor() {
		return borderColor;
	}

	public void setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	
	@Override
	public void addComponent(IMapComponent component) {
		this.components.add(component);
		component.setParent(this);
	}
	
	@Override
	public List<IMapComponent> getComponents() {
		return this.components;
	}

}

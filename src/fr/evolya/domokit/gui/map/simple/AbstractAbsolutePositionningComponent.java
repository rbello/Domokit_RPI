package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;

public abstract class AbstractAbsolutePositionningComponent
	implements IAbsolutePositionningComponent {
	
	protected final Rectangle bounds;
	protected final String label;
	protected Color backgroundColor;
	
	public AbstractAbsolutePositionningComponent(Rectangle bounds, String componentLabel) {
		this.bounds = bounds;
		this.label = componentLabel;
	}
	
	public AbstractAbsolutePositionningComponent(int x, int y, int width, int height, String componentLabel) {
		this(new Rectangle(x, y, width, height), componentLabel);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

	@Override
	public String getIdentifier() {
		return label;
	}

	@Override
	public IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component) {
		// TODO Auto-generated method stub
		component.setParent(this);
		return component;
	}
	
	@Override
	public boolean isInside(int x, int y, double ratio, Point topLeft) {
		int x1 = (int) (bounds.x * ratio + topLeft.x);
		int y1 = (int) (bounds.y * ratio + topLeft.y);
		int x2 = (int) (bounds.width * ratio) + x1;
		int y2 = (int) (bounds.height * ratio) + y1;
		return (x >= x1 && x <= x2 && y >= y1 && y <= y2);
	}
	
	@Override
	public String toString() {
		return label;
	}
	
	@Override
	public IAbsolutePositionningComponent setBackground(Color color) {
		this.backgroundColor = color;
		return this;
	}
	
	public Rectangle getTargetBounds(double ratio, Point topLeft) {
		int x = (int) (bounds.x * ratio + topLeft.x);
		int y = (int) (bounds.y * ratio + topLeft.y);
		int w = (int) (bounds.width * ratio);
		int h = (int) (bounds.height * ratio);
		return new Rectangle(x, y, w, h);
	}

}

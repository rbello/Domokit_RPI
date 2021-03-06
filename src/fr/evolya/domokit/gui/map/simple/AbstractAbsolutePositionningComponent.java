package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.domokit.gui.map.iface.IMapComponent;
import fr.evolya.javatoolkit.exceptions.NotImplementedException;

public abstract class AbstractAbsolutePositionningComponent
	implements IAbsolutePositionningComponent {
	
	protected Rectangle bounds;
	protected String identifier;
	protected Color backgroundColor;
	protected IMapComponent parent;
	
	public AbstractAbsolutePositionningComponent() {
		this.bounds = new Rectangle(0, 0, 1, 1);
		this.identifier = "";
	}
	
	public AbstractAbsolutePositionningComponent(Rectangle bounds, String componentLabel) {
		this.bounds = bounds;
		this.identifier = componentLabel;
	}
	
	public AbstractAbsolutePositionningComponent(int x, int y, int width, int height, String componentLabel) {
		this(new Rectangle(x, y, width, height), componentLabel);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}
	
	@Override
	public IMapComponent setIdentifier(String label) {
		this.identifier = label;
		return this;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public void addBorderElement(IBorderPositionningComponent component) {
		// TODO Auto-generated method stub
		component.setParent(this);
		throw new NotImplementedException();
	}
	
	@Override
	public boolean isInside(int x, int y) {
		return isInside(x, y, 1, new Point(0, 0));
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
		return identifier;
	}
	
	@Override
	public void setBackground(Color color) {
		this.backgroundColor = color;
	}
	
	@Override
	public Color getBackground() {
		return backgroundColor;
	}
	
	@Override
	public Rectangle getTargetBounds(double ratio, Point topLeft) {
		int x = (int) (bounds.x * ratio + topLeft.x);
		int y = (int) (bounds.y * ratio + topLeft.y);
		int w = (int) (bounds.width * ratio);
		int h = (int) (bounds.height * ratio);
		return new Rectangle(x, y, w, h);
	}
	
	@Override
	public void addTo(IMap map) {
		map.addComponent(this);
	}
	
	@Override
	public void setX(int x) {
		bounds.x = x;
	}
	
	@Override
	public void setY(int y) {
		bounds.y = y;
	}
	
	@Override
	public void setWidth(int w) {
		bounds.width = w;
	}
	
	@Override
	public void setHeight(int h) {
		bounds.height = h;
	}
	
	@Override
	public void setParent(IMapComponent parent) {
		this.parent = parent;
	}

	@Override
	public IMapComponent getParent() {
		return parent;
	}

}

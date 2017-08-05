package fr.evolya.domokit.gui.map.simple;

import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;

public abstract class AbstractAbsolutePositionningComponent implements IAbsolutePositionningComponent {
	
	protected final Rectangle bounds;
	protected final String label;
	
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

}

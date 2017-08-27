package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.javatoolkit.exceptions.NotImplementedException;

public class GroundMark extends AbstractAbsolutePositionningComponent {

	protected Color borderColor;
	
	public GroundMark() {
		super();
	}
	
	@Override
	public void addBorderElement(IBorderPositionningComponent component) {
		throw new NotImplementedException();
	}
	
	public Color getBorderColor() {
		return borderColor;
	}
	
	public GroundMark setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		
		// Relative positionning (x/y in pixels from parent top left)
		Rectangle b = null;
		if (getParent() != null) {
			Rectangle bounds = getParent().getBounds();
			int x = (int) (bounds.x * ratio + topLeft.x) + this.bounds.x;
			int y = (int) (bounds.y * ratio + topLeft.y) + this.bounds.y;
			int w = this.bounds.width;
			int h = this.bounds.height;
			b = new Rectangle(x, y, w, h);
		}
		
		// Absolute positionning (x/y in meters)
		else {
			b = getTargetBounds(ratio, topLeft);
		}
		
		// Background
		graphic.setColor(backgroundColor == null ? panel.getBackground() : backgroundColor);
		graphic.fillRect(b.x, b.y, b.width, b.height);
		
		// Border
		graphic.setColor(borderColor == null ? panel.getForeground() : borderColor);
		graphic.drawRect(b.x, b.y, b.width, b.height);
	}

}

package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;

import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.icons.Icons.Size;
import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.javatoolkit.exceptions.NotImplementedException;

public class Badge extends AbstractAbsolutePositionningComponent {

	protected Icons icon;
	protected Color borderColor;
	
	public Badge() {
		super();
		setBackground(Color.GRAY);
	}
	
	public Badge(int x, int y, String componentLabel) {
		super(x, y, 1, 1, componentLabel);
		setBackground(Color.GRAY);
	}

	@Override
	public void addBorderElement(IBorderPositionningComponent component) {
		throw new NotImplementedException();
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		
		// Relative positionning (x/y in pixels from parent top left)
		Rectangle b = null;
		if (getParent() != null) {
			Rectangle bounds = getParent().getBounds();
			int x = (int) (bounds.x * ratio + topLeft.x) + this.bounds.x;
			int y = (int) (bounds.y * ratio + topLeft.y) + this.bounds.y;
			int w = (int) (this.bounds.width * ratio);
			int h = (int) (this.bounds.height * ratio);
			b = new Rectangle(x, y, w, h);
		}
		
		// Absolute positionning (x/y in meters)
		else {
			b = getTargetBounds(ratio, topLeft);
		}
		
		// Border
		graphic.setColor(borderColor == null ? panel.getForeground() : borderColor);
		graphic.fillOval(b.x - 1, b.y - 1, b.width + 4, b.height + 4);
		
		// Background
		graphic.setColor(background == null ? panel.getBackground() : background);
		graphic.fillOval(b.x, b.y, b.width + 2, b.height + 2);
		
		// Icon
		if (icon == null) return;
		ImageIcon img = Icons.getIcon(icon, Size.SIZE16X16, true);
		int inset = 3;
		graphic.drawImage(img.getImage(),
				// Target x/y
				b.x + inset, b.y + inset,
				// Target w/h
				b.x + inset + 16, b.y + inset + 16,
				// Source x/y/w/h
				0, 0, 16, 16, null);
	}

	public Badge setIcon(Icons icon) {
		this.icon = icon;
		return this;
	}
	
	public Icons getIcon() {
		return this.icon;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public Badge setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		return this;
	}

}

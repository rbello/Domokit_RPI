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

public class Tile extends AbstractAbsolutePositionningComponent {

	private Icons icon;
	private Color borderColor;
	
	public Tile(int x, int y, String componentLabel) {
		super(x, y, 1, 1, componentLabel);
	}

	@Override
	public IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component) {
		throw new NotImplementedException();
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		Rectangle b = getTargetBounds(ratio, topLeft);
		
		// Border
		graphic.setColor(borderColor == null ? panel.getForeground() : borderColor);
		graphic.fillOval(b.x - 1, b.y - 1, b.width + 4, b.height + 4);
		
		// Background
		graphic.setColor(backgroundColor == null ? panel.getBackground() : backgroundColor);
		graphic.fillOval(b.x, b.y, b.width + 2, b.height + 2);
		
		// Icon
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

	public Tile setIcon(Icons icon) {
		this.icon = icon;
		return this;
	}
	
	public Icons getIcon() {
		return this.icon;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public Tile setBorderColor(Color borderColor) {
		this.borderColor = borderColor;
		return this;
	}

	@Override
	public Tile setBackground(Color color) {
		return (Tile) super.setBackground(color);
	}

}

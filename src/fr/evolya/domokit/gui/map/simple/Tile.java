package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;

import javax.swing.ImageIcon;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.javatoolkit.exceptions.NotImplementedException;

public class Tile extends AbstractAbsolutePositionningComponent {

	private Image image;
	
	private String icon;
	private Color backgroundColor;

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
		graphic.setColor(backgroundColor == null ? panel.getBackground() : backgroundColor);
		graphic.setColor(Color.BLUE);
		image = getImage("009-message");
		ImageIcon img = new ImageIcon(image);
		int w = img.getIconWidth(), h = img.getIconHeight(), inset = 3;
		graphic.fillOval(b.x, b.y, b.width + 2, b.height + 2);
		
		
		graphic.drawImage(image,
				// Target x/y
				b.x + inset + 1, b.y + inset + 1,
				// Target w/h
				b.x + inset + 16, b.y + inset + 16,
				// Source x/y/w/h
				0, 0, 16, 15, null);
	}

	public static Image getImage(String filename) {
		return Toolkit.getDefaultToolkit().getImage(
				Tile.class.getResource("/fr/evolya/domokit/gui/icons/16x16/" + filename + ".png"));
	}
	
}

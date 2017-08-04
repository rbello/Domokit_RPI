package fr.evolya.domokit.gui.map.buildings;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.IMapComponent;
import fr.evolya.domokit.gui.map.MapPanel;

public class RectangleRoom implements IMapComponent {

	private Rectangle bounds;
	private String name;

	public RectangleRoom(Rectangle size, String name) {
		this.bounds = size;
		this.name = name;
	}
	
	public RectangleRoom(int x, int y, int width, int height, String name) {
		this(new Rectangle(x, y, width, height), name);
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
		int thin = 3;
		graphic.fillRect(x - thin, y - thin, w + thin, thin);
		graphic.fillRect(x - thin, y + h, w + thin, thin);
		graphic.fillRect(x - thin, y, thin, h);
		graphic.fillRect(x - thin + w, y, thin, h);
	}

	@Override
	public Rectangle getBounds() {
		return bounds;
	}

}

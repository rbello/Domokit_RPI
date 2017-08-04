package fr.evolya.domokit.gui.map.buildings;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import fr.evolya.domokit.gui.map.IMap;
import fr.evolya.domokit.gui.map.IMapComponent;
import fr.evolya.domokit.gui.map.MapPanel;

public class BuildingMap implements IMap {

	private List<IMapComponent> components;
	
	private int cols = 17;
	private int rows = 10;
	
	private int width = 10; // meters
	private int height = 10; // meters
	
	public BuildingMap() {
		this.components = new ArrayList<>();
	}

	public void add(IMapComponent component) {
		this.components.add(component);
	}
	
	public List<IMapComponent> getComponents() {
		return new ArrayList<>(this.components);
	}
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel) {
		
		
		
		paintBackground(graphic, panel);
		
		double ratioX = (double)(panel.getWidth() - 20) / (double)(width);
		double ratioY = (double)(panel.getHeight() - 20) / (double)(height);
		double ratio = Math.min(ratioX, ratioY);
		
		int deltaX = panel.getWidth() - (int)(ratio * width);
		int deltaY = panel.getHeight() - (int)(ratio * height);
		
		Point topLeft = new Point(deltaX / 2, deltaY / 2);
		
//		System.out.println("Map of " + width + "x" + height + " meters");
//		System.out.println("Panel of " + panel.getWidth() + "x" + panel.getHeight() + " meters");
//		System.out.println("Ratio=" + ratio);
//		System.out.println("1 meter = " + (int)ratio + " pixels (" + (ratio * width) + ")");
		
		// Components
		for (IMapComponent component : components) {
			component.paint(graphic, panel, ratio, topLeft);
		}
	}

	public void paintBackground(Graphics graphic, MapPanel panel) {
		int step = panel.getWidth() / cols;
		
		graphic.setColor(panel.getForeground());
		
		// Columns
		for (int x = 0; x < cols; x++) {
			graphic.drawLine(x * step, 0, x * step, panel.getHeight());
		}
		graphic.drawLine(panel.getWidth() - 1, 0, panel.getWidth() - 1, panel.getHeight());
		// Rows
		step = panel.getHeight() / rows;
		for (int y = 0; y <= rows; y++) {
			graphic.drawLine(0, y * step, panel.getWidth(), y * step);
		}
	}

	@Override
	public void addComponent(IMapComponent component) {
		this.components.add(component);
		Rectangle r = component.getBounds();
		width = Math.max(width, r.x + r.width);
		height = Math.max(height, r.y + r.height);
	}

}

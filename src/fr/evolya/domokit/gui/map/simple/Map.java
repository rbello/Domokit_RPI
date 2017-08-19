package fr.evolya.domokit.gui.map.simple;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.domokit.gui.map.iface.IMapComponent;
import fr.evolya.javatoolkit.code.funcint.Filter;

public class Map implements IMap {

	private List<IMapComponent> components;
	
	private int cols = 17;
	private int rows = 10;
	
	private int width = 10; // meters
	private int height = 10; // meters

	private MapPanel panel;

	private List<MapArea> areas;
	
	public Map() {
		this.components = new ArrayList<>();
		this.areas = new ArrayList<>();
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
	public void paint(Graphics graphic) {

		paintBackground(graphic, panel);
		
		double ratio = getRatio(panel);
		Point topLeft = getTopLeftPoint(panel, ratio);
		
//		System.out.println("Map of " + width + "x" + height + " meters");
//		System.out.println("Panel of " + panel.getWidth() + "x" + panel.getHeight() + " meters");
//		System.out.println("Ratio=" + ratio);
//		System.out.println("1 meter = " + (int)ratio + " pixels (" + (ratio * width) + ")");
		
		// Components
		for (IMapComponent component : components) {
			component.paint(graphic, panel, ratio, topLeft);
		}
	}

	private Point getTopLeftPoint(MapPanel panel, double ratio) {
		int deltaX = panel.getWidth() - (int)(ratio * width);
		int deltaY = panel.getHeight() - (int)(ratio * height);
		return new Point(deltaX / 2, deltaY / 2);
	}

	private double getRatio(MapPanel panel) {
		double ratioX = (double)(panel.getWidth() - 20) / (double)(width);
		double ratioY = (double)(panel.getHeight() - 20) / (double)(height);
		return Math.min(ratioX, ratioY);
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
		if (component instanceof IAbsolutePositionningComponent) {
			Rectangle bounds = ((IAbsolutePositionningComponent)component).getBounds();
			width = Math.max(width, bounds.x + bounds.width);
			height = Math.max(height, bounds.y + bounds.height);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAbsolutePositionningComponent> T getComponentByName(String name, Class<T> type) {
		for (IMapComponent c : components) {
			if (c instanceof IAbsolutePositionningComponent) {
				if (((IAbsolutePositionningComponent)c).getIdentifier().equals(name)) 
					return (T) c;
			}
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IMapComponent> void forEachComponents(Class<T> typeFilter, Consumer<T> consumer) {
		for (IMapComponent component : components) {
			if (typeFilter.isInstance(component)) {
				consumer.accept((T)component);
			}
		}
	}

	@Override
	public IAbsolutePositionningComponent getComponentAt(int x, int y) {
		// Compute viewport
		double ratio = getRatio(panel);
		Point topLeft = getTopLeftPoint(panel, ratio);
		// Fetch components
		return getFirstComponent(IAbsolutePositionningComponent.class, (c) -> 
			c.isInside(x, y, ratio, topLeft)
		);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAbsolutePositionningComponent> T getFirstComponent(Class<T> type, Filter<T> filter) {
		for (IMapComponent component : components) {
			if (type.isInstance(component)) {
				if (filter.accept((T) component)) {
					return (T) component;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <T extends IAbsolutePositionningComponent> List<T> getComponents(Class<T> type, Filter<T> filter) {
		List<T> result = new ArrayList<>();
		for (IMapComponent component : components) {
			if (type.isInstance(component)) {
				if (filter.accept((T) component)) {
					result.add((T) component);
				}
			}
		}
		return result;
	}

	@Override
	public void setParentPanel(MapPanel parentPanel) {
		this.panel = parentPanel;
	}

	@Override
	public void addArea(MapArea zone) {
		this.areas.add(zone);
	}

}

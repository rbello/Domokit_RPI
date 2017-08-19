package fr.evolya.domokit.gui.map.iface;

import java.awt.Graphics;
import java.util.List;
import java.util.function.Consumer;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.simple.MapArea;
import fr.evolya.javatoolkit.code.funcint.Filter;

public interface IMap {

	void paint(Graphics graphic);

	void setParentPanel(MapPanel parentPanel);

	void addComponent(IMapComponent component);

	List<IMapComponent> getComponents();
	
	<T extends IAbsolutePositionningComponent> T getComponentByName(String name, Class<T> type);

	<T extends IMapComponent> void forEachComponents(Class<T> typeFilter, Consumer<T> consumer);

	IAbsolutePositionningComponent getComponentAt(int x, int y);
	
	<T extends IAbsolutePositionningComponent> T getFirstComponent(Class<T> type, Filter<T> filter);
	
	<T extends IAbsolutePositionningComponent> List<T> getComponents(Class<T> type, Filter<T> filter);

	void addArea(MapArea zone);
	
}

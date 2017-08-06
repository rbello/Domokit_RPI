package fr.evolya.domokit.gui.map.iface;

import java.awt.Graphics;
import java.util.List;
import java.util.function.Consumer;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.javatoolkit.code.funcint.Filter;

public interface IMap {

	void paint(Graphics graphic);

	void setParentPanel(MapPanel parentPanel);

	void addComponent(IMapComponent component);

	List<IMapComponent> getComponents();
	
//	IAbsolutePositionningComponent getRoomByName(String name);
	
	<T extends IAbsolutePositionningComponent> T getRoomByName(String name, Class<T> type);

	<T extends IMapComponent> void getComponents(Class<T> typeFilter, Consumer<T> consumer);

	IAbsolutePositionningComponent getComponentAt(int x, int y);
	
	<T extends IAbsolutePositionningComponent> T getComponent(Class<T> type, Filter<T> filter);


}

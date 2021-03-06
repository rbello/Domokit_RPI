package fr.evolya.domokit.gui.map.simple;

import java.util.List;

import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMapComponent;

public interface IMapContainer {

	void addComponent(IMapComponent component);
	
	List<IMapComponent> getComponents();

	IAbsolutePositionningComponent getMapComponentAt(int x, int y);
	
}

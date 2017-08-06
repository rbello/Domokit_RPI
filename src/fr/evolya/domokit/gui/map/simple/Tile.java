package fr.evolya.domokit.gui.map.simple;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Point;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.javatoolkit.exceptions.NotImplementedException;

public class Tile extends AbstractAbsolutePositionningComponent {

	public Tile(int x, int y, int width, int height, String componentLabel) {
		super(x, y, width, height, componentLabel);
		// TODO Auto-generated constructor stub
	}

	@Override
	public IBorderPositionningComponent addBorderElement(IBorderPositionningComponent component) {
		throw new NotImplementedException();
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getGraphic() {
		// TODO Auto-generated method stub
		return null;
	}
	
}

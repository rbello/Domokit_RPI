package fr.evolya.domokit.gui.map.simple;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;

import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IBorderPositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMapComponent;

public class Door implements IBorderPositionningComponent {

	private IAbsolutePositionningComponent parent;

	@Override
	public String getIdentifier() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void paint(Graphics graphic, MapPanel panel, double ratio, Point topLeft) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IAbsolutePositionningComponent getParent() {
		return this.parent;
	}

	@Override
	public IBorderPositionningComponent setOrientation(Orientation right) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public IBorderPositionningComponent setPosition(Position center) {
		// TODO Auto-generated method stub
		return this;
	}

	@Override
	public void setParent(IAbsolutePositionningComponent parent) {
		this.parent = parent;
	}

	@Override
	public IMapComponent setIdentifier(String label) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setParent(IMapComponent parent) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Rectangle getBounds() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setX(int x) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setY(int y) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setWidth(int w) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setHeight(int h) {
		// TODO Auto-generated method stub
		
	}

}

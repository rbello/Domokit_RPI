package fr.evolya.domokit.gui.map.iface;

public interface IBorderPositionningComponent extends IMapComponent {
	
	void setParent(IAbsolutePositionningComponent parent);

	IAbsolutePositionningComponent getParent();
	
	IBorderPositionningComponent setOrientation(Orientation right);

	IBorderPositionningComponent setPosition(Position center);
	
	public static enum Orientation {
		TOP, RIGHT, BOTTOM, LEFT
	}
	
	public static enum Position {
		LEFT, CENTER, RIGHT
	}

}

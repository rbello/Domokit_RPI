package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.evolya.domokit.gui.icons.Icons;

public class Device extends Badge {
	
	private List<IFeature> features;
	
	public Device(int x, int y, String componentLabel) {
		super(x, y, componentLabel);
		features = new ArrayList<>();
		setBackground(Color.MAGENTA);
		setIcon(Icons.HOME);
	}
	
	public Device addFeature(IFeature feature) {
		this.features.add(feature);
		return this;
	}
	
}

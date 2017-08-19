package fr.evolya.domokit.gui.map.simple;

import java.util.ArrayList;
import java.util.List;

import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.map.iface.IFeature;

public class Device extends Badge {
	
	private List<IFeature> features;
	
	public Device(int x, int y, String componentLabel) {
		super(x, y, componentLabel);
		features = new ArrayList<>();
		setIcon(Icons.HOME);
	}
	
	public Device addFeature(IFeature feature) {
		this.features.add(feature);
		return this;
	}

	public <T extends IFeature> boolean hasFeature(Class<T> featureType) {
		for (IFeature feature : features) {
			if (featureType.isInstance(feature)) return true;
		}
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IFeature> List<T> getFeatures(Class<T> featureType) {
		List<T> result = new ArrayList<>();
		for (IFeature feature : features) {
			if (featureType.isInstance(feature)) result.add((T) feature);
		}
		return result;
	}
	
}

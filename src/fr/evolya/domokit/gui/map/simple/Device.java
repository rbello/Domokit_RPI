package fr.evolya.domokit.gui.map.simple;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.map.iface.IFeature;

public class Device extends Badge {
	
	private List<IFeature> features;
	
	private State state = State.UNKNOWN;
	
	public Device() {
		this(0, 0, "");
	}
	
	public Device(int x, int y, String componentLabel) {
		super(x, y, componentLabel);
		features = new ArrayList<>();
		setIcon(Icons.HOME);
		super.setBackground(Color.GRAY);
	}
	
	public Device addFeature(IFeature feature) {
		this.features.add(feature);
		feature.setDevice(this);
		return this;
	}

	public <T extends IFeature> boolean hasFeature(Class<T> featureType) {
		for (IFeature feature : features) {
			if (featureType.isInstance(feature)) return true;
		}
		return false;
	}
	
	public List<IFeature> getFeatures() {
		return features;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IFeature> List<T> getFeatures(Class<T> featureType) {
		List<T> result = new ArrayList<>();
		for (IFeature feature : features) {
			if (featureType.isInstance(feature)) result.add((T) feature);
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends IFeature> T getFirstFeature(Class<T> featureType) {
		for (IFeature feature : features) {
			if (featureType.isInstance(feature)) return (T) feature;
		}
		return null;
	}
	
	public static enum State {
		UNKNOWN(Color.GRAY),
		OFF(Color.RED),
		ON(Color.GREEN),
		ALERT(Color.RED),
		IDLE(Color.GRAY),
		INTERMEDIATE(Color.ORANGE);
		public final Color color;
		private State(Color color) {
			this.color = color;
		}
	}
	
	public State getState() {
		return this.state;
	}
	
	public void setState(State state) {
		if (this.state == state) return;
		super.setBackground(state.color);
	}
	
	/**
	 * Use setState() instead
	 */
	@Override
	@Deprecated
	public void setBackground(Color color) {
		super.setBackground(color);
	}

}

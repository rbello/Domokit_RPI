package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.gui.map.iface.IFeature;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.javatoolkit.app.App;

public abstract class AbstractFeature implements IFeature {

	private Device device;
	
	public AbstractFeature() {
		App.instance().magic(this);
	}
	
	public Device getDevice() {
		return device;
	}

	@Override
	public void setDevice(Device device) {
		this.device = device;
	}

}

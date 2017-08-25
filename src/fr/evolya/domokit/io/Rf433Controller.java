package fr.evolya.domokit.io;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.gui.map.features.Rf433Emitter;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeEmitted;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeReceived;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CommandReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventProvider;

@EventProvider({OnRf433CodeReceived.class, OnRf433CodeEmitted.class, OnRf433CommandReceived.class})
public class Rf433Controller {
	
	@Inject
	public App app;
	
	@FunctionalInterface
	public static interface OnRf433CodeReceived {
		void onRf433CodeReceived(int code);
	}
	
	@FunctionalInterface
	public static interface OnRf433CodeEmitted {
		void onRf433CodeEmitted(int code);
	}
	
	@FunctionalInterface
	public static interface OnRf433CommandReceived {
		void onRf433CommandReceived(Device device, Rf433Emitter command, int code, 
				Rf433Controller ctrl);
	}
	
	/**
	 * When a RF433 code is received by wireless sensor, try to search the device
	 * mapped to this code.
	 */
	@BindOnEvent(OnRf433CodeReceived.class)
	public void onRf433CodeReceived(int code) {
		Configuration.getInstance().getMap()
			.getComponents(Device.class, (c) -> c.hasFeature(Rf433Emitter.class))
			.forEach((device) -> {
				for (Rf433Emitter feature : device.getFeatures(Rf433Emitter.class)) {
					if (feature.getRfCode() == code) {
						// RF code was sent by this device
						app.notify(OnRf433CommandReceived.class, device, feature, code, this);
						return;
					}
				}
			});
	}

}

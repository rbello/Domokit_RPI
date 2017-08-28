package fr.evolya.domokit.ctrl;

import java.util.List;
import java.util.function.Consumer;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CodeEmitted;
import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CodeReceived;
import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CommandReceived;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.features.Rf433Emitter;
import fr.evolya.domokit.gui.map.iface.IMapComponent;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.IMapContainer;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventProvider;

@EventProvider({OnRf433CodeReceived.class, OnRf433CodeEmitted.class, OnRf433CommandReceived.class})
public class ModuleRf433 {
	
	@Inject
	public View480x320 view;
	
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
				ModuleRf433 ctrl);
	}
	
	/**
	 * When a RF433 code is received by wireless sensor, try to search the device
	 * mapped to this code.
	 */
	@BindOnEvent(OnRf433CodeReceived.class)
	public void onRf433CodeReceived(int code) {
		Rf433Emitter emitter = searchRf433Emitter(code);
		if (emitter == null) return;
		if (emitter.accept(code)) {
			// RF code was sent by this device
			app.notify(OnRf433CommandReceived.class, emitter.getDevice(), emitter, code, this);
		}
	}
	
	public Rf433Emitter searchRf433Emitter(int code) {
		return searchRf433Emitter(code, Configuration.getInstance().getMap().getComponents());
	}
	
	protected Rf433Emitter searchRf433Emitter(int code, List<IMapComponent> components) {
		for (IMapComponent component : components) {
			if (component instanceof IMapContainer) {
				Rf433Emitter result = searchRf433Emitter(code, 
						((IMapContainer)component).getComponents());
				if (result != null) {
					return result;
				}
			}
			if (component instanceof Device) {
				if (!((Device)component).hasFeature(Rf433Emitter.class)) continue;
				Device device = (Device)component;
				// Fetch features
				for (Rf433Emitter feature : device.getFeatures(Rf433Emitter.class)) {
					if (feature.getRfCode() == code) return feature;
				}
			}
		}
		return null;
	}
	
	public void forEachRf433Emitters(Consumer<Rf433Emitter> consumer) {
		forEachRf433Emitters(Configuration.getInstance().getMap().getComponents(), consumer);
	}
	
	protected void forEachRf433Emitters(List<IMapComponent> components, 
			Consumer<Rf433Emitter> consumer) {
		
		for (IMapComponent component : components) {
			if (component instanceof IMapContainer) {
				forEachRf433Emitters(((IMapContainer)component).getComponents(), consumer);
			}
			if (component instanceof Device) {
				if (!((Device)component).hasFeature(Rf433Emitter.class)) continue;
				Device device = (Device)component;
				// Fetch features
				device.getFeatures(Rf433Emitter.class).stream().forEach((feature) -> {
					consumer.accept(feature);
				});
			}
		}
	}
	
	/**
	 * Displays received RF433 commands in logs card.
	 */
	@BindOnEvent(OnRf433CommandReceived.class)
	@GuiTask
	public void printLogOnRf433CommandReceived(Device device, Rf433Emitter command, int code, ModuleRf433 ctrl) {
		view.appendLog("[RF433] Rcvd: " + device + " -> " + command);
	};
	
}

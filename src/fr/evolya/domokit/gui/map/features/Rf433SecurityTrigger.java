package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.SecurityMonitor;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.io.Rf433Controller;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CommandReceived;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class Rf433SecurityTrigger extends AbstractFeature {

	@Inject
	public SecurityMonitor monitor;
	private int level = 0;
	
	@BindOnEvent(OnRf433CommandReceived.class)
	public void onRf433CommandReceived(Device device, Rf433Emitter command, int code, 
			Rf433Controller ctrl) {
		if (getDevice() != device) return;
		monitor.notifySecurityTrigger(device, command.commandName);
	}

	public int addSuspiciousTrigger() {
		return ++level ;
	}

	public void resetCounter() {
		level = 0;
	}
	
}

package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.SecurityMonitor;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.io.Rf433Controller;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CommandReceived;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class Rf433OnOffStates extends AbstractFeature {

	@Inject
	public SecurityMonitor monitor;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(OnRf433CommandReceived.class)
	@GuiTask
	public void onRf433CommandReceived(Device device, Rf433Emitter command, int code, 
			Rf433Controller ctrl) {
		if (getDevice() != device) return;
		if ("ON".equals(command.getCommandName())) {
			device.setState(Device.State.ON);
		}
		else if ("OFF".equals(command.getCommandName())) {
			device.setState(Device.State.OFF);
		}
		else return;
		view.cardMap.repaint();
	}

}

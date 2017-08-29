package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CommandReceived;
import fr.evolya.domokit.ctrl.ModuleSecurity;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationStarting;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class Rf433OnOffStates extends AbstractFeature {

	@Inject
	public ModuleSecurity monitor;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(ApplicationStarting.class)
	public void onStart(App app) {
		app
			.when(OnRf433CommandReceived.class)
			.onlyOn((device) -> device == getDevice())
			.executeOnGui((device, command, code, ctrl) -> {
				if ("ON".equals(command.getCommandName())) {
					device.setState(Device.State.ON);
				}
				else if ("OFF".equals(command.getCommandName())) {
					device.setState(Device.State.OFF);
				}
				else return;
				view.cardMap.repaint();
			});
	}
	
}

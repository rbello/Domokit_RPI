package fr.evolya.domokit.gui.map.features;

import java.awt.Color;
import java.awt.EventQueue;

import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CommandReceived;
import fr.evolya.domokit.ctrl.ModuleSecurity;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.Room;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationStarting;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.code.utils.Utils;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.time.Timer;

public class Rf433SecurityTrigger extends AbstractFeature {

	@Inject
	public ModuleSecurity monitor;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(ApplicationStarting.class)
	public void onStart(App app) {
		app
			.when(OnRf433CommandReceived.class)
			.onlyOn((device) -> device == getDevice())
			.execute((device, command, code, ctrl) -> {
				
				int duration = (int) app.get(AppConfiguration.class)
						.getPropertyInt("Map.DeviceHighlightDelay");

				if (monitor.isLocked()) {
					Room room = Utils.getCasted(device.getParent(), Room.class);
					if (room == null || monitor.isLocked(room)) {
						room.setBackground(new Color(114, 45, 0));
						monitor.notifySecurityThreat(device, room, command);
						duration *= 4; // TODO
					}
				}
				
				device.setState(Device.State.INTERMEDIATE);
				EventQueue.invokeLater(() -> view.cardMap.repaint());
				
				Timer.startCountdown("resetDevice" + device.hashCode(), duration, (remaining) -> {
					if (remaining == 0) {
						device.setState(Device.State.IDLE);
						EventQueue.invokeLater(() -> view.cardMap.repaint());
					}
				});
				
			});
	}

}

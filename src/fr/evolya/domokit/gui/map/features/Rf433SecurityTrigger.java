package fr.evolya.domokit.gui.map.features;

import java.awt.Color;
import java.awt.EventQueue;

import fr.evolya.domokit.ctrl.ModuleRf433;
import fr.evolya.domokit.ctrl.ModuleSecurity;
import fr.evolya.domokit.ctrl.ModuleRf433.OnRf433CommandReceived;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.Device.State;
import fr.evolya.domokit.gui.map.simple.Room;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.code.utils.Utils;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.time.Timer;

public class Rf433SecurityTrigger extends AbstractFeature {

	@Inject
	public ModuleSecurity monitor;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(OnRf433CommandReceived.class)
	@GuiTask
	public void onRf433CommandReceived(Device device, Rf433Emitter command, int code, 
			ModuleRf433 ctrl) {
		if (getDevice() != device) return;
		
		if (monitor.isLocked()) {
			Room room = Utils.getCasted(device.getParent(), Room.class);
			if (room == null) return;
			if (monitor.isLocked(room)) {
				room.setBackground(new Color(114, 45, 0));
				device.setState(State.INTERMEDIATE);
				view.cardMap.repaint();
				return;
			}
		}
		
		device.setState(Device.State.INTERMEDIATE);
		view.cardMap.repaint();
		Timer.startCountdown("resetDevice" + device.hashCode(), 20, (remaining) -> {
			if (remaining == 0) {
				device.setState(Device.State.IDLE);
				EventQueue.invokeLater(() -> view.cardMap.repaint());
			}
		});
		
	}

}

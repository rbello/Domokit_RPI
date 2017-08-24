package fr.evolya.domokit.io;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.panels.PanelStatusStateManager.AlertState;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.ApplicationStopping;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.iot.Arduilink;
import fr.evolya.javatoolkit.iot.Arduino;

public class ArduinoLink {
	
	public static class ArduinoNotConnectedAlert extends AlertState {
		public ArduinoNotConnectedAlert(String message, int priority) {
			super("Arduino is not connected", 10);
		}
	}
	
	@Inject
	public View480x320 view;
	
	protected Arduilink arduilink;

	private Arduino arduino;

	@BindOnEvent(ApplicationBuilding.class)
	public void onApplicationBuilding(App app) {
		
		arduino = Arduino.getFirst();
		
		if (!arduino.isBound()) {
			view.appendLog("No arduino connected");
		}
		
		// Connection event
		arduino.when(Arduino.OnConnected.class)
			.execute((port) -> {
				view.appendLog("[Arduino] Connected to " + port.getName());
			});
		arduino.when(Arduino.OnDisconnected.class)
			.execute((port, ex) -> {
				view.appendLog("[Arduino] Disconnected from " + port);
			});
		
		// Convert to an Arduilink instance
		arduilink = new Arduilink(arduino);
		
		// Autoclose
		app.when(ApplicationStopping.class)
			.execute(a -> {
				try {
					arduilink.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					arduilink = null;
				}
			});
		
	}
	
	@BindOnEvent(ApplicationStarted.class)
	public void onApplicationStarted(App app) {
		
		arduino.start();

		// When a sensor send a data
		arduilink.when(Arduilink.OnDataReceived.class)
			.execute((data) -> {
				if (data.sensor == null) return;
				if ("RF433Receiver".equals(data.sensor.name)) {
					app.notify(OnRf433CodeReceived.class, new Integer(data.value));
				}
			});
		
		// When an error occured
		arduino.when(Arduino.OnReceiveError.class)
			.execute((ex) -> {
				view.appendLog("[Arduino] Error " + ex.getClass().getSimpleName()
						+ ": " + ex.getMessage());
			});
			
	}
	
}

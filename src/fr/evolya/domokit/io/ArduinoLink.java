package fr.evolya.domokit.io;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.ApplicationStopping;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.iot.arduilink.Arduilink;
import fr.evolya.javatoolkit.iot.arduilink.ArduilinkEvents;
import fr.evolya.javatoolkit.iot.arduino.Arduino;
import fr.evolya.javatoolkit.iot.arduino.ArduinoEvents;

public class ArduinoLink {
	
	@Inject
	public View480x320 view;
	
	protected Arduilink arduilink;

	private Arduino arduino;

	@BindOnEvent(ApplicationBuilding.class)
	public void onApplicationBuilding(App app) {
		
		arduino = Arduino.getFirst();
		
		if (!arduino.isBound()) {
			view.appendLog("No arduino connected");
			app.notify(ArduinoEvents.OnDisconnected.class, null, null);
		}
		
		// Connection event
		arduino.when(ArduinoEvents.OnConnected.class)
			.execute((port) -> {
				// Route to application
				app.notify(ArduinoEvents.OnConnected.class, port);
				view.appendLog("[Arduino] Connected to " + port.getName());
			});
		arduino.when(ArduinoEvents.OnDisconnected.class)
			.execute((port, ex) -> {
				// Route to application
				app.notify(ArduinoEvents.OnDisconnected.class, port, ex);
				view.appendLog("[Arduino] Disconnected from " + port);
			});
		
		// When an error occured
		arduino.when(ArduinoEvents.OnReceiveError.class)
			.execute((ex) -> {
				view.appendLog("[Arduino] Error " + ex.getClass().getSimpleName()
						+ ": " + ex.getMessage());
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
		
		// When a RF433 sensor send a data
		arduilink.when(ArduilinkEvents.OnDataReceived.class)
		.execute((data) -> {
			if (data.sensor == null) return;
			if ("RF433Receiver".equals(data.sensor.name)) {
				app.notify(OnRf433CodeReceived.class, new Integer(data.value));
			}
		});
		
	}
	
	@BindOnEvent(ApplicationStarted.class)
	public void onApplicationStarted(App app) {
		arduino.start();
	}
	
}

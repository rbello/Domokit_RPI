package fr.evolya.domokit;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.ApplicationStopping;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.iot.Arduilink;
import fr.evolya.javatoolkit.iot.Arduilink.Sensor;
import fr.evolya.javatoolkit.iot.Arduino;

public class ArduinoLink {
	
	@Inject
	public View480x320 view;
	
	protected Arduilink link;

	@BindOnEvent(ApplicationStarted.class)
	public void start(App app) {
		Arduino arduino = Arduino.getFirst();
		if (arduino == null) {
			view.appendLog("No arduino connected");
			return;
		}
		view.appendLog("Available ports:");
		Arduino.getPortIdentifiers().forEach(port -> view.appendLog(" " + port.getName()));
		link = open(arduino, app);
	}
	
	private Arduilink open(Arduino uno, App app) {
		try {
			
			// Convert to an Arduilink instance
			Arduilink lnk = new Arduilink(uno);
			
			// Connection event
			uno.when(Arduino.OnStateChanged.class)
				.execute((state, ex) -> {
					if (state) {
						view.appendLog("[Arduino] Connected to " + uno.getComPort().getName());
					}
					else {
						view.appendLog("[Arduino] Disconnected from " + uno.getComPort().getName());
					}
				});
			
			// Autoclose
			app.when(ApplicationStopping.class)
				.execute(a -> {
					try {
						lnk.close();
					}
					catch (Exception e) {
						e.printStackTrace();
					}
					finally {
						link = null;
					}
				});
			
			// When an arduilink node join the network
			lnk.when(Arduilink.OnLinkEstablished.class)
				.execute((node) -> {
					view.appendLog(String.format("[Arduino] Node %s is connected to %s (protocol %s)",
							node.nodeId, node.link.getPortName(), node.version));
				});
			
			// When a sensor is connected
			lnk.when(Arduilink.OnSensorConnected.class)
				.execute((sensor) -> {
					String type = sensor.is(Sensor.S_ACTION) ? "actuator" : "sensor";
					view.appendLog(String.format("[Arduino] New %s %s (Node=%s Type=%s)", 
							type, sensor.name, sensor.nodeId, sensor.type));
				});
			
			// When a sensor send a data
			lnk.when(Arduilink.OnDataReceived.class)
				.execute((data) -> {
					if (data.sensor == null) return;
					view.appendLog("[Arduino] Sensor " + data.sensor.name + ": " 
							+ data.value + " " + data.sensor.unit);
				});
			
			// When an error occured
			lnk.when(Arduino.OnReceiveError.class)
				.execute((ex) -> {
					view.appendLog("[Arduino] Error " + ex.getClass().getSimpleName()
							+ ": " + ex.getMessage());
				});
			
			// Open the connection
			uno.open();
			
			return lnk;
		}
		catch (Exception ex) {
			view.appendLog("Error: " + ex.getMessage());
			return null;
		}
	}
	
}

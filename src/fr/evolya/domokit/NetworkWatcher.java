package fr.evolya.domokit;

import java.net.InetAddress;

import fr.evolya.domokit.gui.SmallView;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.gui.swing.SwingApp;
import fr.evolya.javatoolkit.net.discover.NetworkWatcher2;
import fr.evolya.javatoolkit.net.discover.TypeInterface;
import fr.evolya.javatoolkit.net.discover.TypeNetwork;
import fr.evolya.javatoolkit.net.discover.events.OnInternetAvailable;
import fr.evolya.javatoolkit.net.discover.events.OnNetworkConnected;

public class NetworkWatcher extends NetworkWatcher2 {

	private SmallView view;
	
	// When the application is ready...
	@BindOnEvent(GuiIsReady.class)
	public void start(SmallView view, SwingApp app) {
		// ...store the view instance...
		this.view = view;
		// ...and start network watcher service.
		start();
	}
	
	@BindOnEvent(OnInternetAvailable.class)
	public void handle(TypeNetwork network) {
		view.appendLog("Internet is available ("
				+ network.getAttribute("RemoteHost") + ", " + network.getAttribute("GeoipCityName") + ")");
	}
	
	@BindOnEvent(OnNetworkConnected.class)
	public void handle(TypeNetwork net, TypeInterface iface, InetAddress addr) {
		view.appendLog("Connected network: " 
			+ net.getName() + " (" + iface.getName() + addr + ")");
	}
	
}

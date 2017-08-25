package fr.evolya.domokit.ctrl;

import java.awt.Color;
import java.net.InetAddress;

import fr.evolya.domokit.gui.View480x320;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;
import fr.evolya.javatoolkit.net.discover.NetworkWatcher2;
import fr.evolya.javatoolkit.net.discover.TypeInterface;
import fr.evolya.javatoolkit.net.discover.TypeNetwork;
import fr.evolya.javatoolkit.net.discover.events.OnInternetAvailable;
import fr.evolya.javatoolkit.net.discover.events.OnInternetUnavailable;
import fr.evolya.javatoolkit.net.discover.events.OnNetworkConnected;
import fr.evolya.javatoolkit.net.discover.events.OnNetworkDisconnected;

public class ModuleNetworkWatcher extends NetworkWatcher2 {

	@Inject
	public View480x320 view;
	
	@BindOnEvent(GuiIsReady.class)
	@EventArgClassFilter(View480x320.class)
	public void start(View480x320 view, App app) {
		start();
	}
	
	@BindOnEvent(OnInternetAvailable.class)
	@GuiTask
	public void onInternetAvailable(TypeNetwork network) {
		view.cardSettings.fieldWANStatus.setText("Connected by " + network.getGateway());
		view.cardSettings.fieldWANStatus.setForeground(Color.GREEN);
	}
	
	@BindOnEvent(OnInternetUnavailable.class)
	@GuiTask
	public void onInternetUnavailable(TypeNetwork network) {
		view.cardSettings.fieldWANStatus.setText("Disconnected");
		view.cardSettings.fieldWANStatus.setForeground(Color.RED);
	}
	
	@BindOnEvent(OnNetworkConnected.class)
	@GuiTask
	public void onNetworkConnected(TypeNetwork net, TypeInterface iface, InetAddress addr) {
		view.cardSettings.fieldLANStatus.setText("Connected " + net.getName() + " at " + addr);
		view.cardSettings.fieldLANStatus.setForeground(Color.GREEN);
	}
	
	@BindOnEvent(OnNetworkDisconnected.class)
	@GuiTask
	public void onNetworkDisconnected(TypeNetwork net, TypeInterface iface, InetAddress addr) {
		view.cardSettings.fieldLANStatus.setText("Disconnected");
		view.cardSettings.fieldLANStatus.setForeground(Color.RED);
	}
	
}

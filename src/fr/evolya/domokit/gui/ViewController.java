package fr.evolya.domokit.gui;

import java.awt.Color;
import java.awt.Point;

import javax.swing.SwingUtilities;

import fr.evolya.domokit.SecurityMonitor;
import fr.evolya.domokit.SecurityMonitor.OnSecurityLevelChanged;
import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.gui.map.MapPanel.MapListener;
import fr.evolya.domokit.gui.map.features.Rf433Emitter;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.io.Rf433Controller;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CommandReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.app.event.WindowCloseIntent;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;

public class ViewController {

	@Inject
	public App app;
	
	@Inject
	public View480x320 view;
	
	@BindOnEvent(ApplicationBuilding.class)
	//Not a @GuiTask
	public void build(App app) {
		
		// Exit
		view.cardSettings.buttonExit.addActionListener(e -> {
			app.notify(WindowCloseIntent.class, app, this, e);
		});
		
		// Lock button
		view.buttonLock.addActionListener(e -> {
			if (app.get(SecurityMonitor.class).isLocked()) {
				app.get(SecurityMonitor.class).unlock();
			}
			else {
				app.get(SecurityMonitor.class).lock();
			}
		});
		
		view.cardMap.addListener(new MapListener() {
			@Override
			public void onSlide(Point from, Point to) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onClick(Point p) {
				IAbsolutePositionningComponent target = view.cardMap.getMapComponentAt(p.x, p.y);
				if (target == null) return;
				System.out.println("Click on " + p.getX() + "x" + p.getY() + " = " + target);
				target.setBackground(Color.RED);
				view.cardMap.repaint();
				SwingUtilities.invokeLater(() -> {
					try {
						Thread.sleep(60);
					} catch (InterruptedException e1) { }
					target.setBackground(null);
					view.cardMap.repaint();
				});
			}
		});
		
		app.get(View480x320.class).cardMap.setMap(Configuration.getInstance().getMap());
	}
	
	@BindOnEvent(ApplicationStarted.class)
	@GuiTask
	public void run(App app) {
		view.setVisible(true);
		app.notify(GuiIsReady.class, view, app);
	}
	
	@BindOnEvent(WindowCloseIntent.class)
	@GuiTask
	public void close(App app) {
		view.setVisible(false);
		view.dispose();
		app.stop();
	}
	
	@BindOnEvent(GuiIsReady.class)
	@EventArgClassFilter(View480x320.class)
	@GuiTask
	public void initView() {
		view.showDefaultCard();
	}
	
//	@BindOnEvent(OnSecurityLevelChanged.class)
//	@GuiTask
//	public void onSecurityLevelChanged(SecurityLevel level) {
//		if (level.level < 1) return;
//		// TODO Finish
//		view.cardMap.getMap()
//			.getComponents(Device.class, (c) -> c.hasFeature(WindowOpeningSensor.class))
//			.forEach((device) -> device.setBackground(Color.DARK_GRAY));
//		view.cardMap.repaint();
//	}
	
	/**
	 * Displays received RF433 commands in logs card.
	 */
	@BindOnEvent(OnRf433CommandReceived.class)
	@GuiTask
	public void onRf433CommandReceived(Device device, Rf433Emitter command, int code, Rf433Controller ctrl) {
		view.appendLog("[RF433] Rcvd: " + device + " -> " + command);
	};
	
	@BindOnEvent(OnSecurityLevelChanged.class)
	@GuiTask
	public void onSecurityLevelChanged(int level, String label) {
		boolean enabled = (level == 0);
		view.buttonMap.setEnabled(enabled);
		view.buttonLogs.setEnabled(enabled);
		view.buttonSettings.setEnabled(enabled);
		view.setButtonLockIcon(!enabled);
		view.showDefaultCard().setReadonly(!enabled);
	}
	
}

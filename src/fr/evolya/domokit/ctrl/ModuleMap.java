package fr.evolya.domokit.ctrl;

import java.awt.Color;
import java.awt.Point;
import java.util.List;

import javax.swing.SwingUtilities;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.ctrl.ModuleSecurity.OnSecurityLevelChanged;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.map.MapPanel.MapListener;
import fr.evolya.domokit.gui.map.features.Rf433SecurityTrigger;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.Room;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.events.fi.EventArgClassFilter;

public class ModuleMap {

	@Inject
	public View480x320 view;
	
	@BindOnEvent(ApplicationBuilding.class)
	//Not a @GuiTask
	public void build(App app) {
		
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
	
	@BindOnEvent(GuiIsReady.class)
	@EventArgClassFilter(View480x320.class)
	@GuiTask
	public void showDefaultCardOnApplicationReady() {
		view.showDefaultCard();
	}
	
	@BindOnEvent(OnSecurityLevelChanged.class)
	@GuiTask
	public void resetMapColorsWhenSecurityLevelChanged(int level, String label) {
		view.cardMap.getMap()
			.getComponents(Device.class, (room) -> room.hasFeature(Rf433SecurityTrigger.class))
			.forEach((device) -> device.setState(Device.State.IDLE));
		
		// Reset rooms
		view.cardMap.getMap().getComponents(Room.class, (room) -> true)
			.forEach((room) -> {
				room.setBorderColor(null);
				room.setBackground(null);
			});
	}
	
	// TODO Déplacer dansmodule security ?
	@BindOnEvent(OnSecurityLevelChanged.class)
	@GuiTask
	public void onSecurityLevelChanged(int level, String label) {
		// Highlight rooms
		List<Room> target = null;
		Color color = null;
		if (level == 1) {
			target = view.cardMap.getMap().getComponents(Room.class,
					(room) -> "protected".equals(room.getCategory()));
			color = Color.YELLOW;
		}
		else if (level == 2) {
			target = view.cardMap.getMap().getComponents(Room.class, (room) -> true);
			color = Color.ORANGE;
		}
		if (target != null) {
			final Color colorCopy = color;
			target.forEach((room) -> room.setBorderColor(colorCopy));
		}
		view.cardMap.repaint();
	}
	
}

package fr.evolya.domokit.gui.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.reflect.Method;

import javax.swing.JPanel;

import fr.evolya.domokit.gui.map.MapPanel.MapListener;
import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.gui.map.simple.IMapContainer;
import fr.evolya.javatoolkit.code.utils.ReflectionUtils;
import fr.evolya.javatoolkit.events.basic.Dispatcher1;
import fr.evolya.javatoolkit.events.basic.Listenable1;

public class MapPanel extends JPanel 
	implements MouseListener, MouseMotionListener, Listenable1<MapListener, String, Point> {

	private static final long serialVersionUID = -1L;

	private IMap map;

	private Point slide;

	private boolean readonly = false;
	
	private Dispatcher1<MapListener, String, Point> events;
	
	private String displayMouseXY = null;

	/**
	 * Create the panel.
	 */
	public MapPanel() {
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
		events = new Dispatcher1<MapListener, String, Point>() {
			protected Method mapMethod(String methodName) {
				return ReflectionUtils.getMethodMatching(MapListener.class, methodName);
			}
		};
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (map != null) map.paint(g);
		if (displayMouseXY != null) {
			g.setColor(Color.ORANGE);
			g.drawString(displayMouseXY, 3, getHeight() - 3);
		}
	}

	public MapPanel setMap(IMap map) {
		this.map = map;
		this.map.setParentPanel(this);
		return this;
	}
	
	public IMap getMap() {
		return this.map;
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (readonly) return;
		if (e.getClickCount() == 1 && map != null) {
			notifyEvent("onClick", e.getPoint());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		slide = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		slide = null;
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

	@Override
	public void mouseDragged(MouseEvent e) {
		if (readonly) return;
		if (slide == null) return;
		notifyEvent("onSlide", slide, e.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		if (readonly ||displayMouseXY == null) return;
		
		displayMouseXY = "X: " + e.getX() + " Y: " + e.getY();
		
		IAbsolutePositionningComponent component = getMapComponentAt(e.getX(), e.getY());
		
		if (component != null) {
			displayMouseXY += " / " + component.getIdentifier();
			if (component instanceof IMapContainer) {
				Rectangle roomBounds = map.getComponentBounds(component);
				IAbsolutePositionningComponent child = ((IMapContainer) component)
						.getMapComponentAt(e.getX() - roomBounds.x, e.getY() - roomBounds.y);
				if (child != null && child instanceof Device) {
					displayMouseXY += " / " + child.getClass().getSimpleName()
							+ " " + child.getIdentifier();
				}
			}
		}
		
		repaint();
		
	}

	public void setReadonly(boolean value) {
		this.readonly  = value;
	}
	
	public boolean isReadonly() {
		return this.readonly;
	}
	
	public IAbsolutePositionningComponent getMapComponentAt(int x, int y) {
		return map.getComponentAt(x, y);
	}
	
	@Override
	public boolean addListener(MapListener listener) {
		return events.addListener(listener);
	}
	
	@Override
	public boolean removeListener(MapListener listener) {
		return events.removeListener(listener);
	}
	
	@Override
	public void notifyEvent(String event, Point... args) {
		events.notifyEvent(event, args);
	}
	
	public boolean isEnableDisplayMouseXY() {
		return displayMouseXY != null;
	}

	public void setEnableDisplayMouseXY(boolean enable) {
		if (enable && displayMouseXY != null) return;
		if (!enable && displayMouseXY == null) return;
		displayMouseXY = enable ? "" : null;
	}

	public static interface MapListener {
		void onClick(Point point);
		void onSlide(Point from, Point to);
	}
	
}

package fr.evolya.domokit.gui.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.javatoolkit.code.annotations.BadQualityCode;

public class MapPanel extends JPanel implements MouseListener, MouseMotionListener {

	private static final long serialVersionUID = -1L;

	private IMap map;

	private Point slide;

	/**
	 * Create the panel.
	 */
	public MapPanel() {
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (map != null) map.paint(g);
	}

	public void setMap(IMap map) {
		this.map = map;
		this.map.setParentPanel(this);
	}
	
	public IMap getMap() {
		return this.map;
	}

	@BadQualityCode
	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 1 && map != null) {
			System.out.println("Click on " + e.getX() + "x" + e.getY() + " = "
					+ map.getComponentAt(e.getX(), e.getY()));
			map.getComponentAt(e.getX(), e.getY()).setBackground(Color.RED);
			repaint();
			SwingUtilities.invokeLater(() -> {
				try {
					Thread.sleep(60);
				} catch (InterruptedException e1) { }
				map.getComponentAt(e.getX(), e.getY()).setBackground(null);
				repaint();
			});
			
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
		if (slide == null) return;
		System.out.println("Slide from " + slide + " to " + e.getPoint());
	}

	@Override
	public void mouseMoved(MouseEvent e) { }

}

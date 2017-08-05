package fr.evolya.domokit.gui.map;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.evolya.domokit.gui.map.iface.IMap;

public class MapPanel extends JPanel {

	private static final long serialVersionUID = -5755407202154464006L;

	private IMap map;

	/**
	 * Create the panel.
	 */
	public MapPanel() {
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if (map != null) map.paint(g, this);
	}

	public void setMap(IMap map) {
		this.map = map;
	}
	
	public IMap getMap() {
		return this.map;
	}

}

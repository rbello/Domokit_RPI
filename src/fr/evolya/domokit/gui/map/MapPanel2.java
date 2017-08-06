package fr.evolya.domokit.gui.map;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JPanel;

import fr.evolya.domokit.gui.map.iface.IAbsolutePositionningComponent;
import fr.evolya.domokit.gui.map.iface.IMap;

public class MapPanel2 extends JPanel {

	private static final long serialVersionUID = 1L;
	
	private int cols = 17;
	private int rows = 10;

	private IMap map;
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		//paintBackground(g);
	}
	
	public void paintBackground(Graphics graphic) {
		int step = getWidth() / cols;
		
		graphic.setColor(getForeground());
		
		// Columns
		for (int x = 0; x < cols; x++) {
			graphic.drawLine(x * step, 0, x * step, getHeight());
		}
		graphic.drawLine(getWidth() - 1, 0, getWidth() - 1, getHeight());
		// Rows
		step = getHeight() / rows;
		for (int y = 0; y <= rows; y++) {
			graphic.drawLine(0, y * step, getWidth(), y * step);
		}
	}

	public void setMap(IMap map) {
		this.map = map;
		// Fetch existing components
		map.getComponents(IAbsolutePositionningComponent.class, (c) -> {
			Component graphic = c.getGraphic();
			graphic.setBounds(c.getBounds());
			graphic.setBackground(Color.red);
			add(c.getGraphic());
		});
	}

}

package fr.evolya.domokit.gui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import fr.evolya.javatoolkit.code.funcint.Action;

public class PanelCountDown extends JPanel implements Runnable {
	
	private static final long serialVersionUID = -6365318050958238054L;

	public final JLabel label;
	public final JLabel countdown;
	public final JButton cancelButton;
	
	private Color colorNormal;
	private Color colorHighlight;

	private float timeout;
	private Action<Boolean> handler;
	private Thread thread;

	private int threshold = 5;
	private boolean toggle = true;
	
	public PanelCountDown() {
		
		countdown = new JLabel("00:00");
		countdown.setFont(new Font("Tahoma", Font.PLAIN, 50));
		countdown.setHorizontalAlignment(SwingConstants.CENTER);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		label = new JLabel("New label");
		label.setFont(new Font("Tahoma", Font.ITALIC, 20));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addComponent(countdown, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addComponent(cancelButton))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(35)
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 33, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(countdown, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
					.addGap(18)
					.addComponent(cancelButton)
					.addContainerGap(61, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		colorNormal = label.getForeground();
		colorHighlight = Color.RED;
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentHidden(ComponentEvent e) {
				if (thread == null) return;
				thread.interrupt();
				thread = null;
			}
			@Override
			public void componentShown(ComponentEvent e) {
				thread = new Thread(PanelCountDown.this);
				thread.start();
			}
		});
		
		cancelButton.addActionListener((e) -> {
			if (this.handler != null)
				this.handler.call(true);
		});
	}

	public void setTimeout(int delaySecs) {
		this.timeout = delaySecs;
		updateTime();
	}

	public void updateTime() {
		if (!EventQueue.isDispatchThread()) {
			EventQueue.invokeLater(() -> updateTime());
			return;
		}
		int min = 0;
		int delaySecs = (int) timeout;
		while (delaySecs > 60) {
			delaySecs -= 60;
			min += 1;
		}
		String padding = "";
		if (delaySecs < 10) padding = "0";
		countdown.setText(min + ":" + padding + delaySecs);
		if (delaySecs < threshold ) {
			toggle = !toggle;
			countdown.setForeground(toggle ? colorNormal : colorHighlight);
		}
	}

	public void setMessage(String msg) {
		if (!EventQueue.isDispatchThread()) {
			EventQueue.invokeLater(() -> setMessage(msg));
			return;
		}
		label.setText(msg == null ? "" : msg);
	}

	public void setHandler(Action<Boolean> callback) {
		this.handler = callback;
	}

	@Override
	public void run() {
		while (timeout > 0) {
			try {
				Thread.sleep(500);
			}
			catch (InterruptedException e) {
				Thread.interrupted();
				return;
			}
			updateTime();
			timeout -= 0.5;
		}
		this.handler.call(false);
	}

	public Color getColorNormal() {
		return colorNormal;
	}

	public void setColorNormal(Color colorNormal) {
		this.colorNormal = colorNormal;
	}

	public Color getColorHighlight() {
		return colorHighlight;
	}

	public void setColorHighlight(Color colorHighlight) {
		this.colorHighlight = colorHighlight;
	}
	
}

package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.evolya.domokit.SecurityMonitor;
import fr.evolya.domokit.SecurityMonitor.StateUnlocked;
import fr.evolya.domokit.SecurityMonitor.StateLocked;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.panels.PanelStatusStateManager.AlertState;
import fr.evolya.domokit.gui.panels.PanelStatusStateManager.WarningState;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class PanelStatusDebugView extends JFrame {

	private static final long serialVersionUID = -872889675706619230L;

	@Inject
	public App app;
	
	@Inject
	public SecurityMonitor monitor;
	
	@Inject
	public View480x320 view;
	
	@GuiTask
	public PanelStatusDebugView() {
		setTitle("State tester");
		setBounds(10, 340, 230, 200);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnShowAlert = new JButton("Show warning");
		btnShowAlert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setState(new WarningState("A warning", 5));
			}
		});
		btnShowAlert.setBackground(Color.YELLOW);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				view.panelStatus.clearAlertAndWarning();
			}
		});
		btnReset.setBackground(Color.BLUE);
		
		JButton btnShowError = new JButton("Show alert");
		btnShowError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setState(new AlertState("An alert", 5));
			}
		});
		btnShowError.setBackground(Color.RED);
		
		JButton btnState0 = new JButton("Unlocked");
		btnState0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setState(new StateUnlocked());
			}
		});
		
		JLabel lblSecurityLevels = new JLabel("Security levels :");
		
		JButton btnState1 = new JButton("Locked");
		btnState1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setState(new StateLocked());
			}
		});
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblSecurityLevels, GroupLayout.DEFAULT_SIZE, 202, Short.MAX_VALUE)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnShowAlert)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnShowError, GroupLayout.PREFERRED_SIZE, 97, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(btnState0)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnState1))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(59)
							.addComponent(btnReset)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnShowError, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnShowAlert, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(lblSecurityLevels)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnState0)
						.addComponent(btnState1))
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(btnReset)
					.addContainerGap(24, Short.MAX_VALUE))
		);
		getContentPane().setLayout(groupLayout);
	}
	
	@BindOnEvent(ApplicationStarted.class)
	@GuiTask
	public void run(App app) {
		setVisible(true);
		app.notify(GuiIsReady.class, this, app);
	}
}

package fr.evolya.domokit.gui.debug;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.evolya.domokit.ctrl.ModuleSecurity;
import fr.evolya.domokit.gui.View480x320;
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
	public ModuleSecurity monitor;
	
	@Inject
	public View480x320 view;
	
	@GuiTask
	public PanelStatusDebugView() {
		setTitle("State tester");
		setBounds(10, 340, 230, 235);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnShowAlert = new JButton("Show warning");
		btnShowAlert.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.showWarning("The warning's message");
			}
		});
		btnShowAlert.setBackground(Color.YELLOW);
		
		JButton btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.clearAlerts();
				monitor.clearWarning();
			}
		});
		btnReset.setBackground(Color.BLUE);
		
		JButton btnShowError = new JButton("Show alert");
		btnShowError.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (monitor.isAlertActive("An alert !!")) {
					monitor.clearAlert("An alert !!");
				}
				else {
					monitor.alert("An alert !!");
				}
			}
		});
		btnShowError.setBackground(Color.RED);
		
		JLabel lblSecurityLevels = new JLabel("Security levels :");

		JButton btnState0 = new JButton("0 - Unlocked");
		btnState0.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setSecurityLevel(0, "Unlocked");
			}
		});
		
		JButton btnState1 = new JButton("1 - Protected");
		btnState1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setSecurityLevel(1, "Protected");
			}
		});
		
		JButton btnState2 = new JButton("2 - Locked");
		btnState2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				monitor.setSecurityLevel(2, "Locked");
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
									.addComponent(btnState0, GroupLayout.PREFERRED_SIZE, 91, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnState1))))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(69)
							.addComponent(btnReset))
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(btnState2)))
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
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnState2)
					.addPreferredGap(ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
					.addComponent(btnReset)
					.addContainerGap())
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

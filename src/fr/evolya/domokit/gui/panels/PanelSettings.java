package fr.evolya.domokit.gui.panels;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Color;

public class PanelSettings extends JPanel {
	
	private static final long serialVersionUID = -5581114096639937976L;

	public final JButton buttonExit;
	public final JButton buttonReboot;

	public final JLabel fieldRf433Status;
	public final JLabel fieldLANStatus;
	public final JLabel fieldWANStatus;

	/**
	 * Create the panel.
	 */
	public PanelSettings() {
		
		JLabel lblNewLabel = new JLabel("Settings");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		buttonExit = new JButton("Exit");
		buttonExit.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonExit.setBackground(Color.RED);
		
		buttonReboot = new JButton("Reboot");
		buttonReboot.setFont(new Font("Tahoma", Font.PLAIN, 14));
		buttonReboot.setBackground(Color.RED);
		
		JLabel lblNewLabel_1 = new JLabel("RF433 network :");
		lblNewLabel_1.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		fieldRf433Status = new JLabel("Disconnected");
		fieldRf433Status.setForeground(Color.RED);
		fieldRf433Status.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblLocalNetwork = new JLabel("Local area network :");
		lblLocalNetwork.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		fieldLANStatus = new JLabel("Disconnected");
		fieldLANStatus.setForeground(Color.RED);
		fieldLANStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JLabel lblInternetConnection = new JLabel("Internet connection :");
		lblInternetConnection.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		fieldWANStatus = new JLabel("Disconnected");
		fieldWANStatus.setForeground(Color.RED);
		fieldWANStatus.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(lblNewLabel)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(10)
									.addGroup(groupLayout.createParallelGroup(Alignment.LEADING, false)
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblInternetConnection, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(fieldWANStatus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
											.addComponent(lblLocalNetwork, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(fieldLANStatus, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
										.addGroup(groupLayout.createSequentialGroup()
											.addComponent(lblNewLabel_1, GroupLayout.PREFERRED_SIZE, 156, GroupLayout.PREFERRED_SIZE)
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(fieldRf433Status, GroupLayout.PREFERRED_SIZE, 207, GroupLayout.PREFERRED_SIZE)))))
							.addGap(15))
						.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
							.addComponent(buttonExit)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonReboot)
							.addGap(55))))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblNewLabel_1)
						.addComponent(fieldRf433Status, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblLocalNetwork, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldLANStatus, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblInternetConnection, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE)
						.addComponent(fieldWANStatus, GroupLayout.PREFERRED_SIZE, 17, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonReboot, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonExit, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		setLayout(groupLayout);

	}
}

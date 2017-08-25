package fr.evolya.domokit.gui.panels;

import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class PanelNetwork extends JPanel {
	
	private static final long serialVersionUID = 572740134788430503L;

	/**
	 * Create the panel.
	 */
	public PanelNetwork() {
		
		JLabel lblNewLabel = new JLabel("Network");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addGap(15))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblNewLabel)
					.addContainerGap(269, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
}

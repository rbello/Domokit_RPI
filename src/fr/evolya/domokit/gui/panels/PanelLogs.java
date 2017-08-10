package fr.evolya.domokit.gui.panels;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JScrollPane;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.evolya.domokit.gui.View480x320;

import javax.swing.JTextArea;
import javax.swing.JButton;

public class PanelLogs extends JPanel {

	private static final long serialVersionUID = -6237570706391818914L;

	public JTextArea textArea;

	/**
	 * Create the panel.
	 */
	public PanelLogs() {
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		JScrollPane scrollPane = new JScrollPane();
		
		JButton btnNewButton = new JButton();
		btnNewButton.setIcon(new ImageIcon(View480x320.getImage("/16x16/008-equal.png")));
		btnNewButton.addActionListener(e -> textArea.setText(""));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblLogs)
							.addPreferredGap(ComponentPlacement.RELATED, 359, Short.MAX_VALUE)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(lblLogs)
						.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 18, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 254, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
		Font font = new Font("Consolas", Font.PLAIN, 12);
		textArea.setFont(font);
		setLayout(groupLayout);

	}
}

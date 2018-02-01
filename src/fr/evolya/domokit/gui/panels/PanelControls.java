package fr.evolya.domokit.gui.panels;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import fr.evolya.domokit.ctrl.ModuleRf433;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.icons.Icons.Size;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.code.annotations.Inject;

public class PanelControls extends JPanel {
	
	private static final long serialVersionUID = 572740134788430503L;

	@Inject public ModuleRf433 rf433;
	
	@Inject public View480x320 view;
	
	/**
	 * Create the panel.
	 */
	public PanelControls() {
		
		JButton btnNewButton = new JButton("Light ON");
		btnNewButton.setIcon(Icons.EMPTY.getIcon(Size.SIZE24X24));
		btnNewButton.addActionListener((e) -> {
			boolean enabled = btnNewButton.getText().equals("Light OFF");
			rf433.send("Light", !enabled, (error) -> {
				if (error == null) {
					SwingUtilities.invokeLater(() -> {
						btnNewButton.setText("Light " + (enabled ? "ON" : "OFF"));
						btnNewButton.setIcon((enabled ? Icons.EMPTY : Icons.SUN).getIcon(Size.SIZE24X24));
					});
				}
				else {
					view.appendLog("Unable to send command to Light: " + error, PanelLogs.ALERT);
				}
			});
			
		});
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(298, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(233, Short.MAX_VALUE))
		);
		setLayout(groupLayout);

	}
}

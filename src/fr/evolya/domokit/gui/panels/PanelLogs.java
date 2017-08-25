package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.awt.Font;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.icons.Icons.Size;

public class PanelLogs extends JPanel {

	private static final long serialVersionUID = -6237570706391818914L;

	public static final int SUCCESS = 3;
	public static final int ALERT = 2;
	public static final int WARNING = 1;
	public static final int NORMAL = 0;

	public final JTextPane textArea;
	public final JButton buttonRAZ;
	
	public final Style styleNormal;
	public final Style styleWarning;
	public final Style styleAlert;
	public final Style styleSuccess;

	private StyledDocument doc;


	/**
	 * Create the panel.
	 */
	public PanelLogs() {
		
		JLabel lblLogs = new JLabel("Logs");
		lblLogs.setFont(new Font("Tahoma", Font.BOLD, 16));
		
		JScrollPane scrollPane = new JScrollPane();
		
		buttonRAZ = new JButton();
		buttonRAZ.setIcon(Icons.EQUALS.getIcon(Size.SIZE16X16));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 430, Short.MAX_VALUE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblLogs)
							.addPreferredGap(ComponentPlacement.RELATED, 355, Short.MAX_VALUE)
							.addComponent(buttonRAZ, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(lblLogs, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(buttonRAZ, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 247, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		textArea = new JTextPane();
		scrollPane.setViewportView(textArea);
		Font font = new Font("Consolas", Font.PLAIN, 12);
		textArea.setFont(font);
		setLayout(groupLayout);

		doc = textArea.getStyledDocument();
		styleNormal = textArea.addStyle("normal", null);
		StyleConstants.setForeground(styleNormal, buttonRAZ.getForeground());
		styleWarning = textArea.addStyle("warning", null);
		StyleConstants.setForeground(styleWarning, Color.ORANGE);
		styleAlert = textArea.addStyle("alert", null);
		StyleConstants.setForeground(styleAlert, Color.RED);
		styleSuccess = textArea.addStyle("success", null);
		StyleConstants.setForeground(styleSuccess, Color.GREEN);
		
		// RAZ
		buttonRAZ.addActionListener(e -> textArea.setText(""));
	}

	public void append(String string, int level) {
		Style style = null;
		switch (level) {
			case WARNING: style = styleWarning; break;
			case ALERT: style = styleAlert; break;
			case SUCCESS: style = styleSuccess; break;
			default: style = styleNormal; break;
		}
		try {
			doc.insertString(doc.getLength(), string, style);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

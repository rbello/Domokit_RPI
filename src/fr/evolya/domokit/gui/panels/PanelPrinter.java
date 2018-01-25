package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;

import fr.evolya.javatoolkit.code.KeyValue;
import fr.evolya.javatoolkit.code.funcint.Callback;
import fr.evolya.javatoolkit.iot.ImageScanner;

public class PanelPrinter extends JPanel {

	private static final long serialVersionUID = 7514787488936339595L;

	private KeyValue<String, String> scanner = null;

	private JToggleButton buttonModeColor;

	private JToggleButton buttonModeBW;

	private JToggleButton buttonModeGray;

	private JComboBox<String> resolutionComboBox;

	private JComboBox<String> fileEncodingComboBox;

	private String sharedFolder = "/home/shuttle/Bureau/tmpscan/";

	/**
	 * Create the panel.
	 */
	public PanelPrinter() {
		
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				if (scanner == null) {
					ImageScanner.findFirstScanner(
							new Callback<KeyValue<String, String>, String>() {
						public void onSuccess(KeyValue<String, String> result) {
							scanner = result;
						}
						public void onFailure(String error) {
							// TODO Auto-generated method stub
							System.out.println("Error findFirstScanner(): " + error);
						}
					});
				}
			}
		});
		
		JButton btnScan = new JButton("Scan");
		btnScan.setBackground(Color.GREEN);
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Lock sur le scanner
				if (scanner != null) {
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
					
					String colorMode = ImageScanner.MODE_COLOR;
					if (buttonModeGray.isSelected()) colorMode = ImageScanner.MODE_GRAYSCALE;
					if (buttonModeBW.isSelected()) colorMode = ImageScanner.MODE_BW;
					int dpi = new Integer(("" + resolutionComboBox.getSelectedItem()).substring(0, 3));
					String format = fileEncodingComboBox.getSelectedItem() + "";
					String output = "shared-folder";
					String path = sharedFolder + "Scan_" + sdf.format(new Date()) + "." + format.toLowerCase();

					ImageScanner.scan(
							new File(path), // output file
							scanner.getKey(), // scanner id
							colorMode, // color mode
							dpi, // resolution
							format, // file encoding format
							// callback
							new Callback<File, String>() {
								public void onSuccess(File file) {
									// TODO Auto-generated method stub
									System.out.println("Write to " + output + ":" + file);
									
									
									
									
								}
								public void onFailure(String error) {
									// TODO Auto-generated method stub
									System.out.println("Error scan(): " + error);
								}
							});
				}
			}
		});
		
		// Title
		JLabel lblScanner = new JLabel("Scanner");
		lblScanner.setFont(new Font("Dialog", Font.BOLD, 16));
		
		// Color mode selector
		buttonModeColor = new JToggleButton("Color");
		buttonModeColor.setSelected(true);
		buttonModeGray = new JToggleButton("Gray");
		buttonModeBW = new JToggleButton("B&W");
		ButtonGroup colorModeBtnGroupe = new ButtonGroup();
		colorModeBtnGroupe.add(buttonModeColor);
		colorModeBtnGroupe.add(buttonModeGray);
		colorModeBtnGroupe.add(buttonModeBW);

		// Resolution
		resolutionComboBox = new JComboBox<String>();
		resolutionComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {
				"100 dpi", "150 dpi", "200 dpi", "300 dpi", "400 dpi", "600 dpi", "800 dpi"}));
		
		fileEncodingComboBox = new JComboBox<String>();
		fileEncodingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {ImageScanner.FILE_TIFF}));
		
		JLabel lblTarget = new JLabel("Target :");
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setFont(new Font("Dialog", Font.BOLD, 14));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblScanner)
						.addComponent(lblTarget)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(buttonModeColor, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonModeGray)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonModeBW, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(resolutionComboBox, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fileEncodingComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnScan)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblScanner)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonModeGray, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
						.addComponent(buttonModeColor, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
						.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
							.addComponent(buttonModeBW, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
							.addComponent(resolutionComboBox, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
							.addComponent(fileEncodingComboBox, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(lblTarget)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
						.addComponent(btnScan, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("Network", null, panel, null);
		
		JRadioButton rdbtnSharedFolder = new JRadioButton("Shared folder");
		JLabel lblNewLabel = new JLabel("Located at: \\\\PRINTER\\\\Scan");
		
		if (sharedFolder == null) {
			rdbtnSharedFolder.setEnabled(false);
			lblNewLabel.setEnabled(false);
		}
		else {
			rdbtnSharedFolder.setSelected(true);
		}
		
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 10));
		GroupLayout gl_panel = new GroupLayout(panel);
		gl_panel.setHorizontalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panel.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_panel.createSequentialGroup()
							.addGap(21)
							.addComponent(lblNewLabel))
						.addComponent(rdbtnSharedFolder))
					.addContainerGap(164, Short.MAX_VALUE))
		);
		gl_panel.setVerticalGroup(
			gl_panel.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panel.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnSharedFolder)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		panel.setLayout(gl_panel);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Email", null, panel_1, null);
		setLayout(groupLayout);
		
	}
}

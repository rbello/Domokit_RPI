package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;

import fr.evolya.domokit.ctrl.Email;
import fr.evolya.domokit.gui.View480x320;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.config.AppConfiguration;
import fr.evolya.javatoolkit.app.event.ApplicationConfigLoaded;
import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.ConfigDeclare;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.code.funcint.Callback;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.iot.ImageScanner;
import fr.evolya.javatoolkit.iot.ImageScanner.DetectionCallback;

@ConfigDeclare("Scanner.SmtpHost")
@ConfigDeclare("Scanner.SmtpPort=465")
@ConfigDeclare("Scanner.SmtpUser")
@ConfigDeclare("Scanner.SmtpPassword")
@ConfigDeclare("Scanner.SmtpSender")
@ConfigDeclare("Scanner.SharedFolder=./")
@ConfigDeclare("Scanner.BookmarkEmails")

public class PanelPrinter extends JPanel {
	
	public static final Logger LOGGER = Logs.getLogger("Scanner");

	private static final long serialVersionUID = 7514787488936339595L;

	private ImageScanner scanner = null;
	private boolean scannerDetection = false;

	@Inject public View480x320 view;
	
	@Inject public AppConfiguration config; 

	private JToggleButton buttonModeColor;
	private JToggleButton buttonModeBW;
	private JToggleButton buttonModeGray;
	private JComboBox<String> resolutionComboBox;
	private JComboBox<String> fileEncodingComboBox;
	private JTabbedPane destinationTabPane;
	private JButton btnScan;
	private JLabel labelTargetEmail;
	private JList<String> bookmarkList;

	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");

	/**
	 * Create the panel.
	 */
	public PanelPrinter() {

		// When panel is shown, run scanner detection if needed
		addComponentListener(new ComponentAdapter() {
			public void componentShown(ComponentEvent e) {
				if (scanner != null || scannerDetection) return;
				scannerDetection();
			}
		});

		// Run the scan
		btnScan = new JButton("Scan");
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				startScanner();
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
		
		// File format
		fileEncodingComboBox = new JComboBox<String>();
		fileEncodingComboBox.setModel(new DefaultComboBoxModel<String>(new String[] {ImageScanner.FILE_TIFF}));
		
		// Target label
		destinationTabPane = new JTabbedPane(JTabbedPane.TOP);
		destinationTabPane.setFont(new Font("Dialog", Font.BOLD, 16));
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(lblScanner)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(destinationTabPane, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnScan))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(buttonModeColor, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonModeGray, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(buttonModeBW, GroupLayout.PREFERRED_SIZE, 59, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(resolutionComboBox, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(fileEncodingComboBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(lblScanner)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(buttonModeGray, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
						.addComponent(buttonModeBW, GroupLayout.DEFAULT_SIZE, 46, Short.MAX_VALUE)
						.addComponent(resolutionComboBox, GroupLayout.DEFAULT_SIZE, 47, Short.MAX_VALUE)
						.addComponent(buttonModeColor, GroupLayout.PREFERRED_SIZE, 46, GroupLayout.PREFERRED_SIZE)
						.addComponent(fileEncodingComboBox, GroupLayout.DEFAULT_SIZE, 49, Short.MAX_VALUE))
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(33)
							.addComponent(btnScan, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
							.addGap(65))
						.addGroup(groupLayout.createSequentialGroup()
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(destinationTabPane, GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
							.addContainerGap())))
		);
		
		JPanel tabPanelNetwork = new JPanel();
		destinationTabPane.addTab(" Save ", null, tabPanelNetwork, null);
		
		JRadioButton rdbtnSharedFolder = new JRadioButton("Shared folder");
		JLabel lblNewLabel = new JLabel("Located at: \\\\PRINTER\\\\Scan");
		
		String sharedFolder = null;
		
		if (sharedFolder == null) {
			rdbtnSharedFolder.setEnabled(false);
			lblNewLabel.setEnabled(false);
		}
		else {
			rdbtnSharedFolder.setSelected(true);
		}
		
		lblNewLabel.setFont(new Font("Dialog", Font.BOLD, 10));
		GroupLayout gl_tabPanelNetwork = new GroupLayout(tabPanelNetwork);
		gl_tabPanelNetwork.setHorizontalGroup(
			gl_tabPanelNetwork.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanelNetwork.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabPanelNetwork.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_tabPanelNetwork.createSequentialGroup()
							.addGap(21)
							.addComponent(lblNewLabel))
						.addComponent(rdbtnSharedFolder))
					.addContainerGap(164, Short.MAX_VALUE))
		);
		gl_tabPanelNetwork.setVerticalGroup(
			gl_tabPanelNetwork.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanelNetwork.createSequentialGroup()
					.addContainerGap()
					.addComponent(rdbtnSharedFolder)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(lblNewLabel)
					.addContainerGap(100, Short.MAX_VALUE))
		);
		tabPanelNetwork.setLayout(gl_tabPanelNetwork);
		
		JPanel tabPanelSendToMail = new JPanel();
		destinationTabPane.addTab(" Send by email ", null, tabPanelSendToMail, null);
		
		labelTargetEmail = new JLabel("To: ");
		labelTargetEmail.setFont(new Font("Dialog", Font.BOLD, 13));
		
		JButton btnNewButton = new JButton("+");
		btnNewButton.addActionListener((e) -> {
			view.showKeyboardCard(str -> {
				view.showCard("Printer");
				bookmarkList.clearSelection();
				// TODO Vérifier adresse
				labelTargetEmail.setText("To: " + str);
			});
		});
		
		JScrollPane scrollPane = new JScrollPane();
		GroupLayout gl_tabPanelSendToMail = new GroupLayout(tabPanelSendToMail);
		gl_tabPanelSendToMail.setHorizontalGroup(
			gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_tabPanelSendToMail.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabPanelSendToMail.createParallelGroup(Alignment.TRAILING)
						.addComponent(scrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
						.addGroup(gl_tabPanelSendToMail.createSequentialGroup()
							.addComponent(labelTargetEmail, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_tabPanelSendToMail.setVerticalGroup(
			gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanelSendToMail.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabPanelSendToMail.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnNewButton)
						.addComponent(labelTargetEmail, GroupLayout.PREFERRED_SIZE, 21, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		bookmarkList = new JList<String>();
		bookmarkList.setFont(new Font("Dialog", Font.BOLD, 13));
		bookmarkList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		bookmarkList.setModel(new DefaultListModel<String>());
		bookmarkList.addListSelectionListener((e) -> {
			if (e.getValueIsAdjusting()) return;
			labelTargetEmail.setText("To: " + bookmarkList.getSelectedValue());
		});
		scrollPane.setViewportView(bookmarkList);
		tabPanelSendToMail.setLayout(gl_tabPanelSendToMail);
		setLayout(groupLayout);
		
		btnScan.setEnabled(false);
		
	}
	
	@Deprecated
	@Override
	public void setEnabled(boolean enabled) {
		buttonModeColor.setEnabled(enabled);
		buttonModeGray.setEnabled(enabled);
		buttonModeBW.setEnabled(enabled);
		resolutionComboBox.setEnabled(enabled);
		fileEncodingComboBox.setEnabled(enabled);
		destinationTabPane.setEnabled(enabled);
		btnScan.setEnabled(enabled);
//		super.setEnabled(enabled);
	}
	
	@BindOnEvent(ApplicationConfigLoaded.class)
	@GuiTask
	public void onApplicationConfigLoaded(App app, AppConfiguration config) {
		// Bookmark emails
		String[] bookmark = config.getProperty("Scanner.BookmarkEmails").split(";");
		for (String address : bookmark) {
			((DefaultListModel<String>)bookmarkList.getModel()).addElement(address);
		}
	}
	
	private void scannerDetection() {
		// Flag current detection
		scannerDetection = true;
		// Find scanner
		ImageScanner.detectFirstScanner(new DetectionCallback() {
			public void onSuccess(ImageScanner scanner) {
				scannerDetection = false;
				PanelPrinter.this.scanner = scanner;
				LOGGER.log(Logs.INFO, "Scanner found: " + scanner);
				SwingUtilities.invokeLater(() -> {
					btnScan.setBackground(Color.GREEN);
					btnScan.setEnabled(true);
				});
			}
			public void onFailure(String error) {
				scannerDetection = false;
				LOGGER.log(Logs.WARNING, "No scanner found (" + error + ")");
				SwingUtilities.invokeLater(() -> {
					btnScan.setBackground(Color.RED);
					btnScan.setEnabled(false);
				});
			}
		});
	}
	
	private void startScanner() {
		// Scanner is not ready
		if (scanner == null) return;
			
		// Pickup configuration
		String colorMode = ImageScanner.MODE_COLOR;
		if (buttonModeGray.isSelected()) colorMode = ImageScanner.MODE_GRAYSCALE;
		if (buttonModeBW.isSelected()) colorMode = ImageScanner.MODE_BW;
		final String colorMode2 = colorMode;
		int dpi = new Integer(("" + resolutionComboBox.getSelectedItem()).substring(0, 3));
		String format = (fileEncodingComboBox.getSelectedItem() + "").toLowerCase();
		String output = destinationTabPane.getSelectedIndex() == 0 ? "shared-folder" : "email";
		String sharedFolder = "./"; // TODO
		String path = sharedFolder + "Scan_" + sdf.format(new Date()) + "." + format.toLowerCase();

		// Log
		LOGGER.log(Logs.DEBUG, String.format("Run scan task (mode=%s; dpi=%s; format=%s; output=%s)",
				colorMode, dpi, format, output));
		
		// Update GUI state
		btnScan.setBackground(Color.ORANGE);
		btnScan.setEnabled(false);
//		lblTarget.setText("Scanning...");
		
		String email = labelTargetEmail.getText();
		
		// Run scanner
		ImageScanner.scan(
				new File(path), // output file
				scanner.getKey(), // scanner id
				colorMode, // color mode
				dpi, // resolution
				format, // file encoding format
				// callback
				new Callback<File, String>() {
					public void onSuccess(File outputFile) {
						
						SwingUtilities.invokeLater(() -> {
							btnScan.setBackground(Color.GREEN);
							btnScan.setEnabled(true);
//							lblTarget.setText("Destination:");
						});
						
//						File jpegFile = new File(outputFile.getAbsolutePath() + ".jpeg");
//						
//						try {
//							convertToJpeg(output, jpegFile);
//						}
//						catch (Exception ex) {
//							System.err.println("Unable to convert TIFF to JPEG");
//						}
						
						///- -----------------
						
						if (!"email".equals(output)) return;
						
						if (labelTargetEmail.getText().length() < 5) return;
						
						String to = email.substring(4);
						
						LOGGER.log(Logs.DEBUG, String.format("Send scan file to: %s", to));
						
//						lblTarget.setText("Send email to " + to + " ...");

						Email.sendEmailAsynch(
								config.getProperty("Scanner.SmtpHost"), // SMTP Host
								(int)config.getPropertyInt("Scanner.SmtpPort"), // SMTP Port
								config.getProperty("Scanner.SmtpUser"), // Login
								config.getProperty("Scanner.SmtpPassword"), // Password
								"Scan from " + scanner.getValue(), // Message title
								config.getProperty("Scanner.SmtpSender"), // From
								to, // To
								// Message contents
								String.format("Please find attached file.\n\nDate: %s\nColor mode: %s\nResolution: %s dpi\nFormat: %s\n", sdf.format(new Date()), colorMode2, dpi, format),
								LOGGER.isLoggable(Logs.DEBUG), // Debug mode
								outputFile, // Attachment
								"ScanImage-" + new Random().nextInt(99999), // Attachment name
								// Handler
								(error) -> {
									LOGGER.log(Logs.DEBUG, "Scan result: " + (error == null ? "SUCCESS" : 
										"FAILURE " + error.getMessage()));
								}
						);
				
						
					}
					
					public void onFailure(String error) {
						// TODO Auto-generated method stub
						System.out.println("Error scan(): " + error);
						SwingUtilities.invokeLater(() -> {
							btnScan.setBackground(Color.RED);
							btnScan.setEnabled(true);
//							lblTarget.setText("Failure:");
						});
					}
				});
	}
	
	public static void convertToJpeg(String source, File destination) throws IOException {
		convertToJpeg(source, destination, true);
	}
	
	public static void convertToJpeg(String source, File destination, boolean debug) throws IOException {
		if (debug) {
			System.out.println("Convert image to JPEG");
			System.out.println("  from " + source + " (" + source.length() + ")");
			System.out.println("  to   " + destination);
		}
		
		try (InputStream is = new FileInputStream(source)) {
			if (debug) System.out.println("> open source OK");
		    BufferedImage image = ImageIO.read(is);
		    if (debug) System.out.println("> read source OK");
		    try (OutputStream os = new FileOutputStream(destination)) {
		    	if (debug) System.out.println("> open destination OK");
		        ImageIO.write(image, "jpg", os);
		        if (debug) System.out.println("> write destination OK");
		    }
		    catch (IOException ex) {
		    	if (debug) ex.printStackTrace();
		    	throw ex;
		    }
		}
		catch (IOException ex) {
			if (debug) ex.printStackTrace();
		    throw ex;
		}
		
	}

}

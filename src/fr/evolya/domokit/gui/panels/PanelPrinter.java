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
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Logger;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.imageio.ImageIO;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JToggleButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import fr.evolya.javatoolkit.code.Logs;
import fr.evolya.javatoolkit.code.annotations.AsynchOperation;
import fr.evolya.javatoolkit.code.funcint.Action;
import fr.evolya.javatoolkit.code.funcint.Callback;
import fr.evolya.javatoolkit.iot.ImageScanner;
import fr.evolya.javatoolkit.iot.ImageScanner.DetectionCallback;

public class PanelPrinter extends JPanel {
	
	public static final Logger LOGGER = Logs.getLogger("Scanner");

	private static final long serialVersionUID = 7514787488936339595L;

	private ImageScanner scanner = null;
	private boolean scannerDetection = false;

	private JToggleButton buttonModeColor;
	private JToggleButton buttonModeBW;
	private JToggleButton buttonModeGray;
	private JComboBox<String> resolutionComboBox;
	private JComboBox<String> fileEncodingComboBox;

	private String sharedFolder = "/home/shuttle/Bureau/tmpscan/";

	private JTabbedPane destinationTabPane;

	private JButton btnScan;

	/**
	 * Create the panel.
	 */
	public PanelPrinter() {
		
		// 
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				// Don't run scanner detection
				if (scanner != null || scannerDetection) return;
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
		});
		
		btnScan = new JButton("Scan");
		btnScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Lock sur le scanner
				if (scanner == null) return;
				
				btnScan.setBackground(Color.ORANGE);
				btnScan.setEnabled(false);
					
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HH-mm-ss");
				
				String colorMode = ImageScanner.MODE_COLOR;
				if (buttonModeGray.isSelected()) colorMode = ImageScanner.MODE_GRAYSCALE;
				if (buttonModeBW.isSelected()) colorMode = ImageScanner.MODE_BW;
				final String colorMode2 = colorMode;
				int dpi = new Integer(("" + resolutionComboBox.getSelectedItem()).substring(0, 3));
				String format = (fileEncodingComboBox.getSelectedItem() + "").toLowerCase();
				String output = destinationTabPane.getSelectedIndex() == 0 ? "shared-folder" : "email";
				String path = sharedFolder + "Scan_" + sdf.format(new Date()) + "." + format.toLowerCase();

				LOGGER.log(Logs.DEBUG, String.format("Run scan task (mode=%s; dpi=%s; format=%s; output=%s)",
						colorMode, dpi, format, output));
				
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
								});
								
								File jpegFile = new File(outputFile.getAbsolutePath() + ".jpeg");
								System.out.println("from " + outputFile + " (" + outputFile.length() + ")");
								System.out.println("to   " + jpegFile);
								
								try {
									try (InputStream is = new FileInputStream(outputFile)) {
									    BufferedImage image = ImageIO.read(is);
									    try (OutputStream os = new FileOutputStream(jpegFile)) {
									        ImageIO.write(image, "jpg", os);
									    } catch (Exception exp) {
									        exp.printStackTrace();
									    }
									} catch (Exception exp) {
									    exp.printStackTrace();
									}
								}
								catch (Exception ex) {
									System.err.println("Unable to convert TIFF to JPEG");
									ex.printStackTrace();
								}
								
								///- -----------------
								
								if (!"email".equals(output)) return;
								
								String to = "test@evolya.fr";
								
								LOGGER.log(Logs.DEBUG, String.format("Send scan file to: %s", to));
								
								sendEmailAsynch(
										"SSL0.OVH.NET", // SMTP Host
										465, // SMTP Port
										"username", // Login
										"password", // Password
										"Scan from " + scanner.getValue(), // Message title
										"contact@evolya.fr", // From
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
								});
							}
						});
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
		
		JLabel lblTarget = new JLabel("Destination :");
		
		destinationTabPane = new JTabbedPane(JTabbedPane.TOP);
		destinationTabPane.setFont(new Font("Dialog", Font.BOLD, 14));
		
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
							.addComponent(destinationTabPane, GroupLayout.PREFERRED_SIZE, 299, GroupLayout.PREFERRED_SIZE)
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
							.addComponent(destinationTabPane, GroupLayout.DEFAULT_SIZE, 177, Short.MAX_VALUE))
						.addComponent(btnScan, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		JPanel tabPanelNetwork = new JPanel();
		destinationTabPane.addTab("  Network  ", null, tabPanelNetwork, null);
		
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
		destinationTabPane.addTab("  Email  ", null, tabPanelSendToMail, null);
		
		JLabel labelTargetEmail = new JLabel("Email");
		
		JList list = new JList();
		
		JButton btnNewButton = new JButton("+");
		GroupLayout gl_tabPanelSendToMail = new GroupLayout(tabPanelSendToMail);
		gl_tabPanelSendToMail.setHorizontalGroup(
			gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanelSendToMail.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
						.addGroup(Alignment.TRAILING, gl_tabPanelSendToMail.createSequentialGroup()
							.addComponent(list, GroupLayout.DEFAULT_SIZE, 232, Short.MAX_VALUE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnNewButton, GroupLayout.PREFERRED_SIZE, 32, GroupLayout.PREFERRED_SIZE))
						.addComponent(labelTargetEmail, GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE))
					.addContainerGap())
		);
		gl_tabPanelSendToMail.setVerticalGroup(
			gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_tabPanelSendToMail.createSequentialGroup()
					.addContainerGap()
					.addComponent(labelTargetEmail)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_tabPanelSendToMail.createParallelGroup(Alignment.LEADING)
						.addComponent(btnNewButton)
						.addComponent(list, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))
					.addContainerGap())
		);
		tabPanelSendToMail.setLayout(gl_tabPanelSendToMail);
		setLayout(groupLayout);
		
		btnScan.setEnabled(false);
		
	}
	
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
	
	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, Action<MessagingException> callback) {
		sendEmailAsynch(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, false, callback);
	}
	
	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug, 
			Action<MessagingException> callback) {
		sendEmailAsynch(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, null, null, callback);
	}
	
	@AsynchOperation
	public static void sendEmailAsynch(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug,
			File attachment, String attachmentName, Action<MessagingException> callback) {
		
		new Thread(() -> {
			
			try {
				sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, attachment, attachmentName);
				callback.call(null); // Mean success 
			}
			catch (MessagingException ex) {
				callback.call(ex);
			}
			
		}).start();
		
	}
	
	public static boolean sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents) {
		try {
			sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, false);
			return true;
		}
		catch (MessagingException e) {
			return false;
		}
	}
	
	public static void sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug)
					throws MessagingException {
		sendEmail(smtpHost, smtpPort, username, password, msgTitle, from, to, msgContents, debug, null, null);
	}
	
	public static void sendEmail(String smtpHost, int smtpPort, String username, String password,
			String msgTitle, String from, String to, String msgContents, boolean debug,
			File attachment, String attachmentName) throws MessagingException {
		
		Properties props = new Properties();
	    
	    props.put("mail.smtp.auth", true);
	    props.put("mail.smtp.ehlo", true);
	    props.put("mail.smtp.ssl.enable", true);
	    props.put("mail.smtp.starttls.enable", true);
	    props.put("mail.smtp.host", smtpHost);
	    props.put("mail.smtp.port", smtpPort);
	    props.put("mail.smtp.connectiontimeout", "10000");
	    
	    Session session = Session.getInstance(props,
	            new javax.mail.Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(username, password);
	                }
	            });
	    
	    session.setDebug(debug);

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.setRecipients(Message.RecipientType.TO,
                InternetAddress.parse(to));
        message.setSubject(msgTitle);
        message.setText(msgContents);

        if (attachment != null) {
	        MimeBodyPart messageBodyPart = new MimeBodyPart();
	        Multipart multipart = new MimeMultipart();
	        messageBodyPart = new MimeBodyPart();
	        String file = attachment.getAbsolutePath();
	        DataSource source = new FileDataSource(file);
	        messageBodyPart.setDataHandler(new DataHandler(source));
	        messageBodyPart.setFileName(attachmentName);
	        multipart.addBodyPart(messageBodyPart);
	        message.setContent(multipart);
        }

        LOGGER.log(Logs.DEBUG, "Sending...");
        Transport.send(message);
        LOGGER.log(Logs.DEBUG, "Done!");

	}
}

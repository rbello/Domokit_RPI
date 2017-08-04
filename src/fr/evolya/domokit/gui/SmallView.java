package fr.evolya.domokit.gui;

import java.awt.CardLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.util.HashMap;
import java.util.Map;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.app.event.WindowCloseIntent;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import fr.evolya.javatoolkit.gui.swing.map.MapPanel;
import fr.evolya.javatoolkit.gui.swing.map.StatusPanel;
import fr.evolya.javatoolkit.gui.swing.map.StatusPanel.State;

public class SmallView extends JFrame {

	private static final long serialVersionUID = -6653570811059565525L;
	
	private Map<String, JPanel> cards = new HashMap<>();

	private StatusPanel panelStatus;
	private JPanel panelMain;
	private JPanel panelMenu;
	private JButton buttonClose;
	private JButton buttonSettings;


	private JButton buttonLogs;

	private JButton buttonMap;

	private JTextArea textArea;
	
	@GuiTask
	public SmallView() {
		super();
		
		if (!SwingUtilities.isEventDispatchThread()) {
			throw new UnsupportedOperationException("GUI creation not in EDT");
		}
		
		setBounds(0, 0, 480, 320);
		setUndecorated(true);
		getContentPane().setLayout(null);
		
		panelMenu = new JPanel();
		panelMenu.setBounds(416, 0, 64, 253);
		getContentPane().add(panelMenu);
		
		buttonClose = new JButton();
		buttonClose.setToolTipText("Close");
		buttonClose.setIcon(new ImageIcon(getImage("/16x16/046-closed.png")));
		
		buttonSettings = new JButton();
		buttonSettings.setToolTipText("Settings");
		buttonSettings.setIcon(new ImageIcon(getImage("/16x16/005-settings.png")));
		buttonSettings.addActionListener(e -> {
			showCard("Settings");
		});
		
		buttonLogs = new JButton();
		buttonLogs.setToolTipText("Logs");
		buttonLogs.setIcon(new ImageIcon(getImage("/16x16/009-message.png")));
		buttonLogs.addActionListener(e -> {
			showCard("Logs");
		});
		
		buttonMap = new JButton();
		buttonMap.setToolTipText("Map");
		buttonMap.setIcon(new ImageIcon(getImage("/16x16/036-placeholder.png")));
		buttonMap.addActionListener(e -> {
			showCard("Map");
		});
		
		GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
		gl_panelMenu.setHorizontalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonClose, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonMap, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonLogs, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonSettings, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelMenu.setVerticalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonClose, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonMap, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonLogs, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonSettings, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(44, Short.MAX_VALUE))
		);
		panelMenu.setLayout(gl_panelMenu);
		
		panelMain = new JPanel();
		panelMain.setBounds(3, 3, 410, 251);
		panelMain.setLayout(new CardLayout());
		getContentPane().add(panelMain);
		
		cards.put("Settings", new PanelSettings());
		cards.put("Logs", new PanelLogs());
		cards.put("Map", new MapPanel());
		
		panelMain.add(cards.get("Settings"), "Settings");
		panelMain.add(cards.get("Logs"), "Logs");
		panelMain.add(cards.get("Map"), "Map");
		
		textArea = ((PanelLogs)cards.get("Logs")).textArea;
		
		panelStatus = new StatusPanel();
		panelStatus.setBounds(3, 257, 474, 60);
		panelStatus.setCartoucheInfo("LEVEL");
		panelStatus.setCartoucheLevel(0);
		panelStatus.setMainText("Plan view");
		panelStatus.setInfoText("No alerts");
		panelStatus.setState(State.NORMAL);
		getContentPane().add(panelStatus);
	}
	
	@BindOnEvent(ApplicationBuilding.class)
	@GuiTask
	public void build(App app) {
		buttonClose.addActionListener(e -> 
			app.notify(WindowCloseIntent.class, app, this, e));
	}
	
	@BindOnEvent(ApplicationStarted.class)
	@GuiTask
	public void run(App app) {
		setVisible(true);
		app.notify(GuiIsReady.class, this, app);
	}
	
	@BindOnEvent(WindowCloseIntent.class)
	@GuiTask
	public void close(App app) {
		setVisible(false);
		dispose();
		app.stop();
	}

	public StatusPanel getPanelStatus() {
		return panelStatus;
	}

	public JPanel getPanelMain() {
		return panelMain;
	}

	public void showCard(String cardName) {
		CardLayout cl = (CardLayout)(panelMain.getLayout());
        cl.show(panelMain, cardName);
	}

	@SuppressWarnings("unchecked")
	public <T> T getPanelMain(String cardName, Class<T> type) {
		return (T) cards.get(cardName);
	}
	
	/**
	 * Permet de charger une image se trouvant dans le package de cette classe.
	 */
	public static Image getImage(String filename) {
		return Toolkit.getDefaultToolkit().getImage(
				SmallView.class.getResource("/fr/evolya/domokit/gui/icons" + filename));
	}

	public void appendLog(String msg) {
		SwingUtilities.invokeLater(() -> textArea.append(msg + "\n"));
	}
}

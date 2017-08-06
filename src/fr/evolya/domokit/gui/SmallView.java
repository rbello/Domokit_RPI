package fr.evolya.domokit.gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionListener;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import fr.evolya.domokit.SecurityMonitor;
import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationBuilding;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.app.event.WindowCloseIntent;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.code.funcint.Action;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;

public class SmallView extends JFrame {

	private static final long serialVersionUID = -6653570811059565525L;
	
	@Inject
	public App app;
	
	public final PanelStatus panelStatus;
	public final JPanel panelMain;
	public final JPanel panelMenu;

	public final JButton buttonLock;
	public final JButton buttonSettings;
	public final JButton buttonLogs;
	public final JButton buttonMap;

	public final MapPanel cardMap;
	public final PanelSettings cardSettings;
	public final PanelLogs cardLogs;
	public final PanelPin cardPin;
	public final PanelConfirmDialog cardConfirm;
	
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
		
		buttonLock = new JButton();
		buttonLock.setToolTipText("Lock");
		setButtonLockIcon(false);
		
		buttonSettings = new JButton();
		buttonSettings.setToolTipText("Settings");
		buttonSettings.setIcon(new ImageIcon(getImage("/24x24/047-settings.png")));
		buttonSettings.addActionListener(e -> {
			showCard("Settings");
		});
		
		buttonLogs = new JButton();
		buttonLogs.setToolTipText("Logs");
		buttonLogs.setIcon(new ImageIcon(getImage("/24x24/045-message.png")));
		buttonLogs.addActionListener(e -> {
			showCard("Logs");
		});
		
		buttonMap = new JButton();
		buttonMap.setToolTipText("Map");
		buttonMap.setIcon(new ImageIcon(getImage("/24x24/021-home.png")));
		buttonMap.addActionListener(e -> {
			showCard("Map");
		});
		
		GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
		gl_panelMenu.setHorizontalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonLock, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonMap, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonLogs, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonSettings, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
					.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		gl_panelMenu.setVerticalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addComponent(buttonLock, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
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
		
		panelMain.add(cardSettings = new PanelSettings(), "Settings");
		panelMain.add(cardLogs = new PanelLogs(), "Logs");
		panelMain.add(cardMap = new MapPanel(), "Map");
		panelMain.add(cardPin = new PanelPin(), "Pin");
		panelMain.add(cardConfirm = new PanelConfirmDialog(), "ConfirmDialog");
		
		panelStatus = new PanelStatus();
		panelStatus.setBounds(3, 257, 474, 60);
		panelStatus.setCartoucheInfo("LEVEL");
		getContentPane().add(panelStatus);
	}
	
	@BindOnEvent(ApplicationBuilding.class)
	@GuiTask
	public void build(App app) {
		cardSettings.buttonExit.addActionListener(e -> 
			app.notify(WindowCloseIntent.class, app, this, e));
		buttonLock.addActionListener(e -> {
			if (app.get(SecurityMonitor.class).isLocked()) {
				app.get(SecurityMonitor.class).unlock();
			}
			else {
				app.get(SecurityMonitor.class).lock();
			}
		});
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

	public void showCard(String cardName) {
		CardLayout cl = (CardLayout)(panelMain.getLayout());
        cl.show(panelMain, cardName);
	}
	
	public void showPinCard(String password, Action<Boolean> handler) {
        for (Component comp : panelMain.getComponents()) {
            if (comp.isVisible() == true && comp == cardPin) {
                return;
            }
        }
        cardPin.reset();
        cardPin.handler = (code) -> {
        	if (code == null) {
        		// Cancel
        		showDefaultCard();
        		app.get(SecurityMonitor.class).resetStatus();
        	}
        	else {
        		handler.call(code.equals(password));
        	}
        };
        showCard("Pin");
	}

	/**
	 * Permet de charger une image se trouvant dans le package de cette classe.
	 */
	public static Image getImage(String filename) {
		return Toolkit.getDefaultToolkit().getImage(
				SmallView.class.getResource("/fr/evolya/domokit/gui/icons" + filename));
	}

	public void appendLog(String msg) {
		SwingUtilities.invokeLater(() -> cardLogs.textArea.append(msg + "\n"));
	}

	public void setButtonLockIcon(boolean locked) {
		buttonLock.setIcon(new ImageIcon(getImage(
				locked ? "/24x24/003-play-button.png" : "/24x24/002-exclamation-button.png")));
	}

	public void showDefaultCard() {
		showCard("Map");
	}

	public void showConfirmDialogCard(String message, String[] answers, Action<String> callback) {
		showCard("ConfirmDialog");
		cardConfirm.textLabel.setText(message);
		cardConfirm.panelButtons.removeAll();
		ActionListener listener = (e) -> {
			callback.call(((JButton)e.getSource()).getText());
		};
		for (String text : answers) {
			JButton btn = new JButton();
			if (text.startsWith("*")) {
				text = text.substring(1);
				btn.setBackground(new Color(51, 153, 255));
			}
			btn.setText(text);
			btn.setFont(new Font("Tahoma", Font.PLAIN, 18));
			btn.addActionListener(listener);
			cardConfirm.panelButtons.add(btn);
		}
	}

}

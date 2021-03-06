package fr.evolya.domokit.gui;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingUtilities;

import fr.evolya.domokit.gui.icons.Icons;
import fr.evolya.domokit.gui.icons.Icons.Size;
import fr.evolya.domokit.gui.map.MapPanel;
import fr.evolya.domokit.gui.panels.PanelConfirmDialog;
import fr.evolya.domokit.gui.panels.PanelControls;
import fr.evolya.domokit.gui.panels.PanelCountDown;
import fr.evolya.domokit.gui.panels.PanelKeyboard;
import fr.evolya.domokit.gui.panels.PanelLogs;
import fr.evolya.domokit.gui.panels.PanelNetwork;
import fr.evolya.domokit.gui.panels.PanelPin;
import fr.evolya.domokit.gui.panels.PanelPrinter;
import fr.evolya.domokit.gui.panels.PanelSettings;
import fr.evolya.domokit.gui.panels.PanelStatus;
import fr.evolya.javatoolkit.code.annotations.DeepContainer;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.funcint.Action;

@DeepContainer
public class View480x320 extends JFrame {

	private static final long serialVersionUID = -6653570811059565525L;
	
	public final PanelStatus panelStatus;
	public final JPanel panelMain;
	public final JPanel panelMenu;

	public final JButton buttonLock;
	public final JButton buttonSettings;
	public final JButton buttonLogs;
	public final JButton buttonMap;
	public final JButton buttonPrintScan;

	public final MapPanel cardMap;
	public final PanelSettings cardSettings;
	public final PanelLogs cardLogs;
	public final PanelPin cardPin;
	public final PanelConfirmDialog cardConfirm;
	public final PanelCountDown cardCountdown;
	public final PanelNetwork cardNetwork;
	public final PanelPrinter cardPrinter;
	public final PanelKeyboard cardKeyboard;
	public final PanelControls cardControls;
	
	@GuiTask
	public View480x320() {
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
		buttonSettings.setIcon(Icons.SETTINGS.getIcon(Size.SIZE24X24));
		buttonSettings.addActionListener(e -> {
			showCard("Settings");
		});
		
		buttonLogs = new JButton();
		buttonLogs.setToolTipText("Controls");
		buttonLogs.setIcon(Icons.MENU.getIcon(Size.SIZE24X24));
		buttonLogs.addActionListener(e -> {
			showCard("Controls");
		});
		
		buttonMap = new JButton();
		buttonMap.setToolTipText("Map");
		buttonMap.setIcon(Icons.HOME.getIcon(Size.SIZE24X24));
		buttonMap.addActionListener(e -> {
			showCard("Map");
		});
		
		buttonPrintScan = new JButton();
		buttonPrintScan.setToolTipText("Printer");
		buttonPrintScan.setIcon(Icons.PRINTER.getIcon(Size.SIZE24X24));
		buttonPrintScan.addActionListener(e -> {
			showCard("Printer");
		});
		
		GroupLayout gl_panelMenu = new GroupLayout(panelMenu);
		gl_panelMenu.setHorizontalGroup(
			gl_panelMenu.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_panelMenu.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_panelMenu.createParallelGroup(Alignment.LEADING)
						.addComponent(buttonSettings, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonLock, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonMap, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonLogs, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE)
						.addComponent(buttonPrintScan, GroupLayout.PREFERRED_SIZE, 52, GroupLayout.PREFERRED_SIZE))
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
					.addComponent(buttonPrintScan, GroupLayout.PREFERRED_SIZE, 45, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(buttonSettings, GroupLayout.DEFAULT_SIZE, 38, Short.MAX_VALUE))
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
		panelMain.add(cardCountdown = new PanelCountDown(), "CountDown");
		panelMain.add(cardNetwork = new PanelNetwork(), "Network");
		panelMain.add(cardPrinter = new PanelPrinter(), "Printer");
		panelMain.add(cardKeyboard = new PanelKeyboard(), "Keyboard");
		panelMain.add(cardControls = new PanelControls(), "Controls");
		
		panelStatus = new PanelStatus();
		panelStatus.setBounds(3, 257, 474, 60);
		panelStatus.setCartoucheInfo("LEVEL");
		getContentPane().add(panelStatus);
	}
		
	public void showCard(String cardName) {
		CardLayout cl = (CardLayout)(panelMain.getLayout());
        cl.show(panelMain, cardName);
	}
	
	public void showPinCard(Action<String> handler) {
        if (!isCurrentCard(cardPin)) {
        	cardPin.reset();
            cardPin.handler = handler;
            showCard("Pin");
        }
	}
	
	public void showKeyboardCard(String layout, Action<String> handler) {
        if (!isCurrentCard(cardPin)) {
        	cardKeyboard.reset(layout);
        	cardKeyboard.handler = handler;
            showCard("Keyboard");
        }
	}
	
	public void showKeyboardCard(Action<String> handler) {
        showKeyboardCard("default", handler);
	}
	
	public boolean isCurrentCard(JPanel panel) {
		for (Component comp : panelMain.getComponents()) {
            if (comp.isVisible() == true && comp == panel) {
                return true;
            }
        }
		return false;
	}

	public void appendLog(String msg) {
		appendLog(msg, 0);
	}
	
	public void appendLog(String msg, int level) {
		StringBuilder date = new StringBuilder("[");
		Calendar now = Calendar.getInstance();
		date.append(now.get(Calendar.DAY_OF_MONTH));
		date.append("/");
		date.append(now.get(Calendar.MONTH) + 1);
		date.append("/");
		date.append(now.get(Calendar.YEAR));
		date.append(" ");
		date.append(now.get(Calendar.HOUR));
		date.append(":");
		date.append(now.get(Calendar.MINUTE));
		date.append(":");
		date.append(now.get(Calendar.SECOND));
		date.append("] ");
		SwingUtilities.invokeLater(() -> {
			cardLogs.append(date + msg + "\n", level);
		});
	}

	public void setButtonLockIcon(boolean locked) {
		buttonLock.setIcon(
				Icons.getIcon(locked ? Icons.PLAY : Icons.SHIELD, Size.SIZE24X24)
		);
	}

	public MapPanel showDefaultCard() {
		showCard("Map");
		return cardMap;
	}

	public void showConfirmDialogCard(String message, String[] answers, Action<String> callback) {
		showCard("ConfirmDialog");
		cardConfirm.textLabel.setText(message);
		cardConfirm.panelButtons.removeAll();
		ActionListener listener = (e) -> {
			callback.call(((JButton)e.getSource()).getText());
		};
		for (String text : answers) {
			boolean highlight = false;
			if (text.startsWith("*")) {
				text = text.substring(1);
				highlight = true;
			}
			cardConfirm.addButton(text, highlight).addActionListener(listener);
		}
	}

	public void showCountdownCard(String msg, int delaySecs, Action<Boolean> callback) {
		if (isCurrentCard(cardCountdown)) return;
		cardCountdown.setTimeout(delaySecs);
		cardCountdown.setMessage(msg);
		cardCountdown.setHandler(callback);
		showCard("CountDown");
	}

}

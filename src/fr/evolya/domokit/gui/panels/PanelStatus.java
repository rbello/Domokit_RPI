package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 *       .------------- Cartouche ------------.
 *       v                                    v
 *  ------------------------------------------------
 * | LabelLeft |         Title         | LabelRight |
 * |-----------|-----------------------|------------|
 * | ValueLeft |         Status        | ValueRight |
 *  ------------------------------------------------
 * 
 */
public class PanelStatus extends JPanel {

	private static final long serialVersionUID = -6855468458877911636L;

	protected Color borderColor = Color.WHITE;

	protected int borderWidth = 6;
	protected int cartoucheWidth = 50;
	protected int cartoucheHeight = 15;

	protected JLabel cartoucheLabelLeft;
	protected JLabel cartoucheLabelRight;
	protected JLabel cartoucheValueLeft;
	protected JLabel cartoucheValueRight;
	protected JLabel labelTitle;
	protected JLabel labelMessage;
	
	/**
	 * Create the panel.
	 */
	public PanelStatus() {
		setLayout(null);
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		setBorderColor(Color.WHITE);
		
		cartoucheLabelLeft = new JLabel();
		cartoucheLabelLeft.setHorizontalAlignment(SwingConstants.CENTER);
		cartoucheLabelLeft.setFont(new Font("Arial", Font.BOLD, 12));
		add(cartoucheLabelLeft);
		cartoucheValueLeft = new JLabel();
		cartoucheValueLeft.setFont(new Font("Arial", Font.BOLD, 25));
		cartoucheValueLeft.setHorizontalAlignment(SwingConstants.CENTER);
		add(cartoucheValueLeft);
		
		cartoucheLabelRight = new JLabel();
		cartoucheLabelRight.setHorizontalAlignment(SwingConstants.CENTER);
		cartoucheLabelRight.setFont(new Font("Arial", Font.BOLD, 12));
		add(cartoucheLabelRight);
		cartoucheValueRight = new JLabel();
		cartoucheValueRight.setHorizontalAlignment(SwingConstants.CENTER);
		cartoucheValueRight.setFont(new Font("Arial", Font.BOLD, 25));
		add(cartoucheValueRight);
		
		labelTitle = new JLabel();
		labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
		labelTitle.setFont(new Font("Arial", Font.ITALIC | Font.BOLD, 35));
		add(labelTitle);
		labelMessage = new JLabel();
		labelMessage.setHorizontalAlignment(SwingConstants.CENTER);
		labelMessage.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 12));
		add(labelMessage);
		
		cartoucheLabelLeft.setText("LEFT");
		cartoucheValueLeft.setText("0");
		cartoucheLabelRight.setText("RIGHT");
		cartoucheValueRight.setText("0");
		labelTitle.setText("BIG");
		labelMessage.setText("SMALL");
		
		replace();
	}
	
	protected void replace() {
		
		cartoucheLabelLeft.setBounds(borderWidth, borderWidth, cartoucheWidth, cartoucheHeight);
		cartoucheValueLeft.setBounds(borderWidth, borderWidth + borderWidth / 2 + cartoucheHeight, cartoucheWidth, getHeight() - borderWidth * 2 - borderWidth / 2 - cartoucheHeight);
		
		cartoucheLabelRight.setBounds(getWidth() - cartoucheWidth - borderWidth, borderWidth, cartoucheWidth, cartoucheHeight);
		cartoucheValueRight.setBounds(getWidth() - cartoucheWidth - borderWidth, borderWidth + borderWidth / 2 + cartoucheHeight, cartoucheWidth, getHeight() - borderWidth * 2 - borderWidth / 2 - cartoucheHeight);
		
		labelTitle.setBounds(borderWidth * 2 + cartoucheWidth, borderWidth, getWidth() - 4 * borderWidth - 2 * cartoucheWidth, getHeight() - 2 * borderWidth - borderWidth / 2 - cartoucheHeight);
		labelMessage.setBounds(borderWidth * 2 + cartoucheWidth, getHeight() - borderWidth - cartoucheHeight, getWidth() - 4 * borderWidth - 2 * cartoucheWidth, cartoucheHeight);
	}
	
	public int getBorderWidth() {
		return borderWidth;
	}
	
	public void setBorderWidth(int value) {
		this.borderWidth = value;
	}
	
	public int getCartoucheWidth() {
		return cartoucheWidth;
	}

	public void setCartoucheWidth(int cartoucheWidth) {
		this.cartoucheWidth = cartoucheWidth;
	}

	public int getCartoucheHeight() {
		return cartoucheHeight;
	}

	public void setCartoucheHeight(int cartoucheHeight) {
		this.cartoucheHeight = cartoucheHeight;
	}

	public Color getBorderColor() {
		return borderColor;
	}

	public PanelStatus setBorderColor(Color color) {
		if (!this.borderColor.equals(color)) {
			this.borderColor = color;
			repaint();
		}
		return this;
	}

	public String getTitle() {
		return labelTitle.getText();
	}
	
	public PanelStatus setTitle(String text) {
		labelTitle.setText(text);
		return this;
	}
	
	public String getMessage() {
		return labelMessage.getText();
	}

	public PanelStatus setMessage(String text) {
		labelMessage.setText(text);
		return this;
	}
	
	public String getCartoucheInfo() {
		return cartoucheLabelLeft.getText();
	}
	
	public PanelStatus setCartoucheInfo(String text) {
		cartoucheLabelLeft.setText(text);
		cartoucheLabelRight.setText(text);
		return this;
	}
	
	public int getCartoucheLevel() {
		return new Integer(cartoucheLabelLeft.getText());
	}
	
	public PanelStatus setCartoucheLevel(int value) {
		cartoucheValueLeft.setText("" + value);
		cartoucheValueRight.setText("" + value);
		return this;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		replace();
		g.setColor(borderColor);
		// Border
		g.fillRect(0, 0, borderWidth, getHeight());
		g.fillRect(getWidth() - borderWidth, 0, borderWidth, getHeight());
		g.fillRect(0, 0, getWidth(), borderWidth);
		g.fillRect(0, getHeight() - borderWidth, getWidth(), borderWidth);
		// Left
		g.fillRect(borderWidth, borderWidth + cartoucheHeight, cartoucheWidth, borderWidth / 2);
		g.fillRect(borderWidth + cartoucheWidth, borderWidth, borderWidth, getWidth() - borderWidth);
		// Right
		g.fillRect(getWidth() - borderWidth - cartoucheWidth, borderWidth + cartoucheHeight, cartoucheWidth, borderWidth / 2);
		g.fillRect(getWidth() - borderWidth*2 - cartoucheWidth, borderWidth, borderWidth, getHeight() - borderWidth *2);
		// Separator
		g.fillRect(borderWidth * 2 + cartoucheWidth, getHeight() - borderWidth - cartoucheHeight - borderWidth / 2, getWidth() - 2 * cartoucheWidth - borderWidth * 4, borderWidth / 2);
	}

}

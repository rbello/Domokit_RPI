package fr.evolya.domokit.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;

import fr.evolya.javatoolkit.code.funcint.Action;

public class PanelPin extends JPanel {
	
	private static final long serialVersionUID = -1126139605955119674L;

	public final JButton button1;
	public final JButton button2;
	public final JButton button3;
	public final JButton button4;
	public final JButton button5;
	public final JButton button6;
	public final JButton button7;
	public final JButton button8;
	public final JButton button9;
	public final JButton button0;
	public final JButton buttonCancel;
	public final JButton buttonOk;
	public final JLabel passwordLabel;
	
	private List<JButton> buttons = new ArrayList<>();
	private String passwordClear;
	
	public Action<String> handler;
	
	public PanelPin() {
		
		button1 = new JButton("1");
		button1.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button2 = new JButton("1");
		button2.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button3 = new JButton("1");
		button3.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button5 = new JButton("1");
		button5.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button6 = new JButton("1");
		button6.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button7 = new JButton("1");
		button7.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button9 = new JButton("1");
		button9.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button0 = new JButton("1");
		button0.setFont(new Font("Tahoma", Font.PLAIN, 20));		
		
		button4 = new JButton("1");
		button4.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		button8 = new JButton("1");
		button8.setFont(new Font("Tahoma", Font.PLAIN, 20));
		
		buttonCancel = new JButton("Cancel");
		buttonCancel.setBackground(new Color(153, 153, 153));
		buttonCancel.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		buttonOk = new JButton("OK");
		buttonOk.setBackground(new Color(51, 153, 255));
		buttonOk.setFont(new Font("Tahoma", Font.PLAIN, 16));
		
		passwordLabel = new JLabel("***");
		passwordLabel.setFont(new Font("Tahoma", Font.BOLD, 20));
		passwordLabel.setHorizontalAlignment(SwingConstants.CENTER);
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING, false)
						.addComponent(passwordLabel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(Alignment.LEADING, groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
								.addGroup(groupLayout.createSequentialGroup()
									.addComponent(button9, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(button0, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
									.addGap(6)
									.addComponent(buttonCancel, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
								.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(button5, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
										.addGap(6)
										.addComponent(button6, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
										.addGap(6)
										.addComponent(button7, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))
									.addGroup(groupLayout.createSequentialGroup()
										.addComponent(button1, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(button2, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
										.addPreferredGap(ComponentPlacement.RELATED)
										.addComponent(button3, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(button4, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
								.addComponent(button8, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonOk, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(44, Short.MAX_VALUE))
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(passwordLabel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(button4, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(button8, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
							.addGap(6)
							.addComponent(buttonOk, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(button3, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(button2, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(button1, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(button5, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(button6, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(button7, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(button9, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(button0, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE)
								.addComponent(buttonCancel, GroupLayout.PREFERRED_SIZE, 61, GroupLayout.PREFERRED_SIZE))))
					.addContainerGap(185, Short.MAX_VALUE))
		);
		setLayout(groupLayout);
		
		buttons.add(button0);
		buttons.add(button1);
		buttons.add(button2);
		buttons.add(button3);
		buttons.add(button4);
		buttons.add(button5);
		buttons.add(button6);
		buttons.add(button7);
		buttons.add(button8);
		buttons.add(button9);
		
		ActionListener cb = (e) -> {
			int value = Integer.parseInt(((JButton)e.getSource()).getText());
			passwordLabel.setText(passwordLabel.getText() + "* ");
			passwordClear += value;
		};
		
		for (JButton button : buttons) button.addActionListener(cb);
		
		buttonOk.addActionListener((e) -> {
			handler.call(passwordClear);
		});
		
		buttonCancel.addActionListener((e) -> {
			if ("".equals(passwordClear)) {
				// Cancel
				handler.call(null);
			}
			else {
				// RAZ
				passwordLabel.setText("");
				passwordClear = "";
			}
		});
		
	}
	
	public void reset() {
		shuffle();
		clear();
		
	}
	
	public void clear() {
		passwordLabel.setText("");
		passwordClear = "";
	}

	public void shuffle() {
		Random generator = new Random();
		List<Integer> done = new ArrayList<>();
		while (done.size() < 10) {
			int value = generator.nextInt(10);
			if (!done.contains(value)) done.add(value);
		}
		int i = 0;
		for (JButton button : buttons) {
			button.setText("" + done.get(i++));
		}
	}
}

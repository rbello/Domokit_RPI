package fr.evolya.domokit.io;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import fr.evolya.domokit.config.Configuration;
import fr.evolya.domokit.gui.map.features.Rf433Emitter;
import fr.evolya.domokit.gui.map.iface.IMap;
import fr.evolya.domokit.gui.map.simple.Device;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeReceived;
import fr.evolya.javatoolkit.app.App;
import fr.evolya.javatoolkit.app.event.ApplicationStarted;
import fr.evolya.javatoolkit.app.event.GuiIsReady;
import fr.evolya.javatoolkit.code.annotations.GuiTask;
import fr.evolya.javatoolkit.code.annotations.Inject;
import fr.evolya.javatoolkit.events.fi.BindOnEvent;
import javax.swing.SwingConstants;
import java.awt.Font;

public class Rf433DebugView extends JFrame implements ActionListener {

	private static final long serialVersionUID = -6467941167343702663L;
	
	@Inject
	public App app;
	
	@Inject
	public Rf433Controller rf433;

	private JLabel label;

	private DefaultListModel model;

	private JButton button;

	private JList list;


	@GuiTask
	public Rf433DebugView() {
		setTitle("RF433 Injector");
		
		label = new JLabel("RF433 Controller : Not found");
		label.setFont(new Font("Tahoma", Font.BOLD, 11));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		
		model = new DefaultListModel();
		
		list = new JList();
		list.setModel(model);
//		list.addListSelectionListener(this);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2) return;
				actionPerformed(null);
			}
		});

		button = new JButton("Send");
		button.addActionListener(this);
		
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(groupLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(list, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addComponent(label, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 414, Short.MAX_VALUE)
						.addComponent(button, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(label)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(list, GroupLayout.DEFAULT_SIZE, 227, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(button)
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
		initialize();
	}

	private void initialize() {
		setBounds(500, 30, 287, 336);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	@BindOnEvent(ApplicationStarted.class)
	@GuiTask
	public void run(App app) {
		Rf433Controller rf = app.get(Rf433Controller.class);
		if (rf == null) return;
		label.setText("RF433 Controller : OK");
		Configuration.getInstance()
			.getMap()
			.getComponents(Device.class, (c) -> c.hasFeature(Rf433Emitter.class))
			.forEach((device) -> {
				device.getFeatures(Rf433Emitter.class).stream().forEach((feature) -> {
					model.addElement(feature.getRfCode() + " = " + 
						feature.getCommandName() + " on " + device.getIdentifier());
				});
			});
		setVisible(true);
		app.notify(GuiIsReady.class, this, app);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (list.getSelectedValue() == null) return;
		int code = new Integer(list.getSelectedValue().toString().split(" ")[0]);
		app.notify(OnRf433CodeReceived.class, code, rf433);
	}

}

package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.gui.map.iface.IFeature;

public class Rf433Emitter implements IFeature {

	protected int rfCode = 0;
	protected String commandName = "NO_NAME";

	public int getRfCode() {
		return rfCode;
	}

	public void setRfCode(int rfCode) {
		this.rfCode = rfCode;
	}

	public String getCommandName() {
		return commandName;
	}

	public void setCommandName(String commandName) {
		this.commandName = commandName;
	}
	
}

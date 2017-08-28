package fr.evolya.domokit.gui.map.features;

import java.util.Date;

public class Rf433Emitter extends AbstractFeature {

	protected int rfCode = 0;
	protected String commandName = "NO_NAME";
	
	protected long lastReveived = 0;
	
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
	
	@Override
	public String toString() {
		return commandName;
	}

	public boolean accept(int code) {
		long millis = new Date().getTime();
		// Only 1 same message by second
		if (millis - lastReveived < 1000) return false;
		lastReveived = millis;
		return true;
	}

}

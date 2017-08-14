package fr.evolya.domokit.gui.map.features;

import fr.evolya.domokit.gui.map.simple.IFeature;

public class Rf433Emitter implements IFeature {

	protected int rfCode = 0;

	public int getRfCode() {
		return rfCode;
	}

	public void setRfCode(int rfCode) {
		this.rfCode = rfCode;
	}
	
}

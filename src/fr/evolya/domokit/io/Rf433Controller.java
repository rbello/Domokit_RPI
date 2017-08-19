package fr.evolya.domokit.io;

import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeEmitted;
import fr.evolya.domokit.io.Rf433Controller.OnRf433CodeReceived;
import fr.evolya.javatoolkit.code.annotations.AsynchOperation;
import fr.evolya.javatoolkit.code.funcint.Action;
import fr.evolya.javatoolkit.events.fi.DecorableObservable;
import fr.evolya.javatoolkit.events.fi.EventProvider;
import fr.evolya.javatoolkit.events.fi.Observable;

@EventProvider({OnRf433CodeReceived.class, OnRf433CodeEmitted.class})
public class Rf433Controller extends DecorableObservable implements Runnable {
	
	public Rf433Controller() {
		super();
	}

	public Rf433Controller(Observable dispatcher) {
		super(dispatcher);
	}

	@FunctionalInterface
	public static interface OnRf433CodeReceived {
		void onRf433CodeReceived(int code, Rf433Controller ctrl);
	}
	
	@FunctionalInterface
	public static interface OnRf433CodeEmitted {
		void onRf433CodeEmitted(int code, Rf433Controller ctrl);
	}
	
	public void emmit(int code, int repeat) {
		// TODO Implements emmitting
		notify(OnRf433CodeEmitted.class, code, this);
	}

	@AsynchOperation
	public void emmit(int code, int repeat, Action<Throwable> callback) {
		new Thread(() -> {
			try {
				// Emmit code
				emmit(code, repeat);
				// Call back without error
				callback.call(null);
			}
			catch (Throwable t) {
				// Call back with error
				callback.call(t);
			}
		});
	}

	@Override
	public void run() {
		// TODO Implements polling
		int code = 0;
		notify(OnRf433CodeReceived.class, code, this);
	}
	
}

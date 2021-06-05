package fr.evolya.domokit.config;

import java.util.List;

import fr.evolya.javatoolkit.events.rx.Observable;
import static fr.evolya.javatoolkit.events.rx.Observable.of;
import static fr.evolya.javatoolkit.events.rx.Observable.ofList;

public class Model {

	public final Observable<List<JenkinsJob>> jobs = ofList();
	public final Observable<Integer> failures = of(0);
	
}

package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

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
public class PanelStatusStateManager extends PanelStatus {

	private static final long serialVersionUID = 1922468828133750465L;
	
	private Map<Integer, String> registredCategories = new HashMap<>();
	private Map<String, State> currentStates = new HashMap<>();
	
	public PanelStatusStateManager() {
		super();
		setCartoucheInfo("LEVEL");
		createCategory("Error", 10);
	}
	
	public void createCategory(String category, int priority) {
		registredCategories.put(priority, category);
		currentStates.put(category, null);
	}

	public boolean setState(State state) {
		if (state == null)
			throw new NullPointerException();
		String category = state.getStateCategory();
		if (!currentStates.containsKey(category)) {
			throw new IllegalArgumentException("Please create state category '" + category + "' before use setState()");
		}
		State old = currentStates.get(state.getStateCategory());
		if (old != null) {
			// State are equals
			if (old.equals(state)) return false;
			// Less priority
			if (old.getPriority() > state.getPriority()) return false;
			// TODO Event onStateRemoved ?
		}
		System.out.println("[PanelStatus] Change state '" + category + "' with '" + state + "'");
		currentStates.put(state.getStateCategory(), state);
		updateDisplay();
		return true;
	}
	
	public void updateDisplay() {
		State state = null;
		for (int i = 0, found = 0, count = registredCategories.size(); found < count; i++) {
			String category = registredCategories.get(i);
			if (category == null) continue;
			state = currentStates.get(category);
			if (state != null) break;
		}
		if (state == null) return;
		System.out.println("[PanelStatus] Display category '" + state.getStateCategory() 
			+ "' state '" + state + "'");
		setTitle(state.getStateName());
		setBorderColor(state.getColor());
		setStatus(state.getMessage());
		setCartoucheLevel(state.getPriority());
		repaint();
	}
	
	public void removeByCategory(String category) {
		if (!currentStates.containsKey(category)) {
			throw new IllegalArgumentException("Please create state category '" + category + "' before use removeByCategory()");
		}
		currentStates.put(category, null);
		updateDisplay();
	}

	public State getStateByCategory(String category) {
		return currentStates.get(category);
	}
	
	public static abstract class State {
		
		public abstract String getStateCategory();
		
		public abstract Color getColor();

		public abstract String getStateName();
		
		public abstract String getMessage();
		
		public abstract boolean isAllwaysOnTop();
		
		public abstract int getPriority();
		
		public boolean equals(State state) {
			if (state == null) return false;
			if (!state.getStateCategory().equals(getStateCategory())) return false;
			if (!state.getStateName().equals(getStateName())) return false;
			if (!state.getMessage().equals(getMessage())) return false;
			return true;
		}
		
		@Override
		public String toString() {
			return getStateName();
		}
	
	}
	
	public static class AlertState extends State {
		private String message;
		private int priority;
		public AlertState(String message, int priority) {
			this.message = message;
			this.priority = priority;
		}
		public String getStateName() {
			return "Error";
		}
		public String getStateCategory() {
			return "Error";
		}
		public boolean isAllwaysOnTop() {
			return true;
		}
		public Color getColor() {
			return Color.RED;
		}
		public String getMessage() {
			return message;
		}
		public int getPriority() {
			return priority;
		}
	}
	
	public static class WarningState extends State {
		private String message;
		private int priority;
		public WarningState(String message, int priority) {
			this.message = message;
			this.priority = priority;
			
		}
		public String getStateName() {
			return "Warning";
		}
		public String getStateCategory() {
			return "Error";
		}
		public boolean isAllwaysOnTop() {
			return true;
		}
		public Color getColor() {
			return Color.YELLOW;
		}
		public String getMessage() {
			return message;
		}
		public int getPriority() {
			return priority;
		}
	}

}

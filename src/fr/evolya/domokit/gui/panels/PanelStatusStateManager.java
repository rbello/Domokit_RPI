package fr.evolya.domokit.gui.panels;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
	
	private Map<Integer, Category> registredCategories = new HashMap<>();
	
	public PanelStatusStateManager() {
		super();
		setCartoucheInfo("LEVEL");
		createCategory("Alert", 10, 10);
		createCategory("Warning", 5, 10);
	}
	
	public void createCategory(String category, int priority) {
		createCategory(category, priority, 0);
	}
	
	public void createCategory(String category, int priority, int maxLength) {
		//System.out.println("[PanelStatus] Create category " + category + " priority " + priority);
		if (registredCategories.containsKey(priority)) {
			throw new IllegalArgumentException("Priority " + priority + " is allready used by category"
					+ "'" + registredCategories.get(priority) + "'");
		}
		registredCategories.put(priority, new Category(category, priority, maxLength));
	}
	
	private Category getCategory(String name) {
		for (Category cat : registredCategories.values()) {
			if (cat.name.equals(name)) return cat;
		}
		return null;
	}
	
	public boolean addState(State state) {
		
		// Check state
		if (state == null) {
			throw new NullPointerException();
		}
		
		// Check category
		final Category category = getCategory(state.getStateCategory());
		if (category == null) {
			throw new IllegalArgumentException("Please create state category '" + category + "' before use setState()");
		}
		
		// State is already added
		if (category.contains(state)) {
			return false;
		}
		
		// Log
		System.out.println("[PanelStatus] Add state '" + state + "' to '" + category + "'");
		
		// Add state to category
		category.add(state);
		
		// Update display
		updateDisplay();
		
		return true;
	}
	
	public void updateDisplay() {
		Category category = getCurrentCategory();
		State state = category.getFirstState();
		if (state == null) return;
//		System.out.println("[PanelStatus] Display category '" + state.getStateCategory() 
//			+ "' state '" + state + "'");
		setTitle(state.getStateName());
		setBorderColor(state.getColor());
		setStatus(state.getMessage());
//		setCartoucheLevel(state.getPriority());
		repaint();
	}
	
	public Category getCurrentCategory() {
		for (int i = 100, found = 0, count = registredCategories.size();
				found < count && i >= 0; i--) {
			if (!registredCategories.containsKey(i)) continue;
			Category category = registredCategories.get(i);
			//System.out.println("Category " + category + " priority " + i);
			if (category.empty()) continue;
			return category;
			//State state = currentStates.get(category);
			//if (state == null) continue;
			//System.out.println("Choosen " + category + " state " + currentStates.get(category));
			//return currentStates.get(category);
		}
		return null;
	}
	
	public void removeByCategory(String categoryName) {
		Category category = getCategory(categoryName);
		if (category == null) {
			throw new IllegalArgumentException("Please create state category '" + categoryName
					+ "' before use removeByCategory()");
		}
		category.clear();
		updateDisplay();
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
			return "Alert";
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
			return "Warning";
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

	public void clearAlertAndWarning() {
		removeByCategory("Alert");
		removeByCategory("Warning");
	}
	
	private class Category {

		public final String name;
		public final int priority;
		public final int length;
		private final List<State> states;

		public Category(String category, int priority, int maxLength) {
			this.name = category;
			this.priority = priority;
			this.length = maxLength;
			this.states = new ArrayList<>();
		}
		
		public State getFirstState() {
			return empty() ? null : states.get(0);
		}

		public void clear() {
			states.clear();
		}

		public void add(State state) {
			if (length > 0 && length == states.size()) {
				states.remove(0);
			}
			states.add(state);
		}

		public boolean contains(State state) {
			for (State s : states) {
				if (s.equals(state)) return true;
			}
			return states.contains(state);
		}

		public boolean empty() {
			return states.size() == 0;
		}

		@Override
		public String toString() {
			return "Category " + name + " priority " + priority;
		}
		
	}

	public State getStateByCategory(String categoryName) {
		Category category = getCategory(categoryName);
		if (category == null) return null;
		return category.getFirstState();
	}

}

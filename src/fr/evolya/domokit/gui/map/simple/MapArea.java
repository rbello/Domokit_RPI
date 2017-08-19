package fr.evolya.domokit.gui.map.simple;

import java.util.ArrayList;
import java.util.List;

public class MapArea {
	
	public String name;
	
	public List<String> rooms;
	
	public MapArea() {
		this.rooms = new ArrayList<>();
	}
	
	public void addRoom(String room) {
		this.rooms.add(room);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getRooms() {
		return rooms;
	}

}

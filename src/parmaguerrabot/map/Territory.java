package parmaguerrabot.map;

import java.awt.Color;
import java.awt.Point;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parmaguerrabot.Utils;
import parmaguerrabot.log.Logger;
import parmaguerrabot.serialize.json.JSONObject;

@SuppressWarnings("serial")
public class Territory implements Comparable<Territory>, Serializable {

	public static final String PREFIX_COMUNE = "Il comune di ";
	public static final String PREFIX_QUARTIERE = "Quartiere ";
	
	private transient Map map;
	
	/*
	 * Basic fields
	 */
	public int id;
	public String name;
	public String prefix;
	public int owner;
	public Color color;
	public Point coordinates;
	public List<Integer> neighbors;
	public List<Integer> underControl;
	public boolean insurrection;
	public Date insurrectionDate;
	public String insurrectionLoser;
	
	/*
	 * Stats fields
	 */
	public int timesAttackStat;
	public int timesLostStat;
	public Date deathTimeStat;
	public int maxTerritoriesUnderControlStat;	
	
	
	public Territory() { }
	
	public Territory(Map map, int id, String name, Point coordinates, String prefix) {
		this.map = map;
		this.id = id;
		this.name = name;
		this.coordinates = coordinates;
		this.prefix = prefix;
		
		init();
	}


	private void init() {
		this.owner = id;
		
		underControl = new ArrayList<>();
		underControl.add(id);
		
		this.timesAttackStat = 0;
		this.timesLostStat = 0;
		this.deathTimeStat = null;
		
		this.maxTerritoriesUnderControlStat = underControl.size();
		
		this.insurrection = false;
		this.insurrectionDate = null;
		this.insurrectionLoser = null;
		
		this.color = Utils.getRandomColor();
		Logger.mapLog("Setted color (" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ") for " + name);
		
		Logger.mapLog("");
	}
	
	public List<Integer> getUnderControl() {
		return underControl;
	}
	
	public void addNeighbors(int[] array) {
		Logger.mapLog("-------- Adding neighbors to '" + this.name + "' [" + this.id + "] --------");
		
		for(int i : array) {
			addNeighbor(map.territories.get(i).id);
			map.territories.get(i).addNeighbor(this.id);
		}
		Logger.mapLog("");
	}
	
	private void addNeighbor(int index) {
		if(neighbors == null)
			neighbors = new ArrayList<>();
		
		if(!neighbors.contains(index)) {
			neighbors.add(index);
			Logger.mapLog("Added neighbor '" + map.get(index).name + "' [" + index + "] to " + this.name);
		}
	}
	
	
	public boolean canAttack() {
		for(int i : neighbors) {
			if(map.get(i).owner != owner)
				return true;
		}
		
		return false;
	}
	
	
	public Territory getRandomNeighborToAttack() {
		Territory enemy;
		
		do {
			enemy = map.get(neighbors.get(Utils.randomInt(neighbors.size())));
		} while(enemy.owner == owner);
		
		return enemy;
	}
	
	public void attack(Territory loser, Territory loserOwner, List<Integer> alive, List<Integer> dead) {
		underControl.add(Integer.valueOf(loser.id));
		if(this.underControl.size() > this.maxTerritoriesUnderControlStat)
			this.maxTerritoriesUnderControlStat = this.underControl.size();
		
		this.timesAttackStat++;
		
		
		loserOwner.underControl.remove(Integer.valueOf(loser.id));
		loser.owner = this.id;
		loserOwner.timesLostStat++;
		if(loserOwner.underControl.isEmpty()) {
			loserOwner.deathTimeStat = map.date;
			
			alive.remove(Integer.valueOf(loserOwner.id));
			dead.add(Integer.valueOf(loserOwner.id));
		}
	}
	
	public boolean hasInsurrect() {
		return insurrection;
	}
	
	public void insurrect(Territory loserOwner, List<Integer> alive, List<Integer> dead) {
		this.owner = this.id;
		
		this.timesAttackStat++;
		loserOwner.timesLostStat++;
		
		Integer iid = Integer.valueOf(this.id);
		
		dead.remove(iid);
		alive.add(iid);
		underControl.add(iid);
		loserOwner.underControl.remove(iid);
		
		this.deathTimeStat = null;
		this.insurrection = true;		
		this.insurrectionDate = map.date;
		this.insurrectionLoser = loserOwner.name;
	}
	
	public void removeFromControl(int id) {
		underControl.remove(Integer.valueOf(id));
	}
	
	public boolean isDeath() {
		return this.deathTimeStat != null;
	}
	
	public int daySurvived() {
		if(deathTimeStat != null) 
			return Utils.getDifferenceInDaysFromFirstDay(deathTimeStat);
		
		return -1;
	}
	
	public List<Integer> getNeighbors() {
		return this.neighbors;
	}
	

	@Override
	public int compareTo(Territory t) {
		if(this.underControl.size() > t.underControl.size())
			return -1;
		if(this.underControl.size() < t.underControl.size())
			return 1;
		
		return this.name.compareTo(t.name);
	}
	
	public int getScore() {
		return underControl.size();
	}
	
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof Territory) {
			return this.id == ((Territory) obj).id;
		}
		return false;
	}
	
	
	public JSONObject getJSONObject() throws IllegalAccessException {
		JSONObject json = new JSONObject();
		
		for(Field f : this.getClass().getFields()) {
			int modifiers = f.getModifiers();
			
			if(!Modifier.isStatic(modifiers)) {
				if(f.getType().isAssignableFrom(Date.class) && f.get(this) != null) {
					String date = Utils.DATE_FORMAT.format((Date) f.get(this));
					json.put(f.getName(), date);
				}
				else if(f.getType().isAssignableFrom(Color.class)) 
					json.put(f.getName(), this.color.getRGB());
				else
					json.put(f.getName(), f.get(this));
			}
		}
		
		return json;
	}
	
}

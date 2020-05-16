package parmaguerrabot.map;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parmaguerrabot.Utils;
import parmaguerrabot.log.Logger;

@SuppressWarnings("serial")
public class Territory implements Comparable<Territory>, Serializable {

	public static final String PREFIX_COMUNE = "Il comune di ";
	public static final String PREFIX_QUARTIERE = "Quartiere ";
	
	
	private final Map map;
	
	public final int id;
	public final String name;
	public int owner;
	
	public Color color;
	public Coordinates coordinates;
	
	private List<Integer> neighbors;
	
	private List<Integer> underControl;
	
	public int timesAttackStat;
	public int timesLostStat;
	public Date deathTimeStat;
	public int maxTerritoriesUnderControlStat;
	
	public String prefix;
	
	private boolean insurrection;
	public Date insurrectionDate;
	public String insurrectionLoser;
	
	
	public Territory(Map map, int id, String name, Coordinates coordinates, String prefix) {
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
	
	public void addUnderControl(Territory loser, Territory loserOwner, List<Integer> alive, List<Integer> dead) {
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
		
		System.out.println(loserOwner.name + " " + loserOwner.id);
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
}

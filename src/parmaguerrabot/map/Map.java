package parmaguerrabot.map;

import java.awt.Point;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import parmaguerrabot.GameConstants;
import parmaguerrabot.Utils;
import parmaguerrabot.images.ImagesGenerator;
import parmaguerrabot.log.Logger;
import parmaguerrabot.serialize.MapSerializer;


@SuppressWarnings("serial")
public class Map implements Serializable {

	public static final String FILE_TO_SERIALIZE = "game/map.sav";
	public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
	
	public List<Territory> territories;
	
	public Date date;
	
	public Territory tWinner;
	public int insurrections;
	private int dayFromLastInsurrection;
	
	
	public Map() {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, 2020);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DATE, 1);
		
		date = cal.getTime();		
		
		territories = new ArrayList<>();
		
		insurrections = 0;
		dayFromLastInsurrection = 0;
	}
	
	/**
	 * Method called to initialize the map class. Generates the 01/01/2020 map image
	 * 
	 * @throws IOException
	 */
	public void init() throws IOException {
		territories.add(new Territory(this, 0, "Polesine Zibello", new Point(1412,4), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 1, "Roccabianca", new Point(1712,43), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 2, "Sissa Trecasali", new Point(1857,145), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 3, "Colorno", new Point(2071,215), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 4, "Sorbolo Mezzani", new Point(2222,308), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 5, "Busseto", new Point(1287,127), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 6, "San Secondo Parmense", new Point(1687,277), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 7, "Soragna", new Point(1494,217), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 8, "Fidenza", new Point(1278,380), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 9, "Fontanellato", new Point(1555,437), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 10, "Salsomaggiore Terme", new Point(1086,695), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 11, "Noceto", new Point(1407,683), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 12, "Medesano", new Point(1277,939), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 13, "Torrile", new Point(2001,352), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 14, "Fontevivo", new Point(1690,618), Territory.PREFIX_COMUNE));
		
		territories.add(new Territory(this, 15, "Parma Centro", new Point(2075,837), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 16, "Oltretorrente", new Point(2058,839), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 17, "Molinetto", new Point(1926,880), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 18, "Pablo", new Point(2019,815), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 19, "Golese", new Point(1858,522), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 20, "San Pancrazio", new Point(1824,779), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 21, "San Leonardo", new Point(2066,793), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 22, "Cortile San Martino", new Point(2043,588), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 23, "Lubiana", new Point(2116,891), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 24, "San Lazzaro", new Point(2124,763), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 25, "Cittadella", new Point(2075,886), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 26, "Montanara", new Point(2035,907), Territory.PREFIX_QUARTIERE));
		territories.add(new Territory(this, 27, "Vigatto", new Point(1890,961), Territory.PREFIX_QUARTIERE));
		
		territories.add(new Territory(this, 28, "Collecchio", new Point(1604,857), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 29, "Montechiarugolo", new Point(2094,1067), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 30, "Traversetolo", new Point(2072,1291), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 31, "Pellegrino Parmense", new Point(927,854), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 32, "Bore", new Point(761,1015), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 33, "Varano de' Melegari", new Point(1029,1139), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 34, "Fornovo di Taro", new Point(1367,1190), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 35, "Varsi", new Point(713,1262), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 36, "Solignano", new Point(1056,1274), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 37, "Terenzo", new Point(1268,1407), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 38, "Valmozzola", new Point(854,1505), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 39, "Calestano", new Point(1422,1434), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 40, "Sala Baganza", new Point(1704,1108), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 41, "Felino", new Point(1737,1149), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 42, "Langhirano", new Point(1709,1264), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 43, "Lesignano de' Bagni", new Point(1879,1339), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 44, "Neviano degli Arduini", new Point(1855,1578), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 45, "Tizzano Val Parma", new Point(1614,1724), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 46, "Palanzano", new Point(1635,1982), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 47, "Albareto", new Point(431,1903), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 48, "Borgo Val di Taro", new Point(610,1712), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 49, "Berceto", new Point(1053,1572), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 50, "Corniglio", new Point(1247,1692), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 51, "Monchio delle Corti", new Point(1443,2116), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 52, "Bardi", new Point(269,1281), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 53, "Bedonia", new Point(116,1546), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 54, "Compiano", new Point(476,1638), Territory.PREFIX_COMUNE));
		territories.add(new Territory(this, 55, "Tornolo", new Point(4,1980), Territory.PREFIX_COMUNE));
		
				
		territories.get(0).addNeighbors(new int[] {1,5,7});
		territories.get(1).addNeighbors(new int[] {2,6,7});
		territories.get(2).addNeighbors(new int[] {3,6,9,13,19});
		territories.get(3).addNeighbors(new int[] {4,13});
		territories.get(4).addNeighbors(new int[] {13,22,24});
		territories.get(5).addNeighbors(new int[] {7,8});
		territories.get(6).addNeighbors(new int[] {7,9});
		territories.get(7).addNeighbors(new int[] {8,9});
		territories.get(8).addNeighbors(new int[] {9,10,11,12});
		territories.get(9).addNeighbors(new int[] {11,14,19});
		territories.get(10).addNeighbors(new int[] {12,31});
		territories.get(11).addNeighbors(new int[] {12,20,28});
		territories.get(12).addNeighbors(new int[] {31,33});
		territories.get(13).addNeighbors(new int[] {19,22});
		territories.get(14).addNeighbors(new int[] {19,20});
		
		territories.get(15).addNeighbors(new int[] {16,18,21,23,24,25});
		territories.get(16).addNeighbors(new int[] {17,18,25,26});
		territories.get(17).addNeighbors(new int[] {18,20,26,27,28});
		territories.get(18).addNeighbors(new int[] {19,20,21});
		territories.get(19).addNeighbors(new int[] {20,21,22});
		territories.get(20).addNeighbors(new int[] {28});
		territories.get(21).addNeighbors(new int[] {22,24});
		territories.get(22).addNeighbors(new int[] {24});
		territories.get(23).addNeighbors(new int[] {24,25,29});
		territories.get(24).addNeighbors(new int[] {});
		territories.get(25).addNeighbors(new int[] {26,27,29});
		territories.get(26).addNeighbors(new int[] {27});
		territories.get(27).addNeighbors(new int[] {28,29,30,40,41,42,43});
		
		territories.get(28).addNeighbors(new int[] {17,20,27,34,40});
		territories.get(29).addNeighbors(new int[] {23,25,27,30});
		territories.get(30).addNeighbors(new int[] {27,43,44});
		territories.get(31).addNeighbors(new int[] {32,33});
		territories.get(32).addNeighbors(new int[] {33,35,52});
		territories.get(33).addNeighbors(new int[] {35,36});
		territories.get(34).addNeighbors(new int[] {36,37,40});
		territories.get(35).addNeighbors(new int[] {36,38,52});
		territories.get(36).addNeighbors(new int[] {37,38});
		territories.get(37).addNeighbors(new int[] {39,40,49});
		territories.get(38).addNeighbors(new int[] {48,52});
		territories.get(39).addNeighbors(new int[] {42,49});
		territories.get(40).addNeighbors(new int[] {27,41});
		territories.get(41).addNeighbors(new int[] {27,42});
		territories.get(42).addNeighbors(new int[] {27,43,44,45});
		territories.get(43).addNeighbors(new int[] {27,44});
		territories.get(44).addNeighbors(new int[] {45,46});
		territories.get(45).addNeighbors(new int[] {46,50});
		territories.get(46).addNeighbors(new int[] {50,51});
		territories.get(47).addNeighbors(new int[] {48,54});
		territories.get(48).addNeighbors(new int[] {49,52});
		territories.get(49).addNeighbors(new int[] {50});
		territories.get(50).addNeighbors(new int[] {51});
		territories.get(51).addNeighbors(new int[] {});
		territories.get(52).addNeighbors(new int[] {53,54});
		territories.get(53).addNeighbors(new int[] {54,55});
		territories.get(54).addNeighbors(new int[] {55});
		territories.get(55).addNeighbors(new int[] {});
		
		ImagesGenerator.generateMapImage(this, true, territories.size(), 0);
		
		checkColors();
	}
	
	/**
	 * Checks if all the colors from colors.txt are "different enough" to distinguish them
	 */
	private void checkColors() {
		for(Territory t : territories) {
			for(Territory u : territories) {
				if(!t.equals(u)) {
					int tr = t.color.getRed();
					int tg = t.color.getGreen();
					int tb = t.color.getBlue();
					int ur = u.color.getRed();
					int ug = u.color.getGreen();
					int ub = u.color.getBlue();
					
					if(tr >= ur-10 && tr <= ur+10 && tg >= ug-10 && tg <= ug+10 && tb >= ub-10 && tb <= ub+10) {
						System.err.println(t.name + " / " + u.name);
						System.err.println(String.format("#%02x%02x%02x", tr, tg, tb) + " / " + String.format("#%02x%02x%02x", ur, ug, ub)); 
					}
						
				}
			}
		}
	}
	
	/**
	 * Scheduled method, called at any iteration from the Core class
	 * 
	 * @return true if war is over
	 * @throws IOException
	 */
	public boolean run() throws IOException {
		List<Integer> alive = new ArrayList<>();
		List<Integer> dead = new ArrayList<>();
		
		for(Territory t : territories) {
			if(t.isDeath())
				dead.add(Integer.valueOf(t.id));
			else
				alive.add(Integer.valueOf(t.id));
		}
		
		//End game condition
		if(alive.size() == 1) {
			tWinner = territories.get(alive.get(0));
			
			MapSerializer.serializeMap(this, Map.FILE_TO_SERIALIZE);
			
			return true;
		}
		else if(alive.size() > 1) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			cal.add(Calendar.DATE, 1);
			date = cal.getTime();
			
			boolean insurrectionAttempt = dayFromLastInsurrection >= GameConstants.INSURRECTION_TIMER && Utils.randomInt(100) < GameConstants.INSURRECTION_PERCENT_CHANCE;
			
			if(insurrectionAttempt) {
				int winnerId = -1;
				
				Collections.shuffle(dead);
				for(int i = 0; i < dead.size(); i++) {
					Territory d = territories.get(dead.get(i));
					
					if(!d.hasInsurrect() && territories.get(d.owner).getUnderControl().size() >= 10) {
						winnerId = dead.get(i);
						break;
					}
				}
				
				if(winnerId != -1)
					insurrection(winnerId, alive, dead);
				else
					attack(alive, dead);
			}
			else
				attack(alive, dead);
			
			ImagesGenerator.generateMapImage(this, false, alive.size(), dead.size());
		}
		
		return false;
	}
	
	/**
	 * Executes an attack
	 * 
	 * @param alive list with all the alive territories id
	 * @param dead list with all the dead territories id
	 * 
	 * @throws IOException
	 */
	private void attack(List<Integer> alive, List<Integer> dead) throws IOException {
		dayFromLastInsurrection++;
		
		Territory winner = null;
		do {
			winner = territories.get(Utils.randomInt(territories.size()));
		} while(!winner.canAttack());
		
		Territory loser = winner.getRandomNeighborToAttack();
		Territory winnerOwner = territories.get(winner.owner);
		Territory loserOwner = territories.get(loser.owner);

		winnerOwner.addUnderControl(loser, loserOwner, alive, dead);
		
		ImagesGenerator.generateAttackImage(this, winner, loser, winnerOwner, loserOwner);
		Logger.writeAttackLog(this, alive, loser, winnerOwner, loserOwner, false);
	}
	
	/**
	 * Executes an insurrection
	 * 
	 * @param alive list with all the alive territories id
	 * @param dead list with all the dead territories id
	 * 
	 * @throws IOException
	 */
	private void insurrection(int winnerId, List<Integer> alive, List<Integer> dead) throws IOException {
		insurrections++;
		dayFromLastInsurrection = 0;
		
		Territory winnerOwner = territories.get(winnerId);
		Territory winner = winnerOwner;
		Territory loser = winnerOwner;
		Territory loserOwner = territories.get(winnerOwner.owner);
		
		winnerOwner.insurrect(loserOwner, alive, dead);
		
		ImagesGenerator.generateAttackImage(this, winner, loser, winnerOwner, loserOwner);
		Logger.writeAttackLog(this, alive, loser, winnerOwner, loserOwner, true);
	}
	
	
	public Territory get(int index) {
		return territories.get(index);
	}
	
	public Territory get(String name) {
		for(Territory t : territories) {
			if(t.name.equals(name))
				return t;
		}
		
		throw new ArrayIndexOutOfBoundsException("Didn't found territory with name: " + name);
	}	

	
}

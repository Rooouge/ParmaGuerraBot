package parmaguerrabot.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import parmaguerrabot.Utils;
import parmaguerrabot.map.Map;
import parmaguerrabot.map.Territory;

public class Logger {

	public static final File LOG_DIRECTORY = new File("log");
	public static final File MAP_LOG_FILE = new File(LOG_DIRECTORY.getPath() + "/map.log");
	public static final File GENERIC_LOG_FILE = new File(LOG_DIRECTORY.getPath() + "/generic.log");
	public static final File GAME_LOG_FILE = new File(LOG_DIRECTORY.getPath() + "/game.log");
	
	/**
	 * Inits the class
	 * 
	 * @param toInit true when begins a new game
	 * @throws IOException
	 */
	public static void init(boolean toInit) throws IOException {
		if(toInit) {
			if(!LOG_DIRECTORY.exists())
				LOG_DIRECTORY.mkdirs();
		
			Logger.genericLog("Log directory: " + LOG_DIRECTORY.getAbsolutePath());
			
			if(!GENERIC_LOG_FILE.exists() && GENERIC_LOG_FILE.createNewFile())
					throw new IOException("Error creating " + GENERIC_LOG_FILE.getName());
			if(!MAP_LOG_FILE.exists() && !MAP_LOG_FILE.createNewFile())
					throw new IOException("Error creating " + MAP_LOG_FILE.getName());
			if(!GAME_LOG_FILE.exists() && !GAME_LOG_FILE.createNewFile())
					throw new IOException("Error creating " + GAME_LOG_FILE.getName());
		}
	}
	
	/**
	 * Writes on "generic.log"
	 * 
	 * @param log
	 */
	public static void genericLog(String log) {
		try (
			BufferedWriter writer = new BufferedWriter(new FileWriter(GENERIC_LOG_FILE, true));
		) {
			writer.write(Utils.getFormattedMessage(log));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes on "map.log"
	 * 
	 * @param log
	 */
	public static void mapLog(String log) {
		try (
			BufferedWriter writer = new BufferedWriter(new FileWriter(MAP_LOG_FILE, true));
		) {
			writer.write(Utils.getFormattedMessage(log));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Writes on "game.log"
	 * 
	 * @param log
	 */
	public static void gameLog(String log) {
		try (
			BufferedWriter writer = new BufferedWriter(new FileWriter(GAME_LOG_FILE, true));
		) {
			writer.write(Utils.getFormattedMessage(log));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	/**
	 * Generates the attack/insurrection description and calls Utils.writeDescription() to 
	 * write it in "description.txt"
	 * 
	 * @param alive list with all the alive territories id
	 * @param winner
	 * @param loser
	 * @param winnerOwner
	 * @param loserOwner
	 * 
	 * @throws IOException
	 */
	public static void writeAttackLog(Map map, List<Integer> alive, Territory loser, Territory winnerOwner, Territory loserOwner, boolean insurrection) {
		String log = Map.DATE_FORMAT.format(map.date) + " - ";
		
		if(insurrection) {
			log += winnerOwner.name 
				+ " è insorto nei confronti di " 
				+ loserOwner.name;
		}
		else {
			log += winnerOwner.name + 
				" ha conquistato " + 
				loser.prefix.toLowerCase() + loser.name;
			
			if(loserOwner.id != loser.id) {
				log += " precedentemente occupato da "
					+ loserOwner.name;
			}
			if(loserOwner.isDeath())
				log += ". " + loserOwner.name + " è stato completamente sconfitto";
			
			if(alive.size() == 1)
				log += ". " + winnerOwner.name + " ha conquistato la provincia di Parma!";
		}
		

		Logger.gameLog(log);
		
		
		log += " " + Utils.getHashtags();
		Utils.writeDescription(log);
	}
}

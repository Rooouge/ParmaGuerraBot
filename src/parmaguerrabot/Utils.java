package parmaguerrabot;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import parmaguerrabot.log.Logger;

public class Utils {

	public static final String GAME_TIMESTAMP = getTimestamp();
	
	public static final File INITIALIZED_FILE = new File("res/files/initialized.txt");
	
	public static final File GAME_DIRECTORY = new File("game");
	public static final File MAPS_DIRECTORY = new File(GAME_DIRECTORY.getPath() + "/maps");
	public static final File ATTACKS_DIRECTORY = new File(MAPS_DIRECTORY.getPath() + "/attacks");
	public static final File DESCRIPTION_FILE = new File("game/description.txt");
	public static final File HASHTAGS_FILE = new File("res/files/hashtags.txt");
	
	private static final File colorFile = new File("res/files/colors.txt");
	private static List<Color> colors;
	private static String hashtags;
	
	private static Random random;
	
	/**
	 * Inits the class
	 * 
	 * @param toInit true when begins a new game
	 * @throws IOException
	 */
	public static void init(boolean toInit) throws IOException {
		if(toInit) {		
			if(!MAPS_DIRECTORY.exists())
				MAPS_DIRECTORY.mkdirs();
			if(!ATTACKS_DIRECTORY.exists())
				ATTACKS_DIRECTORY.mkdirs();
			if(!DESCRIPTION_FILE.exists() && !DESCRIPTION_FILE.createNewFile())
					throw new IOException("Error creating " + DESCRIPTION_FILE.getName());
			if(!HASHTAGS_FILE.exists() && !HASHTAGS_FILE.createNewFile())
					throw new IOException("Error creating " + HASHTAGS_FILE.getName());
			
			Logger.genericLog("Game directory: " + MAPS_DIRECTORY.getAbsolutePath());
		}
		
		random = new Random();
		
		colors = new ArrayList<>();
		
		try (
			BufferedReader br = new BufferedReader(new FileReader(colorFile));
		) {
			List<String> lines = br.lines().collect(Collectors.toList());
			
			for(String line : lines) {
				if(line.startsWith("#")) {
					Color c = new Color(
							Integer.valueOf(line.substring(1,3), 16),
							Integer.valueOf(line.substring(3,5), 16),
							Integer.valueOf(line.substring(5,7), 16));
					
					colors.add(c);
				}
			}
			
			if(colors.size() < 56)
				throw new IOException("Missing " + (56 - colors.size() + " color(s)"));
			if(colors.size() > 56)
				throw new IOException((colors.size() - 56) + " color(s) over");
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
		
		try (
			BufferedReader br = new BufferedReader(new FileReader(HASHTAGS_FILE));
		) {
			hashtags = br.readLine();
		} catch(IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public static String getHashtags() {
		return hashtags;
	}
	
	public static Color getRandomColor() {
		return colors.remove(Utils.randomInt(colors.size()));
	}
	
	/**
	 * Write the description in "description.txt". That text will be copied on the Facebook post
	 * 
	 * @param description
	 */
	public static void writeDescription(String description) {
		try (
				BufferedWriter writer = new BufferedWriter(new FileWriter(DESCRIPTION_FILE, false));
			) {
				writer.write(description);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	
	public static String getFormattedMessage(String message) {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
	    Date now = new Date();
	    String strDate = sdfDate.format(now);
	    
		return "[" + strDate + "] " + message + "\n";
	}
	
	public static String getTimestamp() {
		SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH_mm_ss_SSS");//dd/MM/yyyy
	    Date now = new Date();
	    
	    return sdfDate.format(now);
	}
	
	public static int randomInt(int bound) {
		return random.nextInt(bound);
	}
	
	/**
	 * Calculates the number of days between 01/01/2020 and end date
	 * 
	 * @param end
	 * @return
	 */
	public static int getDifferenceInDaysFromFirstDay(Date end) {
		Calendar c = Calendar.getInstance();
		c.setTime(end);
		
		int years = c.get(Calendar.YEAR) - 2020;
		int months = c.get(Calendar.MONTH) + (12 * years);
		int days = c.get(Calendar.DATE);
		
		int y = 0;
		
		for(int i = 0; i < months && y <= years; i++) {
			if(i == 1 + (y*12)) {
				days += 28;
				
				if(y % 4 == 0)
					days++;
			}
			else if(i == 3 + (y*12) || i == 5 + (y*12) || i == 8 + (y*12) || i== 10 + (y*12))
				days += 30;
			else
				days += 31;
			
			if(i == (y+1) * 12 - 1)
				y++;
		}
		
		return days;
	}
	
}

package parmaguerrabot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import parmaguerrabot.log.Logger;
import parmaguerrabot.map.Map;
import parmaguerrabot.serialize.MapSerializer;

public class Core {

	public static void init() throws IOException {
		Logger.init(true);
		Utils.init(true);
		Logger.genericLog("Succesfully initialized Utils class");
		
		Map map = new Map();
		map.init();
		Logger.genericLog("Succesfully initialized Map class");
		
		MapSerializer.serializeMap(map, Map.FILE_TO_SERIALIZE);
	}

	public static boolean run() throws IOException {
		Logger.init(false);
		Utils.init(false);
		Logger.genericLog("Succesfully initialized Utils class");
		
		Map map = MapSerializer.deserializeMap(Map.FILE_TO_SERIALIZE);
		
		boolean gameOver = map.run();

		MapSerializer.serializeMap(map, Map.FILE_TO_SERIALIZE);
		
		return gameOver;
	}
	
	public static boolean isInitialized() throws IOException {		
		try (
			FileReader fr = new FileReader(Utils.INITIALIZED_FILE);
		) {
			int initialized = fr.read();
			if(initialized == '0') {
				try (
					FileWriter fw = new FileWriter(Utils.INITIALIZED_FILE);
				) {
					fw.write("1");
				}
			}
			
			return initialized != '0';
		}
	}
	
	public static void reset() throws IOException {
		Core.deleteIfExists(Utils.GAME_DIRECTORY);
		Core.deleteIfExists(Logger.LOG_DIRECTORY);
		
		try (
			FileWriter fw = new FileWriter(Utils.INITIALIZED_FILE);
		) {
			fw.write("0");
		}
	}
	
	private static void deleteIfExists(File file) throws IOException {
		if(file.exists()) {
			if(file.isDirectory()) {
				File[] list = file.listFiles();
				
				for(File f : list) {
					Core.deleteIfExists(f);
				}
			}
			
			Files.delete(file.toPath());
		}
	}

}

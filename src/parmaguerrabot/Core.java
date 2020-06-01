package parmaguerrabot;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import parmaguerrabot.log.Logger;
import parmaguerrabot.map.Map;
import parmaguerrabot.serialize.Serializer;
import parmaguerrabot.serialize.json.JSONConverter;

public class Core {

	public static final int SERIALIZATION = 0;
	public static final int JSON = 1;
	
	/**
	 * Specifies if map & territories info have to be saved in json files or in map.sav (default = json)
	 */
	private static int fileSaveSystem = 1;
	
	
	public static void setFileSaveSystem(int type) {
		Core.fileSaveSystem = type;
	}
	
	public static void init() throws IOException {
		Logger.init(true);
		Utils.init(true);
		Logger.genericLog("Succesfully initialized Utils class");
		
		Map map = new Map();
		map.init();
		Logger.genericLog("Succesfully initialized Map class");
		
		switch (Core.fileSaveSystem) {
		case Core.SERIALIZATION:
			Serializer.serializeMap(map, Map.SERIALIZATION_FILE);
			break;
		case Core.JSON:
			JSONConverter.writeJSONFiles(map);
			break;
		default:
			throw new IOException("Invalid file saving system: " + Core.fileSaveSystem);
		}
		
	}

	public static boolean run() throws IOException {
		Logger.init(false);
		Utils.init(false);
		Logger.genericLog("Succesfully initialized Utils class");
		
		Map map = null;
		
		switch (Core.fileSaveSystem) {
		case Core.SERIALIZATION:
			map = Serializer.deserializeMap(Map.SERIALIZATION_FILE);
			break;
		case Core.JSON:
			map = JSONConverter.readMapJSON();
			break;
		default:
			throw new IOException("Invalid file saving system: " + Core.fileSaveSystem);
		}
		
		boolean gameOver = map.run();

		switch (Core.fileSaveSystem) {
		case Core.SERIALIZATION:
			Serializer.serializeMap(map, Map.SERIALIZATION_FILE);
			break;
		case Core.JSON:
			JSONConverter.writeJSONFiles(map);
			break;
		default:
			throw new IOException("Invalid file saving system: " + Core.fileSaveSystem);
		}
		
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
	
	
	
	private Core() {}

}

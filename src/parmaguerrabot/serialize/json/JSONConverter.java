package parmaguerrabot.serialize.json;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import parmaguerrabot.map.Map;
import parmaguerrabot.map.Territory;

public class JSONConverter {

	private static final Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	
	private JSONConverter() { }
	
	
	public static void writeJSONFiles(Map map) throws IOException {		
		try (
			FileWriter writer = new FileWriter(Map.JSON_FILE)
		) {
			String json;
			
			writer.write("{\n");
			
			json = gson.toJson(map.getJSONObject()).replace("map", "Map");
			json = json.substring(2, json.length()-2);
			
			writer.write(json + "");
			
			for(Territory t : map.territories) {
				json = gson.toJson(t.getJSONObject()).replace("map", t.name);
				json = json.substring(2, json.length()-2);
				
				writer.write(",\n" + json);
			}
			
			writer.write("\n}");
		} catch (IllegalAccessException e) {
			throw new IOException(e.fillInStackTrace());
		}
	}
	
	public static Map readMapJSON() throws IOException {
		Map map = new Map();
		map.addTerritories();
		
		String json = new String(Files.readAllBytes(Paths.get(Map.JSON_FILE)));
		
		String mapString = "{\n";
		mapString += json.substring(json.indexOf("  \"Map\": {"));
		mapString = mapString.substring(0, mapString.indexOf('}') + 1);
		mapString += "\n}";
		mapString = mapString.replaceFirst("Map", "map");
		
		JSONObject mapJSON = gson.fromJson(mapString, JSONObject.class);
		mapJSON.parseJSONObject(map);
		
		for(Territory t : map.territories) {
			String tString = "{\n";
			tString += json.substring(json.indexOf("  \"" + t.name + "\": {"));
			tString = tString.substring(0, tString.indexOf("\n  }") + 4);
			tString += "\n}";
			tString = tString.replaceFirst(t.name, "map");
			
			JSONObject tJSON = gson.fromJson(tString, JSONObject.class);
			
			tJSON.parseJSONObject(t);
		}
		
		return map;
	}
}

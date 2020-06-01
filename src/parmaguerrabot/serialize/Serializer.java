package parmaguerrabot.serialize;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import parmaguerrabot.map.Map;

public class Serializer {

	private Serializer() {}
	
	public static void serializeMap(Map map, String fileName) throws IOException {
		File mapFile = new File(fileName);
		
		try (
			FileOutputStream fos = new FileOutputStream(mapFile);
			ObjectOutputStream oos = new ObjectOutputStream(fos);
		) {
			oos.writeObject(map);
		}
	}
	
	
	public static Map deserializeMap(String fileName) throws IOException {
		File file = new File(fileName);
		if(!file.exists())
			throw new IOException("file " + file.getAbsolutePath() + " didn't found");
		
		Map map = null;
		
		try (
			FileInputStream fis = new FileInputStream(file);
			ObjectInputStream ois = new ObjectInputStream(fis);
		) {
			map = (Map)ois.readObject();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			throw new IOException("Failed to deserialize " + file.getName());
		}
		
		if(map != null) 
			return map;
		
		throw new IOException("map null");
	}
	
}

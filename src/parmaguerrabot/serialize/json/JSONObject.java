package parmaguerrabot.serialize.json;

import java.awt.Color;
import java.awt.Point;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import parmaguerrabot.Utils;

public class JSONObject {

	private Map<String, Object> map;
	
	public JSONObject() {
		map = new HashMap<>();
	}
	
	public void put(String key, Object value) {
		map.put(key, value);
	}
	
	public Object get(String key) {
		return map.get(key);
	}
	
	public void parseJSONObject(Object dest) throws IOException {
		Class<?> clazz = dest.getClass();
		
		List<String> fields = (Arrays.stream(clazz.getFields())
				.map(n -> n.getName()))
				.collect(Collectors.toList());
		
		for(Map.Entry<String, Object> entry : map.entrySet()) {
			String key = entry.getKey();
			
			if(fields.contains(key)) {
				int index = fields.indexOf(key);
				
				try {
					Field f = clazz.getField(fields.get(index));
					
					if(f.getType().isAssignableFrom(int.class)) {
//						System.out.println("int");
						double d = (double) entry.getValue();
						int value = (int) d;
						f.set(dest, value);
					}
					else if(f.getType().isAssignableFrom(Date.class)) {
//						System.out.println("Date");
						Date date = Utils.DATE_FORMAT.parse((String) entry.getValue());
						f.set(dest, date);
					}
					else if(f.getType().isAssignableFrom(Color.class)) {
//						System.out.println("Color");
						double d = (double) entry.getValue();
						int rgb = (int) d;
						f.set(dest, new Color(rgb));
					}
					else if(f.getType().isAssignableFrom(List.class)) {
//						System.out.println("List");
						@SuppressWarnings("unchecked")
						List<Integer> list = ((List<Double>) entry.getValue())
								.stream()
								.map(i -> i.intValue())
								.collect(Collectors.toList());
						
						f.set(dest, list);
					}
					else if(f.getType().isAssignableFrom(Point.class)) {
//						System.out.println("Point");
						String values = entry.getValue().toString();
						int x = Integer.parseInt(values.substring(values.indexOf("x=") + 2, values.indexOf(".0,")));
						int y = Integer.parseInt(values.substring(values.indexOf("y=") + 2, values.indexOf(".0}")));
						
						f.set(dest, new Point(x, y));
					}
					else {
//						System.out.println(entry.getValue().getClass().getName());
						f.set(dest, entry.getValue());
					}
				} catch (Exception e) {
					throw new IOException(e.fillInStackTrace());
				}
			}
		}
	}
}

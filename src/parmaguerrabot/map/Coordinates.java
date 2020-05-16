package parmaguerrabot.map;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Coordinates implements Serializable {

	public final int x;
	public final int y;
	
	public Coordinates(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object c) {
		if(c instanceof Coordinates) {
			Coordinates c2 = (Coordinates) c;
			
			return (x == c2.x && y == c2.y);
		}
		return false;
	}
}

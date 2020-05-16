package parmaguerrabot.images;

import javax.swing.ImageIcon;

public class Images {

	public static final ImageIcon MAP_IMAGE = new ImageIcon("res/images/map.png");
	public static final ImageIcon BORDER_LAYER = new ImageIcon("res/images/border_layer.png");
	
	
	public static ImageIcon getTerritoryImage(String territoryName) {
		return new ImageIcon("res/images/territories/" + territoryName.toLowerCase().replace(' ', '_') + ".png");
	}
}

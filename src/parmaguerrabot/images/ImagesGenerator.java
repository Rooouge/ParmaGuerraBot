package parmaguerrabot.images;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import parmaguerrabot.Utils;
import parmaguerrabot.log.Logger;
import parmaguerrabot.map.Map;
import parmaguerrabot.map.Territory;

public class ImagesGenerator {
	
	/**
	 * Called to generate the Map image and writes in "description.txt" the the begin of the war phrase
	 * 
	 * @param firstRound 
	 * 
	 * @throws IOException
	 */
	public static void generateMapImage(Map map, boolean firstRound, int alives, int deads) throws IOException {
		File outputMap;
		
		BufferedImage newMap = new BufferedImage(Images.MAP_IMAGE.getIconWidth(), Images.MAP_IMAGE.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = newMap.getGraphics();
		
		Images.MAP_IMAGE.paintIcon(null, g, 0, 0);
		
		for(Territory t : map.territories) {
			paintTerritoryImage(g, t, map.territories.get(t.owner).color);
		}
		
		Images.BORDER_LAYER.paintIcon(null, g, 0, 0);
		paintScoreboard(map, g);
		
		g.drawString("In vita : " + alives + "   ---   Sconfitti : " + deads, 100, 875);
		
		
		String mapImagePath = Utils.MAPS_DIRECTORY.getAbsolutePath();
		
		outputMap = new File(mapImagePath + "/map_" + new SimpleDateFormat("dd-MM-yyyy").format(map.date) + ".png");
		ImageIO.write(newMap, "png", outputMap);
		
		
		if(firstRound) {
			Logger.mapLog("");
			
			String log = Map.DATE_FORMAT.format(map.date) + " - La guerra ha inizio! " + Utils.getHashtags();
			
			Utils.writeDescription(log);
		}
	}
	
	/**
	 * Called to generate the Attack image
	 * 
	 * @param alive list with all the alive territories id
	 * @param winner
	 * @param loser
	 * @param winnerOwner
	 * @param loserOwner
	 * 
	 * @throws IOException
	 */
	public static void generateAttackImage(Map map, Territory winner, Territory loser, Territory winnerOwner, Territory loserOwner) throws IOException {
		File outputAttackMap;
		
		BufferedImage attackMap = new BufferedImage(Images.MAP_IMAGE.getIconWidth(), Images.MAP_IMAGE.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = attackMap.getGraphics();
		
		Images.MAP_IMAGE.paintIcon(null, g, 0, 0);
		
		for(Territory t : map.territories) {
			if(!t.equals(loser)) {
				if(winnerOwner.getUnderControl().contains(t.id) || t.equals(winner))
					paintTerritoryImage(g, t, Color.green);
				else if(loserOwner.getUnderControl().contains(t.id))
					paintTerritoryImage(g, t, Color.red);
				else
					paintTerritoryImage(g, t, new Color(200,200,200));
			}
			else {
				paintTerritoryImage(g, t, new Color(0,128,0));
			}	
		}
		
		Images.BORDER_LAYER.paintIcon(null, g, 0, 0);
		paintAttackInfo(g, winnerOwner, loserOwner, loser);
		
		String attackImagePath = Utils.ATTACKS_DIRECTORY.getAbsolutePath();
		
		outputAttackMap = new File(attackImagePath + "/attacks_" + new SimpleDateFormat("dd-MM-yyyy").format(map.date) + ".png");
		ImageIO.write(attackMap, "png", outputAttackMap);
	}
	
	
	/**
	 * Paints the territory image with the specified color on the graphics object
	 * 
	 * @param g
	 * @param t
	 * @param color
	 */
	private static void paintTerritoryImage(Graphics g, Territory t, Color color) {
		g.setColor(color);
		
		ImageIcon tImage = Images.getTerritoryImage(t.name);
		
		BufferedImage image = new BufferedImage(tImage.getIconWidth(), tImage.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
		Graphics imageG = image.getGraphics();
		tImage.paintIcon(null, imageG, 0, 0);
		
		
		for(int y = 0; y < tImage.getIconHeight(); y++) {
			for(int x = 0; x < tImage.getIconWidth(); x++) {
				int pixel = image.getRGB(x, y);
				if((pixel >> 24) != 0x00)
					g.fillRect(t.coordinates.x + x, t.coordinates.y + y, 1, 1);
			}
		}
	}
	
	/**
	 * Paints the scoreboard on the Graphics object
	 * 
	 * @param g
	 */
	private static void paintScoreboard(Map map, Graphics g) {
		List<Territory> scores = new ArrayList<>();
		
		for(Territory t : map.territories) {
			if(!t.isDeath())
				scores.add(t);
		}
		
		Collections.sort(scores);
		
		Color generic = Color.white;
		
		g.setColor(generic);
		g.setFont(new Font("SansSerif", Font.BOLD, 50));
		g.drawString("   " + Map.DATE_FORMAT.format(map.date), 100, 100);
		g.drawString("--------------------", 100, 150);
		
		for(int i = 0; i < 8 && i < scores.size(); i++) {
			Territory t = scores.get(i);
			
			int y = 200 + i*75;
			
			g.drawString((i+1) + ")", 100, y);
			
			g.setColor(t.color);
			g.fillRect(200, y-40, 50, 50);
			g.setColor(Color.black);
			g.drawRect(200, y-40, 50, 50);
			
			g.setColor(generic);
			g.drawString(t.name + " (" + t.getScore() + ")", 300, y);
		}
	}

	/**
	 * Paints the attack information label on the Graphics object
	 * 
	 * @param g
	 * @param winnerOwner
	 * @param loserOwner
	 * @param loser
	 */
	private static void paintAttackInfo(Graphics g, Territory winnerOwner, Territory loserOwner, Territory loser) {
		Color generic = Color.white;
		g.setFont(new Font("SansSerif", Font.BOLD, 50));
		
		g.setColor(Color.green);
		g.fillRect(100, 160, 50, 50);
		g.setColor(Color.black);
		g.drawRect(100, 160, 50, 50);
		
		g.setColor(generic);
		g.drawString("Attaccante: " + winnerOwner.name, 200, 200);
		
		g.setColor(new Color(0, 128, 0));
		g.fillRect(100, 160 + 75, 50, 50);
		g.setColor(Color.black);
		g.drawRect(100, 160 + 75, 50, 50);

		g.setColor(generic);
		g.drawString("Conquistato: " + loser.name, 200, 200 + 75);
		if(loser.id != loserOwner.id) 
			g.drawString("(Occupato da " + loserOwner.name + ")", 200, 200 + 150);
		
		if(!loserOwner.isDeath()) {
			g.setColor(Color.red);
			g.fillRect(100, 160 + 225, 50, 50);
			g.setColor(Color.black);
			g.drawRect(100, 160 + 225, 50, 50);

			g.setColor(generic);
			g.drawString("Sconfitto: " + loserOwner.name, 200, 200 + 225);
		}
	}
}

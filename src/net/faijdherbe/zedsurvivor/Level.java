package net.faijdherbe.zedsurvivor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Level {
	private int[] levelData;
	private Dimension levelSize;
	private Dimension tileSize = new Dimension(16,16);
	private BufferedImage levelTiles;
	
	public Level(String filename) {
		this.loadAndParse(filename);
	}
	
	
	private void loadAndParse(String filename) {
		BufferedImage levelImage = null;
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream input = loader.getSystemResourceAsStream(filename);
			levelImage = ImageIO.read(input);
			
			input = loader.getSystemResourceAsStream("level1Graphics.bmp");
			levelTiles = ImageIO.read(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(levelImage == null) {
			return;
		}
		levelSize = new Dimension(levelImage.getWidth(), levelImage.getHeight());
		
		levelData = ((DataBufferInt) levelImage.getRaster().getDataBuffer()).getData();
		
		System.out.println("loaded level: "+ levelData.length + " - " + levelSize.toString());
	}
	
	public Point getSpawnLocation() {
		
		for(int i = 0; i < levelData.length; i++) {
			
			if((levelData[i]&Level.Data.PLAYER_SPAWN) == Level.Data.PLAYER_SPAWN) {
				return new Point(i % levelSize.width, (int)Math.floor(i / (float)levelSize.width));
			}
		}
		
		return null;
	}
	
	public void render(Graphics2D g, float _x, float _y) {
		
		int cols = levelTiles.getWidth() / tileSize.width;
		
		
		for(int x = 0; x < levelSize.width; x++) {
			for(int y = 0; y < levelSize.height; y++) {

				float __x = x - (_x / 10.0f);
				float __y = y - (_y / 10.0f);
				
				int tileIndex = tileIndexForLevelData(levelData[y*levelSize.width + x]);
				Point tilePoint = new Point((tileIndex%cols)*tileSize.width, (tileIndex/cols) * tileSize.height);
				
				g.drawImage(levelTiles, 
						Math.round(__x*tileSize.width), Math.round(__y*tileSize.height), Math.round(__x*tileSize.width+tileSize.width), Math.round(__y*tileSize.height+tileSize.height), 
						tilePoint.x, tilePoint.y, tilePoint.x + tileSize.width, tilePoint.y + tileSize.height, 
						null);
			}
		}
	}
	

	class Data {
		public static final int PLAYER_SPAWN = 0x00FF00;
	}
	class Tile {
		public static final int SOLID 				= 0x00;
		public static final int ROAD 				= 0xBF;
		public static final int PAVEMENT_WEST 		= 0x7E;
		public static final int PAVEMENT_EAST 		= 0x81;
		public static final int PAVEMENT_NORTH 		= 0x7F;
		public static final int PAVEMENT_SOUTH 		= 0x80;
		public static final int PAVEMENT_CORNER_NE 	= 0x83;
		public static final int PAVEMENT_CORNER_NW 	= 0x82;
		public static final int PAVEMENT_CORNER_SW 	= 0x84;
		public static final int PAVEMENT_CORNER_SE 	= 0x85;
		
	}
	
	private int tileIndexForLevelData(int data) {
		int i = 99;
		switch (data&0xFF) {
		case Tile.SOLID:
			i= 5;
			break;

		case Tile.PAVEMENT_WEST:
			i = 0;
			break;
		case Tile.PAVEMENT_NORTH:
			i = 2;
			break;
		case Tile.PAVEMENT_SOUTH:
			i = 3;
			break;
		case Tile.PAVEMENT_EAST:
			i = 1;
			break;
			

		case Tile.PAVEMENT_CORNER_SW: //sw
			i = 10;
			break;
		case Tile.PAVEMENT_CORNER_SE: //se
			i = 11;
			break;
		case Tile.PAVEMENT_CORNER_NW: //nw
			i = 12;
			break;
		case Tile.PAVEMENT_CORNER_NE: //ne
			i = 13;
			break;
			
			
		case Tile.ROAD:
			i = 4;
			break;

		default :
			i= 99;
		}
		
		return i;
		
	}
	
}

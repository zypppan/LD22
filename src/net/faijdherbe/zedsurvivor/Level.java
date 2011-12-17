package net.faijdherbe.zedsurvivor;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.vecmath.Point2f;

public class Level {
	private int[] levelData;
	private Dimension levelSize;
	private Dimension tileSize = new Dimension(16,16);
	private BufferedImage levelTiles;
	
	int [] tileMapping = null;
	
	public Level(String filename) {
		tileMapping = new int[0xFF];
		tileMapping[0x00] = 5;
		tileMapping[0xBF] = 4;
		tileMapping[0x7E] = 0;
		tileMapping[0x81] = 1;
		tileMapping[0x7F] = 2;
		tileMapping[0x80] = 3;
		tileMapping[0x83] = 13;
		tileMapping[0x82] = 12;
		tileMapping[0x84] = 10;
		tileMapping[0x85] = 11;
		this.loadAndParse(filename);
		
		items[0] = new Item();
		items[0].origin = new Point2f(10.0f,10.0f);
	}
	
	Item [] items = new Item[256];
	
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
	
	public Point2f getSpawnLocation() {
		
		for(int i = 0; i < levelData.length; i++) {
			
			if((levelData[i]&Data.PLAYER_SPAWN_MASK) == Data.PLAYER_SPAWN_MASK) {

				return new Point2f((float)(i % levelSize.width), (float)Math.floor(i / (float)levelSize.width));
			}
		}
		
		return null;
	}
	
	public void render(Graphics2D g, float _x, float _y) {
		
		int cols = levelTiles.getWidth() / tileSize.width;
		
		
		for(int x = 0; x < levelSize.width; x++) {
			for(int y = 0; y < levelSize.height; y++) {

				float __x = x - (_x);
				float __y = y - (_y);
				
				int tileIndex = tileIndexForLevelData(levelData[y*levelSize.width + x]);
				Point tilePoint = new Point((tileIndex%cols)*tileSize.width, (tileIndex/cols) * tileSize.height);
				
				g.drawImage(levelTiles, 
						Math.round(__x*tileSize.width), Math.round(__y*tileSize.height), Math.round(__x*tileSize.width+tileSize.width), Math.round(__y*tileSize.height+tileSize.height), 
						tilePoint.x, tilePoint.y, tilePoint.x + tileSize.width, tilePoint.y + tileSize.height, 
						null);
			}
		}
		
		for (Item item : items) {
			if(item != null) {
				g.drawImage(item.getImage(), Math.round((item.origin.x - _x)*tileSize.width), Math.round((item.origin.y - _y)*tileSize.height), null);
			}
		}
	}
	

	class Data {
		public static final int PLAYER_SPAWN_MASK = 0xff00ff00;
	}

	
	private int tileIndexForLevelData(int data) {
		
		
		
		return tileMapping[data&0xFF];
		
	}
	
}

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
	
	class Data {
		public static final int PLAYER_SPAWN = 0x00FF00;
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

				int __x = Math.round(x - (_x / 10.0f));
				int __y = Math.round(y - (_y / 10.0f));
				
				int tileIndex = tileIndexForLevelData(levelData[y*levelSize.width + x]);
				Point tilePoint = new Point((tileIndex%cols)*tileSize.width, (tileIndex/cols) * tileSize.height);
				
				g.drawImage(levelTiles, 
						__x*tileSize.width, __y*tileSize.height, __x*tileSize.width+tileSize.width, __y*tileSize.height+tileSize.height, 
						tilePoint.x, tilePoint.y, tilePoint.x + tileSize.width, tilePoint.y + tileSize.height, 
						null);
			}
		}
	}
	
	private int tileIndexForLevelData(int data) {
		int i = 99;
		switch (data&0xFFFFFF) {
		case 0x00:
			i= 5;
			break;

		case 0x6B6B6B:
			i = 0;
			break;
		case 0x6C6C6C:
			i = 2;
			break;
		case 0x6D6D6D:
			i = 3;
			break;
		case 0x6E6E6E:
			i = 1;
			break;
			

		case 0x717171:
			i = 10;
			break;
		case 0x727272:
			i = 11;
			break;
		case 0x737373:
			i = 12;
			break;
		case 0x747474:
			i = 13;
			break;
			
			
		case 0xB1B1B1:
			i = 4;
			break;

		default :
			i= 99;
		}
		
		return i;
		
	}
	
}

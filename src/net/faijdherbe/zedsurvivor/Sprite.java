package net.faijdherbe.zedsurvivor;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.vecmath.Point2f;

public class Sprite {

	private BufferedImage spriteImage = null;
	
	public Point2f origin;
	
	public static final int ALPHA_MASK = 0xFFFF00FF;
	
	public class Direction {
		public static final int NONE = 0;
		public static final int LEFT = 1;
		public static final int RIGHT = 2;
		public static final int UP = 3;
		public static final int DOWN = 4;
	}
	
	public Sprite(String filename) {
		loadImage(filename);
	}
	
	private void loadImage(String filename) {
		try {
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream input = loader.getSystemResourceAsStream(filename);
			BufferedImage tmpImage = ImageIO.read(input);
			
			spriteImage = new BufferedImage(tmpImage.getWidth(), tmpImage.getHeight(), BufferedImage.TYPE_INT_ARGB);
			spriteImage.getGraphics().drawImage(tmpImage, 0, 0, null);
			
			int [] imgData = ((DataBufferInt) spriteImage.getRaster().getDataBuffer()).getData();
			
			for(int i = 0; i < imgData.length; i++ ) {
				if(imgData[i] == ALPHA_MASK) {
					imgData[i] = 0x00000000;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void update(long timeSinceLastUpdate) {
		
	}
	
	public BufferedImage getImage() {
		return spriteImage;
	}
}

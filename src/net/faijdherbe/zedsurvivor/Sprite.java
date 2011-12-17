package net.faijdherbe.zedsurvivor;

import java.awt.image.BufferedImage;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class Sprite {
	
	private float xLocation, yLocation;
	private BufferedImage spriteImage = null;
	
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
			spriteImage = ImageIO.read(input);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public float getxLocation() {
		return xLocation;
	}

	public void setxLocation(float xLocation) {
		this.xLocation = xLocation;
	}

	public float getyLocation() {
		return yLocation;
	}

	public void setyLocation(float yLocation) {
		this.yLocation = yLocation;
	}

	public void update(long timeSinceLastUpdate) {
		
	}
	
	public BufferedImage getImage() {
		return spriteImage;
	}
}

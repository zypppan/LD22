package net.faijdherbe.zedsurvivor;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

import net.java.games.input.Controller;
import net.java.games.input.ControllerEnvironment;
import net.java.games.input.Event;
import net.java.games.input.EventQueue;

public class Engine extends Thread {
	private int canvasWidth, canvasHeight, canvasScale;
	
	private JFrame window = null;
	private Canvas canvas = null;
	
	private boolean isRunning;
	private BufferedImage renderBuffer;
	private BufferStrategy renderStrategy;
	
	private Graphics2D canvasGraphics, bufferGraphics;


	public Engine(int width, int height, int scale) {
		canvasWidth = width;
		canvasHeight = height;
		canvasScale = scale;
		 
		window = new JFrame("ZedSurvivor");
		window.addWindowListener(new WindowAdapter() {
			public void windowClosing(final WindowEvent e) {
				isRunning = false;
			}
		});
		window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		window.setSize(width*scale, height*scale);
		window.setLocationRelativeTo(null);
		window.setVisible(true);
		window.setResizable(false);
		
		canvas = new Canvas();
		canvas.setSize(width*scale, height*scale);
		window.add(canvas, 0);
		
		renderBuffer = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		canvas.createBufferStrategy(2);
		do {
			renderStrategy = canvas.getBufferStrategy();
		} while(renderStrategy == null);
	}

	public int getCanvasWidth() {
		return canvasWidth;
	}

	public int getCanvasHeight() {
		return canvasHeight;
	}

	public int getCanvasScale() {
		return canvasScale;
	}
	
	public void run() {
		isRunning = true;
		long fpsWait = (long) (1.0/30 * 1000);
		double currentFPS = 0;
		long lastFrameTime = 0 , timeSinceLastFrame = 0;
		while(isRunning) {
			long renderStart = System.nanoTime();
			
			Graphics2D renderGraphics = getBufferGraphics();
			
			pollInput();
			updateGameLoop(timeSinceLastFrame);
			renderGameLoop(renderGraphics);
			postProcess(renderBuffer);
			renderHUD(renderGraphics);
			
			Graphics2D canvasGraphics = getCanvasGraphics();
			if(canvasGraphics != null) {
				canvasGraphics.drawImage(renderBuffer, 0,0, canvasWidth*canvasScale,canvasHeight*canvasScale, null);
				canvasGraphics.setColor(Color.black);
				canvasGraphics.fillRect(0, 0, canvasWidth, 17);
				canvasGraphics.setColor(Color.white);
				canvasGraphics.drawString(String.format("fps: %d", Math.round(currentFPS)), 5,15);
			}
			renderStrategy.show();
			
			long renderTime = (System.nanoTime() - renderStart)/ 1000000;
			try {
				Thread.sleep(Math.max(0,  fpsWait-renderTime));
			} catch (InterruptedException e) {
				Thread.interrupted();
				break;
			}
			currentFPS = (currentFPS * 0.9f) + (1000.f / ((renderStart-lastFrameTime)/1000000) *0.1);
			lastFrameTime = renderStart;
			timeSinceLastFrame = System.nanoTime() - renderStart;
		}
	}
	
	/* engine stuff */
	
	public boolean updateScreen() {
		canvasGraphics.dispose();
		canvasGraphics = null;
		

		try {
			renderStrategy.show();
			Toolkit.getDefaultToolkit().sync();
			return (!renderStrategy.contentsLost());
		} catch (NullPointerException e) {
			return true;
		} catch (IllegalStateException e) {
			return true;
		}
	}
	
	
	public Graphics2D getCanvasGraphics() {
		if(canvasGraphics == null) {
			canvasGraphics = (Graphics2D)renderStrategy.getDrawGraphics();
		}
		return canvasGraphics;
	}

	public Graphics2D getBufferGraphics() {
		if(bufferGraphics == null) {
			bufferGraphics = (Graphics2D)renderBuffer.getGraphics();
		}
		return bufferGraphics;
	}

	public void pollInput() {
		Controller[] controllers = ControllerEnvironment.getDefaultEnvironment().getControllers();
		for (Controller controller : controllers) {
			controller.poll();
			EventQueue queue = controller.getEventQueue();
			Event event = new Event();
			while (queue.getNextEvent(event)) {
				handleInputEvent(controller, event);
			}
		}
		
	}
	
	/* overrideables */
	public void updateGameLoop(long timeSinceLastFrame) {}
	public void renderGameLoop(Graphics2D g) {}
	public void postProcess(BufferedImage buffer) {}
	public void renderHUD(Graphics2D g) {}
	public void handleInputEvent(Controller controller, Event event){}
}

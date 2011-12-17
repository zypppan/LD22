package net.faijdherbe.zedsurvivor;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import javax.vecmath.Point2f;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Event;

public class ZedSurvivor extends Engine {

	Player player = null;
	Level level = null;
	float time = 0;
	
	public ZedSurvivor() {
		super(160,144,3);
		
		level = new Level("level1.bmp");
		
		player = new Player();
		Point2f spawn = level.getSpawnLocation();
		if(spawn != null) {
			player.origin=spawn;
		}
		start();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		new ZedSurvivor();
	}

	@Override
	public void updateGameLoop(long timeSinceLastFrame) {
		// TODO Auto-generated method stub
		super.updateGameLoop(timeSinceLastFrame);
		
		player.update(timeSinceLastFrame);
	}
	
	@Override
	public void renderGameLoop(Graphics2D g) {
		// TODO Auto-generated method stub
		super.renderGameLoop(g);
		
		Point levelRenderOffset = new Point((int)Math.round((this.getCanvasWidth()/16) * 0.5), (int)Math.round((this.getCanvasHeight()/16) * 0.5)) ;
		
		level.render(g, player.origin.x-levelRenderOffset.x, player.origin.y-levelRenderOffset.y);
		g.drawImage(player.getImage(), 72, 66, null);
		
	}
	
	
	public void handleInputEvent(Controller controller, Event event){
		Component component = event.getComponent();
		
			
		if(component.getIdentifier() == Component.Identifier.Axis.POV) {
			int value = (int)Math.round(event.getValue() * 1000.0);

			switch(value) {
			case 1000:
				player.walk(Sprite.Direction.LEFT);
				break;
			case 250:
				player.walk(Sprite.Direction.UP);
				break;
			case 500:
				player.walk(Sprite.Direction.RIGHT);
				break;
			case 750:
				player.walk(Sprite.Direction.DOWN);
				break;
			case 0:
				player.walk(Sprite.Direction.NONE);
				break;
			}
			return;
		}
		
		
	}

	@Override
	public void postProcess(BufferedImage buffer) {
		// TODO Auto-generated method stub
		super.postProcess(buffer);
		
		byte [] data = ((DataBufferByte) buffer.getRaster().getDataBuffer()).getData();
		
		
		
		for(int i = 0; i < data.length; i++) {

		}
	}
	
}

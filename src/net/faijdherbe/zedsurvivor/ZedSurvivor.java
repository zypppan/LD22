package net.faijdherbe.zedsurvivor;

import java.awt.Graphics2D;
import java.awt.Point;

import net.java.games.input.Component;
import net.java.games.input.Component.Identifier;
import net.java.games.input.Controller;
import net.java.games.input.Event;

public class ZedSurvivor extends Engine {

	Player player = null;
	Level level = null;
	
	public ZedSurvivor() {
		super(160,144,3);
		
		level = new Level("level1.bmp");
		
		player = new Player();
		Point spawn = level.getSpawnLocation();
		if(spawn != null) {
			player.setxLocation(spawn.x);
			player.setyLocation(spawn.y);
			System.out.println(spawn.toString());
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
		
		level.render(g, player.getxLocation(), player.getyLocation());
		g.drawImage(player.getImage(), 72, 66, null);
		
	}
	
	
	public void handleInputEvent(Controller controller, Event event){
		Component component = event.getComponent();
		
		if(event.getValue() == 1.0f) {
			Identifier cID = component.getIdentifier();
			if(cID == Component.Identifier.Key.LEFT) {
				player.walk(Sprite.Direction.LEFT);
			} else if(cID == Component.Identifier.Key.RIGHT) {
				player.walk(Sprite.Direction.RIGHT);
				
			
			} else if(cID == Component.Identifier.Key.UP) {
				player.walk(Sprite.Direction.UP);
				
			
			} else if(cID == Component.Identifier.Key.DOWN) {
				player.walk(Sprite.Direction.DOWN);
				
			
			}  
			
		} else {
			player.walk(Sprite.Direction.NONE);
		}
		
	}

}

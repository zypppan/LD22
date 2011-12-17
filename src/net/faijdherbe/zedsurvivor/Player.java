package net.faijdherbe.zedsurvivor;

public class Player extends Sprite {
	
	int walkingDirection = Sprite.Direction.NONE;
	
	Item [] items = new Item[16];
	
	public void walk(int direction) {
		walkingDirection = direction;
	}
	
	@Override
	public void update(long timeSinceLastUpdate) {
		// TODO Auto-generated method stub
		super.update(timeSinceLastUpdate);

		float moveFactor = (0.0667f*(timeSinceLastUpdate/10000000));
		switch(walkingDirection) {
		case Sprite.Direction.LEFT:
			this.origin.x -= moveFactor;
			break;
		case Sprite.Direction.RIGHT:
			this.origin.x += moveFactor;
			break;
		case Sprite.Direction.UP:
			this.origin.y -= moveFactor;
			break;
		case Sprite.Direction.DOWN:
			this.origin.y += moveFactor;
			break;
		}
	}
	
	public boolean pickup(Item item) {
		for(int i = 0; i < items.length; i++) {
			if(items[i] == null) {
				items[i] = item;
				return true;
			}
		}
		return false;
	}
	
	
	public Player() {
		super("player.bmp");
	}
}

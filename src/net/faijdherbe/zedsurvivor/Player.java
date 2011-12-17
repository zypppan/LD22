package net.faijdherbe.zedsurvivor;

public class Player extends Sprite {
	
	int walkingDirection = Sprite.Direction.NONE;
	
	public void walk(int direction) {
		walkingDirection = direction;
	}
	
	@Override
	public void update(long timeSinceLastUpdate) {
		// TODO Auto-generated method stub
		super.update(timeSinceLastUpdate);

		float moveFactor = (1*(timeSinceLastUpdate/10000000));
		switch(walkingDirection) {
		case Sprite.Direction.LEFT:
			this.setxLocation(this.getxLocation() - moveFactor);
			break;
		case Sprite.Direction.RIGHT:
			this.setxLocation(this.getxLocation() + moveFactor);
			break;
		case Sprite.Direction.UP:
			this.setyLocation(this.getyLocation() - moveFactor);
			break;
		case Sprite.Direction.DOWN:
			this.setyLocation(this.getyLocation() + moveFactor);
			break;
		}
	}
	
	public Player() {
		super("player.bmp");
	}
}

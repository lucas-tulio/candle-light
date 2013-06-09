package game.gameplay;

import game.system.Map;
import game.system.Tile;

import org.newdawn.slick.Animation;
import org.newdawn.slick.Image;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;

public abstract class Entity {
	
	// Basic Stuff
	protected float xPos;
	protected float yPos;
	protected float xOffset;
	protected float yOffset;
	protected int depth;
	protected float speed;
	protected boolean isMoving;
	protected boolean isAlive;
	
	// Map Stuff
	protected int xTile;
	protected int yTile;
	
	// Image stuff
	protected SpriteSheet spriteSheep;
	protected Animation[] animation;
	protected Image[] image;
	protected Image imageDead;
	protected int facingDirection;
	protected int animationDuration;
	protected Rectangle rectangle;
	
	// Facing Directions
	public static final int UP = 0;
	public static final int RIGHT = 1;
	public static final int DOWN = 2;
	public static final int LEFT = 3;
	
	/**
	 * Checks if movement is valid, based on the current Facing Direction
	 * @param map
	 * @param delta
	 * @return
	 */
	public boolean isValidMovement(Map map, int delta) {
		
		// Create the test rectangle
		Rectangle testRectangle = null;
		
		if(facingDirection == UP) {
			testRectangle = new Rectangle(xPos + xOffset, yPos + yOffset - (speed * delta), 16, 16);
		} else if (facingDirection == LEFT) {
			testRectangle = new Rectangle(xPos + xOffset - (speed * delta), yPos + yOffset, 16, 16);
		} else if (facingDirection == DOWN) {
			testRectangle = new Rectangle(xPos + xOffset, yPos + yOffset + (speed * delta), 16, 16);
		} else if (facingDirection == RIGHT) {
			testRectangle = new Rectangle(xPos + xOffset + (speed * delta), yPos + yOffset, 16, 16);
		}
		
		// Test against Map blocks
		for(Tile t : map.getTiles()) {
			if(testRectangle.intersects(t.getRectangle()))
				return false;
		}
		
		return true;
	}
	
	public float getxPos() {
		return xPos;
	}
	public void setxPos(float xPos) {
		this.xPos = xPos;
	}
	public float getyPos() {
		return yPos;
	}
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}
	public float getxOffset() {
		return xOffset;
	}
	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}
	public float getyOffset() {
		return yOffset;
	}
	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}
	public int getDepth() {
		return depth;
	}
	public void setDepth(int depth) {
		this.depth = depth;
	}
	public int getxTile() {
		return xTile;
	}
	public void setxTile(int xTile) {
		this.xTile = xTile;
	}
	public int getyTile() {
		return yTile;
	}
	public void setyTile(int yTile) {
		this.yTile = yTile;
	}
	public SpriteSheet getSpriteSheep() {
		return spriteSheep;
	}
	public void setSpriteSheep(SpriteSheet spriteSheep) {
		this.spriteSheep = spriteSheep;
	}
	public Animation[] getAnimation() {
		return animation;
	}
	public void setAnimation(Animation[] animation) {
		this.animation = animation;
	}
	public Image[] getImage() {
		return image;
	}
	public void setImage(Image[] image) {
		this.image = image;
	}
	public int getFacingDirection() {
		return facingDirection;
	}
	public void setFacingDirection(int facingDirection) {
		this.facingDirection = facingDirection;
	}
	public int getAnimationDuration() {
		return animationDuration;
	}
	public void setAnimationDuration(int animationDuration) {
		this.animationDuration = animationDuration;
	}
	public float getSpeed() {
		return speed;
	}
	public void setSpeed(float speed) {
		this.speed = speed;
	}
	public boolean isMoving() {
		return isMoving;
	}
	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}
	public Rectangle getRectangle() {
		return rectangle;
	}
	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}
	public boolean isAlive() {
		return isAlive;
	}
	public void setAlive(boolean alive) {
		this.isAlive = alive;
	}

	public Image getImageDead() {
		return imageDead;
	}

	public void setImageDead(Image imageDead) {
		this.imageDead = imageDead;
	}
	
}

package game.system;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Tile {
	
	private Rectangle rectangle;
	private float xPos;
	private float yPos;
	private float xOffset;
	private float yOffset;
	
	public Tile(float x, float y, float width, float height) {
		
		rectangle = new Rectangle(x, y, width, height);
		xPos = x;
		yPos = y;
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		rectangle.setX(xPos + xOffset);
		rectangle.setY(yPos + yOffset);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
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
	
	
}

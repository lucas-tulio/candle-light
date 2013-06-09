package game.gameplay;

import game.system.GameSystem;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Book extends Entity {
	
	private Rectangle rectangle;
	private boolean gotBook;
	
	public Book(int xTile, int yTile) throws SlickException {
		
		this.xTile = xTile;
		this.yTile = yTile;
		this.xPos = xTile * 16;
		this.yPos = yTile * 16;
		
		gotBook = false;
		
		image = new Image[1];
		image[0] = new Image("res/img/items/book.png");
		image[0].setFilter(Image.FILTER_NEAREST);
		rectangle = new Rectangle(xPos, yPos, 16, 16);
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta, Character character) throws SlickException {
		
		// Update the rectangle position
		rectangle.setX(xPos + xOffset);
		rectangle.setY(yPos + yOffset);

		// Check if the player picked it up
		if(rectangle.intersects(character.getRectangle())) {
			game.getState(GameSystem.FINISHED_STATE).init(container, game);
			game.enterState(GameSystem.FINISHED_STATE);
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawImage(image[0], xPos + xOffset, yPos + yOffset);
		//g.draw(rectangle);
	}

	public Rectangle getRectangle() {
		return rectangle;
	}

	public void setRectangle(Rectangle rectangle) {
		this.rectangle = rectangle;
	}

	public boolean gotBook() {
		return gotBook;
	}

	public void setGotBook(boolean gotBook) {
		this.gotBook = gotBook;
	}
}

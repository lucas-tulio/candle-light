package game.gameplay;

import game.system.Map;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class Light extends Entity {
	
	private int currentImage;
	
	public Light(float xPos, float yPos) throws SlickException {
		this.xPos = xPos;
		this.yPos = yPos;
		
		currentImage = 2;
		
		image = new Image[3];
		image[0] = new Image("res/img/light/Lightning_small.png");
		image[1] = new Image("res/img/light/Lightning_medium.png");
		image[2] = new Image("res/img/light/Lightning_large.png");
		
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta, Input input, Map map) throws SlickException {
		
	}
	
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		g.drawImage(image[currentImage], xPos + xOffset, yPos + yOffset);
	}

	public int getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(int currentImage) {
		this.currentImage = currentImage;
	}
	
	
}

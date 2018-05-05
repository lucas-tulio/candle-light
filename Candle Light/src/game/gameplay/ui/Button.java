package game.gameplay.ui;

import game.gameplay.Entity;
import game.system.GameSystem;

import org.newdawn.slick.Image;

public class Button extends Entity {
	
	protected float width;
	protected float height;
	
	public Button(float xPos, float yPos, float width, float height, Image image) {
		this.xPos = xPos;
		this.yPos = yPos;
		this.width = width;
		this.height = height;
		this.image = new Image[1];
		this.image[0] = image;
	}
	
	public boolean gotClicked(boolean leftMouse, int mouseX, int mouseY) {
		if(leftMouse) {
			if(mouseX / GameSystem.globalScale >= xPos && mouseX / GameSystem.globalScale <= xPos + width
					&& mouseY / GameSystem.globalScale >= yPos && mouseY / GameSystem.globalScale <= yPos + height) {
				return true;
			}
		}
		return false;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}
}

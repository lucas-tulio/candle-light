package game.gameplay;

import game.system.GameSystem;
import game.system.Map;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;

public class Character extends Entity implements Collidable {
	
	Light light;		// Light around the Character
	
	public Character(int xTile, int yTile) throws SlickException {
		
		this.xTile = xTile;
		this.yTile = yTile;
		this.xPos = xTile * 16;
		this.yPos = yTile * 16;
		
		// Basic properties
		speed = 0.03f;
		isMoving = false;
		isAlive = true;
		
		
		
		// Open the Sprite Sheep
		spriteSheep = new SpriteSheet("res/img/character/bartender alpha.png", 16, 16);
		
		// Create the Images and the Animations
		image = new Image[4];
		animation = new Animation[4];
		animationDuration = 200;
		rectangle = new Rectangle(xPos, yPos, 16, 16);
		light = new Light(0f, 0f);
		
		// The Standing Still images are on the first row
		image[Entity.UP] = spriteSheep.getSprite(0, 0);
		image[Entity.RIGHT] = spriteSheep.getSprite(1, 0);
		image[Entity.DOWN] = spriteSheep.getSprite(2, 0);
		image[Entity.LEFT] = spriteSheep.getSprite(3, 0);
		imageDead = new Image("res/img/character/bartender dead.png");
		imageDead.setFilter(Image.FILTER_NEAREST);
		
		// The Animated images are on the rows below
		animation[Entity.UP] = new Animation(new Image[]{image[Entity.UP], spriteSheep.getSprite(0, 1), spriteSheep.getSprite(1, 1)}, animationDuration, false);
		animation[Entity.RIGHT] = new Animation(new Image[]{image[Entity.RIGHT], spriteSheep.getSprite(0, 2), spriteSheep.getSprite(1, 2)}, animationDuration, false);
		animation[Entity.DOWN] = new Animation(new Image[]{image[Entity.DOWN], spriteSheep.getSprite(0, 3), spriteSheep.getSprite(1, 3)}, animationDuration, false);
		animation[Entity.LEFT] = new Animation(new Image[]{image[Entity.LEFT], spriteSheep.getSprite(0, 4), spriteSheep.getSprite(1, 4)}, animationDuration, false);
	
		// Apply animation settings
		for(int i = 0; i < 4; i++) {
			animation[i].setPingPong(true);
			for(int j = 0; j < 3; j++) {
				animation[i].setDuration(j, animationDuration);
			}
		}
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta, Input input, Map map) throws SlickException {
		
		// Update the rectangle position
		rectangle.setX(xPos + xOffset);
		rectangle.setY(yPos + yOffset);
		
		// Update the light position
		if(light.getCurrentImage() == 0) {
			
			// Small light
			light.setxPos(xPos + xOffset - 194 - (light.getImage()[light.getCurrentImage()].getWidth() / 2 / GameSystem.globalScale) + image[facingDirection].getWidth() / 2);
			light.setyPos(yPos + yOffset - 120 - (light.getImage()[light.getCurrentImage()].getHeight() / 2 / GameSystem.globalScale) + image[facingDirection].getHeight() / 2);
			
		} else if(light.getCurrentImage() == 1) {
			
			// Medium Light
			light.setxPos(xPos + xOffset - 203 - (light.getImage()[light.getCurrentImage()].getWidth() / 2 / GameSystem.globalScale) + image[facingDirection].getWidth() / 2);
			light.setyPos(yPos + yOffset - 136 - (light.getImage()[light.getCurrentImage()].getHeight() / 2 / GameSystem.globalScale) + image[facingDirection].getHeight() / 2);
			
		} else if(light.getCurrentImage() == 2) {
			
			// Large Light
			light.setxPos(xPos + xOffset - 219 - (light.getImage()[light.getCurrentImage()].getWidth() / 2 / GameSystem.globalScale) + image[facingDirection].getWidth() / 2);
			light.setyPos(yPos + yOffset - 150 - (light.getImage()[light.getCurrentImage()].getHeight() / 2 / GameSystem.globalScale) + image[facingDirection].getHeight() / 2);
			
		}
				
		// Check input and set movement
		if(isAlive) {
			if(input.isKeyDown(Input.KEY_W)) {
				
				// Update Facing Direction
				facingDirection = Entity.UP;
				
				// Check if the movement is valid
				if(isValidMovement(map, delta)) {
					animation[Entity.UP].update(delta);
					yPos -= speed * delta;
					isMoving = true;
				}
				
			} else if (input.isKeyDown(Input.KEY_A)) {
				
				// Update Facing Direction
				facingDirection = Entity.LEFT;
				
				// Check if movement is valid
				if(isValidMovement(map, delta)) {
					xPos -= speed * delta;
					animation[Entity.LEFT].update(delta);
					isMoving = true;	
				}
				
			} else if (input.isKeyDown(Input.KEY_S)) {
				
				// Update Facing Direction
				facingDirection = Entity.DOWN;
				
				// Check if movement is valid
				if(isValidMovement(map, delta)) {
					yPos += speed * delta;
					animation[Entity.DOWN].update(delta);
					isMoving = true;	
				}
				
			} else if (input.isKeyDown(Input.KEY_D)) {
				
				// Update Facing Direction
				facingDirection = Entity.RIGHT;
				
				// Check if movement is valid
				if(isValidMovement(map, delta)) {
					xPos += speed * delta;
					animation[Entity.RIGHT].update(delta);
					isMoving = true;
				}
			} else {
				isMoving = false;
			}
		}
		
		// Update its current x and y Tiles, depending on the x and y Pos
		xTile = (int)((xPos + image[facingDirection].getWidth() / 2) / 16);
		yTile = (int)((yPos + image[facingDirection].getHeight() / 2) / 16);
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		// Draw rectangle (debug)
		//g.draw(rectangle);
		
		
		// Check if it's moving
		if(isMoving && isAlive) {

			// Renders the animation
			g.drawAnimation(animation[facingDirection], xPos + xOffset, yPos + yOffset);

		} else if (!isMoving && isAlive) {
			
			// Renders the standing still sprite
			g.drawImage(image[facingDirection], xPos + xOffset, yPos + yOffset);
		} else if(!isAlive) {
			
			// Dead!
			g.drawImage(imageDead, xPos + xOffset, yPos + yOffset);
		}
		
		
		
	}

	public Light getLight() {
		return light;
	}

	public void setLight(Light light) {
		this.light = light;
	}	
}
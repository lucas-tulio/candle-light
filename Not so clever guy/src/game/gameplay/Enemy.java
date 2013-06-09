package game.gameplay;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.geom.Line;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.pathfinding.Mover;
import org.newdawn.slick.util.pathfinding.Path;

import game.system.Map;
import game.system.Tile;

public class Enemy extends Entity implements Mover, Collidable {

	// Behavior stuff
	private int movementDirection;	// Direction to where he's moving
	private boolean readNextStep;	// Indicates the enemy reached the next Tile so it needs to read the next step in the path to continue walking
	private boolean isChasing;	// Is chasing the player
	private int detectionTime;	// Current detection interval
	private int maxDetectionTime;	// Max detection interval (so it doesn't run all the time)
	private boolean isRandomlyMoving;	// Enemy is idle, walking around looking for players to kill
	private int idleTime;	// Current idle time
	private int maxIdleTime;		// Max idle time before moving randomly
	
	// Pathfinding and stuff
	private Path path;		// Path to where it will move
	private Line detectionLine;	// The line the Enemy will use to detect players
	private int currentPathStep;	// Current path step it is following
	private float maxDetectionRange; // Max detection range
	
	private int targetXTile;
	private int targetYTile;
	private float targetXPos;
	private float targetYPos;
	private float originalXPos;
	private float originalYPos;
	
	// Sound
	private Sound deathSound;
	private Sound detectionSound;
	private boolean alreadyPlayedDeathSound;
	private boolean alreadyPlayedDetectionSound;
		
	public Enemy(int xTile, int yTile) throws SlickException {
		
		this.xTile = xTile;
		this.yTile = yTile;
		this.xPos = xTile * 16;
		this.yPos = yTile * 16;
		
		// Basic properties
		speed = 0.02f;
		isMoving = false;
		isAlive = true;
		maxDetectionRange = 64;
		maxDetectionTime = 1000;
		maxIdleTime = 2000;
		detectionTime = 0;
		readNextStep = false;
		isChasing = false;
		
		// Sound
		deathSound = new Sound("res/sfx/death.wav");
		detectionSound = new Sound("res/sfx/monster.wav");
		alreadyPlayedDeathSound = false;
		alreadyPlayedDetectionSound = false;
		
		// Open the Sprite Sheep
		spriteSheep = new SpriteSheet("res/img/enemy/zombie alpha.png", 16, 20);
		
		// Create the Images and the Animations
		image = new Image[4];
		animation = new Animation[4];
		animationDuration = 300;
		rectangle = new Rectangle(xPos, yPos, 16, 16);
		
		// The Standing Still images are on the middle column
		image[Entity.UP] = spriteSheep.getSprite(1, 0);
		image[Entity.RIGHT] = spriteSheep.getSprite(1, 1);
		image[Entity.DOWN] = spriteSheep.getSprite(1, 2);
		image[Entity.LEFT] = spriteSheep.getSprite(1, 3);
		
		// The Animated images are on the rows below
		animation[Entity.UP] = new Animation(new Image[]{image[Entity.UP], spriteSheep.getSprite(0, 0), spriteSheep.getSprite(2, 0)}, animationDuration, false);
		animation[Entity.RIGHT] = new Animation(new Image[]{image[Entity.RIGHT], spriteSheep.getSprite(0, 1), spriteSheep.getSprite(2, 1)}, animationDuration, false);
		animation[Entity.DOWN] = new Animation(new Image[]{image[Entity.DOWN], spriteSheep.getSprite(0, 2), spriteSheep.getSprite(2, 2)}, animationDuration, false);
		animation[Entity.LEFT] = new Animation(new Image[]{image[Entity.LEFT], spriteSheep.getSprite(0, 3), spriteSheep.getSprite(2, 3)}, animationDuration, false);

		// Apply animation settings
		for(int i = 0; i < 4; i++) {
			animation[i].setPingPong(true);
			for(int j = 0; j < 3; j++) {
				animation[i].setDuration(j, animationDuration);
			}
		}
		
		// Adjusts positions
		updateTiles();
		snapToGrid();
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta, Input input, Map map, Character character) throws SlickException {
		
		// Update the rectangle position
		rectangle.setX(xPos + xOffset);
		rectangle.setY(yPos + yOffset + 4);
		
		// Update Positions
		updateTiles();
		
		/**
		 * Detection
		 */
		// Count torwards detection time
		detectionTime += delta;
		
		if(detectionTime >= maxDetectionTime && !isChasing && !readNextStep && character.isAlive()) {
		//if(input.isKeyPressed(Input.KEY_SPACE) && !isChasing && !readNextStep) {
			
			// Reset detection time
			detectionTime = 0;
			
			// Draw a line from itself to the player
			detectionLine = new Line(xPos + xOffset + image[facingDirection].getWidth() / 2,
					yPos + yOffset + image[facingDirection].getHeight() / 2,
					xOffset + character.getxPos() + character.getImage()[character.getFacingDirection()].getWidth() / 2,
					yOffset + character.getyPos() + character.getImage()[character.getFacingDirection()].getHeight() / 2);
			
			// Check if it crossed any Blocks. If it did, this detect doesn't count (hit wall)
			boolean crossedWall = false;
			for(Tile t : map.getTiles()) {
				if(detectionLine.intersects(t.getRectangle())) {
					crossedWall = true;
					isChasing = false;
					readNextStep = false;
					maxDetectionTime = 1000;
					maxIdleTime = 3000;
					break;
				}
			}
			
			// Saw the Player!
			if(!crossedWall && detectionLine.length() <= maxDetectionRange && detectionLine.intersects(character.getRectangle())) {
				readNextStep = true;
				speed = 0.05f;
				animationDuration = 100;
				currentPathStep = 0;	
				
				// Max Detection time is reduced so the Enemy can chase better
				maxDetectionTime = 300;
				maxIdleTime = 10000;
				
				// Play Sound!
				if(!alreadyPlayedDetectionSound) {
					alreadyPlayedDetectionSound = true;
					detectionSound.play();
				}
				
				// Calculate the Path to the Player
				path = map.getPathFinder().findPath(this, xTile, yTile, character.getxTile(), character.getyTile());
			}
		}
		
		/**
		 * Random movement
		 */
		idleTime += delta;
		if(idleTime >= maxIdleTime && !isChasing && !readNextStep) {
			
			// Reset idle time and path step
			idleTime = 0;
			maxIdleTime = 3000;
			speed = 0.02f;
			currentPathStep = 0;
			
			// Get a random movement direction and the target tiles
			int randomDirection = new java.util.Random().nextInt(4);
			if(randomDirection == UP) {
				targetXTile = xTile;
				targetYTile = yTile - 1;
				movementDirection = UP;
			} else if (randomDirection == DOWN) {
				targetXTile = xTile;
				targetYTile = yTile + 1;
				movementDirection = DOWN;
			} else if (randomDirection == LEFT) {
				targetXTile = xTile - 1;
				targetYTile = yTile;
				movementDirection = LEFT;
			} else if (randomDirection == RIGHT) {
				targetXTile = xTile + 1;
				targetYTile = yTile;
				movementDirection = RIGHT;
			}
			
			// Get the target tiles of the next step
			path = map.getPathFinder().findPath(this, xTile, yTile, targetXTile, targetYTile);
			if(path != null) {
				targetXTile = path.getX(currentPathStep);
				targetYTile = path.getY(currentPathStep);
				
				// Calculate target positions based on the target Tiles
				originalXPos = xPos;
				originalYPos = yPos;
				
				// Set chasing true
				isRandomlyMoving = true;
			}
		}
		
		if(isRandomlyMoving && !isChasing) {
			// Move!
			move(delta);
			
			if(movementDirection == UP) {
				if(yPos < originalYPos - 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = isRandomlyMoving = false;
					currentPathStep = 0;
				}
			} else if(movementDirection == DOWN) {
				if(yPos > originalYPos + 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = isRandomlyMoving = false;
					currentPathStep = 0;
				}
			} else if(movementDirection == LEFT) {
				if(xPos < originalXPos - 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = isRandomlyMoving = false;
					currentPathStep = 0;
				}
			} else if(movementDirection == RIGHT) {
				if(xPos > originalXPos + 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = isRandomlyMoving = false;
					currentPathStep = 0;
				}
			}
		}
		
		/**
		 * Chasing (get next step)
		 */
		if(readNextStep) {
			
			// Check if we still have paths to follow
			if(currentPathStep < path.getLength()) {
				
				// Get the target tiles of the next step
				targetXTile = path.getX(currentPathStep);
				targetYTile = path.getY(currentPathStep);
				
				// Calculate target positions based on the target Tiles
				originalXPos = xPos;
				originalYPos = yPos;
				
				if(targetXTile < xTile && targetYTile == yTile) {
					movementDirection = LEFT;
					readNextStep = false;
					isChasing = true;
				} else if (targetXTile > xTile && targetYTile == yTile) {
					movementDirection = RIGHT;
					readNextStep = false;
					isChasing = true;
				} else if (targetYTile < yTile && targetXTile == xTile) {
					movementDirection = UP;
					readNextStep = false;
					isChasing = true;
				} else if (targetYTile > yTile && targetXTile == xTile) {
					movementDirection = DOWN;
					readNextStep = false;
					isChasing = true;
				} else if(targetXTile == xTile && targetYTile == yTile) {
					currentPathStep++;
					readNextStep = true;
					isChasing = false;
				}
				
			} else {
				
				// Ended Path
				path = null;
				readNextStep = false;
				isChasing = false;
				alreadyPlayedDetectionSound = false;
			}
		}
		
		// Chasing (movement)
		if(isChasing) {

			// Move!
			move(delta);
			
			if(movementDirection == UP) {
				if(yPos < originalYPos - 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = true;
				}
			} else if(movementDirection == DOWN) {
				if(yPos > originalYPos + 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = true;
				}
			} else if(movementDirection == LEFT) {
				if(xPos < originalXPos - 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = true;
				}
			} else if(movementDirection == RIGHT) {
				if(xPos > originalXPos + 16f) {
					updateTiles();
					snapToGrid();
					isChasing = false;
					readNextStep = true;
				}
			}
		}
		
		// Update facing direction so the animation plays like a pro
		facingDirection = movementDirection;
		
		// Check if we killed the player
		if(rectangle.intersects(character.getRectangle())) {
			character.setAlive(false);
			
			if(!alreadyPlayedDeathSound) {
				alreadyPlayedDeathSound = true;
				deathSound.play();
			}
		}
	}
	
	/**
	 * Adjusts x and y pos so it doesn't lose precision over time
	 */
	public void snapToGrid() {
		xPos = xTile * 16;
		yPos = yTile * 16;
	}

	/**
	 * 	 Update its current x and y Tiles, depending on the x and y Pos
	 */
	public void updateTiles() {
		xTile = (int)((xPos + image[facingDirection].getWidth() / 2) / 16);
		yTile = (int)((yPos + image[facingDirection].getHeight() / 2) / 16);
	}
	
	/**
	 * Move the enemy
	 * @param map
	 * @param delta
	 */
	private void move(int delta) {
		if(movementDirection == Entity.UP) {
			yPos -= speed * delta;
			animation[Entity.UP].update(delta);
		}
		else if(movementDirection == Entity.LEFT) {
			xPos -= speed * delta;
			animation[Entity.LEFT].update(delta);
		}
		else if(movementDirection == Entity.DOWN) {
			yPos += speed * delta;
			animation[Entity.DOWN].update(delta);
		}
		else if(movementDirection == Entity.RIGHT) {
			xPos += speed * delta;
			animation[Entity.RIGHT].update(delta);
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		// Check if it's moving
		if(isChasing || isRandomlyMoving) {

			// Renders the animation
			g.drawAnimation(animation[facingDirection], xPos + xOffset, yPos + yOffset);

		} else {
			
			// Renders the standing still sprite
			g.drawImage(image[facingDirection], xPos + xOffset, yPos + yOffset);
		}
		
		// Rectangle render
		//g.draw(rectangle);
		
		// Detection line render
		//if(detectionLine != null)
		//	g.draw(detectionLine);
		
	}
	
	public int getMovementDirection() {
		return movementDirection;
	}

	public void setMovementDirection(int movementDirection) {
		this.movementDirection = movementDirection;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public Line getDetectionLine() {
		return detectionLine;
	}

	public void setDetectionLine(Line detectionLine) {
		this.detectionLine = detectionLine;
	}

	public boolean isReadNextStep() {
		return readNextStep;
	}

	public void setReadNextStep(boolean readNextStep) {
		this.readNextStep = readNextStep;
	}

	public boolean isChasing() {
		return isChasing;
	}

	public void setChasing(boolean isChasing) {
		this.isChasing = isChasing;
	}

	public int getCurrentPathStep() {
		return currentPathStep;
	}

	public void setCurrentPathStep(int currentPathStep) {
		this.currentPathStep = currentPathStep;
	}

	public int getTargetXTile() {
		return targetXTile;
	}

	public void setTargetXTile(int targetXTile) {
		this.targetXTile = targetXTile;
	}

	public int getTargetYTile() {
		return targetYTile;
	}

	public void setTargetYTile(int targetYTile) {
		this.targetYTile = targetYTile;
	}

	public float getTargetXPos() {
		return targetXPos;
	}

	public void setTargetXPos(float targetXPos) {
		this.targetXPos = targetXPos;
	}

	public float getTargetYPos() {
		return targetYPos;
	}

	public void setTargetYPos(float targetYPos) {
		this.targetYPos = targetYPos;
	}

	public float getOriginalXPos() {
		return originalXPos;
	}

	public void setOriginalXPos(float originalXPos) {
		this.originalXPos = originalXPos;
	}

	public float getOriginalYPos() {
		return originalYPos;
	}

	public void setOriginalYPos(float originalYPos) {
		this.originalYPos = originalYPos;
	}
	
}
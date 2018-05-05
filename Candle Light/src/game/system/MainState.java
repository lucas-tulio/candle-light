package game.system;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import game.gameplay.Book;
import game.gameplay.Character;
import game.gameplay.Enemy;

public class MainState extends BasicGameState {

	// Entities in the game
	private Character character;
	private Enemy[] enemy;
	private Book book;
	
	// The Map
	private Map map;
	
	// Graphics stuff
	private float xOffset = 0f;
	private float yOffset = 0f;
	
	// Input
	private Input input;
	
	// Font
    private Image fontImage;
	private AngelCodeFont font;
	
	// Time Controller
	private int currentTime;
	
	// Sound
	private Sound piano;
	private boolean playedFirstPiano;
	private boolean playedSecondPiano;
	
	@Override
	public int getID() {return 3;}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		
		// Start the font
		fontImage = new Image("res/font/lcd_solid_9_00.png");
		fontImage.setFilter(Image.FILTER_NEAREST);
		font = new AngelCodeFont("res/font/lcd_solid_9.fnt", fontImage);
		
		// Start the map
		map = new Map("res/map/dungeon.tmx");
		
		// Start the time
		currentTime = 0;
		
		// Sound
		piano = new Sound("res/sfx/piano.wav");
		playedFirstPiano = false;
		playedSecondPiano = false;
		
		// Create the Entities
		character = new Character(27, 27);
		book = new Book(4, 1);
		//book = new Book(25, 27);
		
		enemy = new Enemy[7];
		enemy[0] = new Enemy(2, 6);
		enemy[1] = new Enemy(2, 18);
		enemy[2] = new Enemy(13, 15);
		enemy[3] = new Enemy(10, 2);
		enemy[4] = new Enemy(27, 2);
		enemy[5] = new Enemy(23, 15);
		enemy[6] = new Enemy(14, 25);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		// Pass Time. If too much time has passed, reduce light
		currentTime += delta;
		if(currentTime >= 120000) {
			
			if(!playedFirstPiano) {
				playedFirstPiano = true;
				character.getLight().setCurrentImage(0);
				piano.play();
			}
		}
		else if(currentTime >= 60000) {
			
			if(!playedSecondPiano) {
				playedSecondPiano = true;
				character.getLight().setCurrentImage(1);
				piano.play();
			}
		}
		
		// Get Input
		input = container.getInput();
		
		// Update the Character
		character.update(container, game, delta, input, map);				
		
		// Update global offset
		xOffset = -1 * (character.getxPos() + (character.getImage()[character.getFacingDirection()].getWidth()  / 2)) + (container.getWidth() / 2 / GameSystem.globalScale);
		yOffset = -1 * (character.getyPos() + (character.getImage()[character.getFacingDirection()].getHeight() / 2)) + (container.getHeight() / 2 / GameSystem.globalScale);

		// Apply the Global Offset to all Entities		
		character.setxOffset(xOffset);
		character.setyOffset(yOffset);
		for(int i = 0; i < enemy.length; i++) {
			enemy[i].setxOffset(xOffset);
			enemy[i].setyOffset(yOffset - 4);
		}
		book.setxOffset(xOffset);
		book.setyOffset(yOffset);
		map.setxOffset(xOffset);
		map.setyOffset(yOffset);
		
		// Update other Entities
		for(int i = 0; i < enemy.length; i++) {
			enemy[i].update(container, game, delta, input, map, character);
		}
		book.update(container, game, delta, character);
		map.update(container, game, delta);
		
		// Check the Game State
		if(!character.isAlive()) {
			if(input.isKeyPressed(Input.KEY_R)) {
				this.init(container, game);
			}
		}
		
		// Release Input to prevent weird shit from happening
		input.clearKeyPressedRecord();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		// Rendering settings
		g.scale(GameSystem.globalScale, GameSystem.globalScale);
		g.setBackground(Color.black);
		g.setColor(Color.white);
		g.setFont(font);
		
		// Render the map
		map.render(container, game, g);
		
		// Render the Character and the Enemy
		character.render(container, game, g);
		for(int i = 0; i < enemy.length; i++) {
			enemy[i].render(container, game, g);
		}
		book.render(container, game, g);
		character.getLight().render(container, game, g);
		
		// Render text
		if(!character.isAlive()) {
			g.drawString("Press 'R' to restart", (container.getWidth() / 2 / GameSystem.globalScale) - 62, (container.getHeight() / 2/ GameSystem.globalScale) - 20);
		}		
	}
}

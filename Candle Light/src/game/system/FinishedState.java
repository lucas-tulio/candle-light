package game.system;


import game.gameplay.ui.Button;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.SpriteSheet;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class FinishedState extends BasicGameState {
	
	Button backButton;
	Image backButtonImage;
		
	Input input;
	
	Image characterImage;
	Image bookImage;
	Image dealWithIt;
	float dealWithItXPos;
	float dealWithItYPos;
	float dealWithItYTarget;
	
	int printTheEnd;
	int printTheEndTarget;
	
	// Font
    private Image fontImage;
	private AngelCodeFont font;
	
	@Override
	public int getID() {
		return 4;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		fontImage = new Image("res/font/lcd_solid_9_00.png");
		fontImage.setFilter(Image.FILTER_NEAREST);
		font = new AngelCodeFont("res/font/lcd_solid_9.fnt", fontImage);
		
		backButtonImage = new Image("res/img/ui/backButton.png");
		backButtonImage.setFilter(Image.FILTER_NEAREST);
		backButton = new Button(80f, 160f, backButtonImage.getWidth(), backButtonImage.getHeight(), backButtonImage);	
		
		dealWithIt = new Image("res/img/ui/dealwithit.png");
		dealWithIt.setFilter(Image.FILTER_NEAREST);
		dealWithItXPos = 36f;
		dealWithItYPos = -20f;
		dealWithItYTarget = 72f;
		printTheEnd = 0;
		printTheEndTarget = 1000;
		
		// Sprite sheep strikes back
		SpriteSheet spriteSheep = new SpriteSheet("res/img/character/bartender alpha.png", 16, 16);
		characterImage =  spriteSheep.getSprite(3, 0);
		characterImage.setFilter(Image.FILTER_NEAREST);
		
		bookImage = new Image("res/img/items/book.png");
		bookImage.setFilter(Image.FILTER_NEAREST);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		// Get input
		input = container.getInput();
		boolean leftMouse = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		if(backButton.gotClicked(leftMouse, mouseX, mouseY)) {
			game.enterState(GameSystem.MENU_STATE);
		}
		
		// Move deal with it down
		if(dealWithItYPos < dealWithItYTarget) {
			dealWithItYPos += delta * 0.01f;
		} else {
			printTheEnd += delta;
		}
		
		// Release input
		input.clearMousePressedRecord();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		g.setColor(Color.black);
		g.setBackground(Color.white);
		g.setFont(font);
		g.scale(GameSystem.globalScale, GameSystem.globalScale);
		
		g.drawString("Found it!", 10f, 20f);
		g.drawString("oh wait", 10f, 30f);
		g.drawString("That's not the book I was looking for!", 10f, 40f);
		
		g.drawImage(characterImage, 80f, 70f);
		g.drawImage(bookImage, 50f, 70f);
		
		g.drawImage(dealWithIt, dealWithItXPos, dealWithItYPos);
		
		if(!(dealWithItYPos < dealWithItYTarget)) {
			g.drawString("DEAL WITH IT", 22f, 90f);
		}
		
		
		if(printTheEnd >= printTheEndTarget) {
			g.drawString("THE END", 20f, 120f);
		}
		g.drawImage(backButtonImage, backButton.getxPos(), backButton.getyPos());
	}
}

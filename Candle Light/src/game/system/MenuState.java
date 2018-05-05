package game.system;

import game.gameplay.ui.Button;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class MenuState extends BasicGameState {

	Button startButton;
	Button creditsButton;
	Button quitButton;
	
	Image startButtonImage;
	Image creditsButtonImage;
	Image quitButtonImage;
	
	boolean clickedStartButton;
	boolean clickedCreditsButton;
	boolean clickedQuitButton;
	
	Input input;
	
	// Font
    private Image fontImage;
	private AngelCodeFont font;
	
	@Override
	public int getID() {
		return 0;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
	
		fontImage = new Image("res/font/lcd_solid_9_00.png");
		fontImage.setFilter(Image.FILTER_NEAREST);
		font = new AngelCodeFont("res/font/lcd_solid_9.fnt", fontImage);
		
		startButtonImage = new Image("res/img/ui/startButton.png");
		startButtonImage.setFilter(Image.FILTER_NEAREST);
		creditsButtonImage= new Image("res/img/ui/creditsButton.png");
		creditsButtonImage.setFilter(Image.FILTER_NEAREST);
		quitButtonImage= new Image("res/img/ui/quitButton.png");
		quitButtonImage.setFilter(Image.FILTER_NEAREST);
		
		startButton = new Button(80f, 120f, startButtonImage.getWidth(), startButtonImage.getHeight(), startButtonImage);
		creditsButton = new Button(80f, 140f, creditsButtonImage.getWidth(), creditsButtonImage.getHeight(), creditsButtonImage);
		quitButton = new Button(80f, 160f, quitButtonImage.getWidth(), quitButtonImage.getHeight(), quitButtonImage);
	}

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
		// Get input
		input = container.getInput();
		boolean leftMouse = input.isMousePressed(Input.MOUSE_LEFT_BUTTON);
		int mouseX = input.getMouseX();
		int mouseY = input.getMouseY();
		
		if(startButton.gotClicked(leftMouse, mouseX, mouseY)) {
			game.enterState(GameSystem.ENTRANCE_STATE);
		} else if(creditsButton.gotClicked(leftMouse, mouseX, mouseY)) {
			game.enterState(GameSystem.CREDITS_STATE);
		} else if(quitButton.gotClicked(leftMouse, mouseX, mouseY)) {
			container.exit();
		}

		// Release input
		input.clearMousePressedRecord();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		g.setColor(Color.black);
		g.setBackground(Color.white);
		g.setFont(font);
		g.scale(8f, 8f);
		g.drawString("NOT SO CLEVER GUY", 11f, 20f);
		g.scale(1/8f, 1/8f);
		
		// Draw Text
		g.scale(2f, 2f);
		g.drawString("Programmed and designed by Lucas Tulio", 44f, 160f);
		g.drawString("for the BaconGameJam 05", 44f, 170f);
		g.drawString("lucasdnd@gmail.com", 44f, 190f);
		
		g.scale(1/2f, 1/2f);
		g.scale(GameSystem.globalScale, GameSystem.globalScale);
		
		// Draw Buttons
		g.drawImage(startButtonImage, startButton.getxPos(), startButton.getyPos());
		g.drawImage(creditsButtonImage, creditsButton.getxPos(), creditsButton.getyPos());
		g.drawImage(quitButtonImage, quitButton.getxPos(), quitButton.getyPos());
	}
}

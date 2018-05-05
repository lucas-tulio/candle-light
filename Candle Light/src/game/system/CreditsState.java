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

public class CreditsState extends BasicGameState {

	Button backButton;
	Image backButtonImage;
		
	Input input;
	
	// Font
    private Image fontImage;
	private AngelCodeFont font;
	
	@Override
	public int getID() {
		return 1;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {

		fontImage = new Image("res/font/lcd_solid_9_00.png");
		fontImage.setFilter(Image.FILTER_NEAREST);
		font = new AngelCodeFont("res/font/lcd_solid_9.fnt", fontImage);
		
		backButtonImage = new Image("res/img/ui/backButton.png");
		backButtonImage.setFilter(Image.FILTER_NEAREST);
		
		backButton = new Button(80f, 160f, backButtonImage.getWidth(), backButtonImage.getHeight(), backButtonImage);		
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

		// Release input
		input.clearMousePressedRecord();
	}
	
	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		g.setColor(Color.black);
		g.setBackground(Color.white);
		g.setFont(font);
		g.scale(GameSystem.globalScale, GameSystem.globalScale);
		
		g.drawString("CREDITS", 20f, 20f);
		g.drawImage(backButtonImage, backButton.getxPos(), backButton.getyPos());
		
		g.scale(1/GameSystem.globalScale, 1/GameSystem.globalScale);
		
		g.scale(2f, 2f);
		
		g.drawString("Designed and programmed by Lucas Tulio - @lucasdnd / murlocsByTheBeach", 20, 70);
		
		g.drawString("Special thanks to the artists who made their work", 20, 100);
		g.drawString("freely available on the Internet:", 20, 110);
		
		g.drawString("Erick Windsor - LCD Solid Font" , 20, 130);
		
		g.drawString("2D art from opengameart.org:" , 20, 150);
		g.drawString("Charles Gabriel - zombie sprite" , 20, 160);
		g.drawString("Trent Gamblin - player sprite" , 20, 170);
		g.drawString("Daniel Siegmund - map tiles" , 20, 180);
		
		g.drawString("Sounds from freesound.org:", 20, 200);
		g.drawString("EliDirkx98 - monster sound", 20, 210);
		g.drawString("harri - piano hit", 20, 220);
		g.drawString("Replix - death sound", 20, 230);
		
	}
}

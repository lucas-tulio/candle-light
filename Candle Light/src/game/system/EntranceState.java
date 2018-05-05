package game.system;

import org.newdawn.slick.AngelCodeFont;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

public class EntranceState extends BasicGameState {

	Input input;
	
	// Font
    private Image fontImage;
	private AngelCodeFont font;
	
	@Override
	public int getID() {
		return 2;
	}
	
	@Override
	public void init(GameContainer container, StateBasedGame game) throws SlickException {
		fontImage = new Image("res/font/lcd_solid_9_00.png");
		fontImage.setFilter(Image.FILTER_NEAREST);
		font = new AngelCodeFont("res/font/lcd_solid_9.fnt", fontImage);
	}	

	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		// Get input
		input = container.getInput();
		
		if(input.isKeyPressed(Input.KEY_W) || input.isKeyPressed(Input.KEY_A) || input.isKeyPressed(Input.KEY_S) || input.isKeyPressed(Input.KEY_D)) {
			game.getState(GameSystem.MAIN_STATE).init(container, game);
			game.enterState(GameSystem.MAIN_STATE);
		}
		// Release input
		input.clearMousePressedRecord();
	}

	@Override
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {

		g.scale(GameSystem.globalScale, GameSystem.globalScale);
		g.setColor(Color.black);
		g.setBackground(Color.white);
		g.setFont(font);
		
		g.drawString("You hear a distant voice:", 20f, 20f);
		g.drawString("\"You came to the wrong", 20f, 40f);
		g.drawString("dungeon, m*********er!\"", 20f, 50f);
		g.drawString("Damn! This place is hardcore!", 20f, 70f);
		
		g.drawString("Press W, A, S or D to continue", 20f, 100f);
	}
}

package game.system;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

public class GameSystem extends StateBasedGame {

	// Graphics scale
	public static float globalScale = 4f;
	
	// States
	MenuState menuState;
	CreditsState creditsState;
	EntranceState entranceState;
	MainState mainState;
	FinishedState finishedState;
	
	public static final int MENU_STATE = 0;
	public static final int CREDITS_STATE = 1;
	public static final int ENTRANCE_STATE = 2;
	public static final int MAIN_STATE = 3;
	public static final int FINISHED_STATE = 4;
	
	public GameSystem(String name) {
		super(name);
		
		menuState = new MenuState();
		creditsState = new CreditsState();
		entranceState = new EntranceState();
		mainState = new MainState();
		finishedState = new FinishedState();
	}

	@Override
	public void initStatesList(GameContainer container) throws SlickException {
		addState(menuState);
		addState(creditsState);
		addState(entranceState);
		addState(mainState);
		addState(finishedState);
	}
}

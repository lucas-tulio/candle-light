package main;

import game.system.GameSystem;

import org.newdawn.slick.AppGameContainer;

public class Start {
	public static void main(String argsp[]) {
		
		try {
			// Create the App Container
			AppGameContainer container = new AppGameContainer(new GameSystem("Candle Light"));
			
			// Start shit up
			container.setDisplayMode(1024, 768, false);
			container.setShowFPS(false);
			container.setTargetFrameRate(60);
			container.setMinimumLogicUpdateInterval(3);
			container.setMaximumLogicUpdateInterval(3);
			container.setVSync(true);
			container.start();

		} catch(Exception e) {e.printStackTrace();}
		
	}
}

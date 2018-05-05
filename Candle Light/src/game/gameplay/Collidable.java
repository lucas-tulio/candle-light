package game.gameplay;

import game.system.Map;

public interface Collidable {
	public boolean isValidMovement(Map map, int delta);
}

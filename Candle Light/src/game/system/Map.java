package game.system;

import java.util.ArrayList;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.pathfinding.AStarPathFinder;
import org.newdawn.slick.util.pathfinding.PathFindingContext;
import org.newdawn.slick.util.pathfinding.TileBasedMap;

public class Map extends TiledMap implements TileBasedMap {

	// Offset
	private float xOffset;
	private float yOffset;
	
	// Blocked Tiles
	private boolean blocked[][];
	private ArrayList<Tile> tiles;
	
	// A*
	private AStarPathFinder pathFinder;
	
	public Map(String ref) throws SlickException {
		super(ref);
		
		// Start the Rectangles Array List
		tiles = new ArrayList<Tile>();
		
		// Setup blocked tiles
		blocked = new boolean[this.getWidth()][this.getHeight()];
		for(int i = 0; i < this.getWidth(); i++) {
			for(int j = 0; j < this.getHeight(); j++) {
				
				int tileID = getTileId(i, j, 1);
				String value = getTileProperty(tileID, "blocked", "false");
				if(value.equals("true")) {
					blocked[i][j] = true;
					tiles.add(new Tile((float)i * 16f, (float)j * 16f, 16f, 16f));
				}
			}
		}
		
		// PathFinding
		pathFinder = new AStarPathFinder(this, super.getWidth() * super.getHeight(), false);
	}
	
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {

		// Updates the Tiles offsets
		for(Tile t : tiles) {
			t.setxOffset(xOffset);
			t.setyOffset(yOffset);
			t.update(container, game, delta);
		}
	}
	
	public void render(GameContainer container, StateBasedGame game, Graphics g) throws SlickException {
		
		// Render the Map
		this.render((int)xOffset, (int)yOffset);
		
		// Render the Rectangles
		//for(Tile t : tiles) {
	//		g.draw(t.getRectangle());
	//	}
	}

	@Override
	public boolean blocked(PathFindingContext arg0, int x, int y) {
		return blocked[x][y];
	}

	@Override
	public float getCost(PathFindingContext arg0, int arg1, int arg2) {
		return 0;
	}

	@Override
	public int getHeightInTiles() {
		return getHeight();
	}

	@Override
	public int getWidthInTiles() {
		return getWidth();
	}

	@Override
	public void pathFinderVisited(int arg0, int arg1) {
		// TODO Auto-generated method stub
	}

	public AStarPathFinder getPathFinder() {
		return pathFinder;
	}

	public void setPathFinder(AStarPathFinder pathFinder) {
		this.pathFinder = pathFinder;
	}

	public float getxOffset() {
		return xOffset;
	}

	public void setxOffset(float xOffset) {
		this.xOffset = xOffset;
	}

	public float getyOffset() {
		return yOffset;
	}

	public void setyOffset(float yOffset) {
		this.yOffset = yOffset;
	}

	public ArrayList<Tile> getTiles() {
		return tiles;
	}

	public void setTiles(ArrayList<Tile> tiles) {
		this.tiles = tiles;
	}
	
}

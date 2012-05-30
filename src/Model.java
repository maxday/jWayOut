import java.awt.Point;
import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import Components.Wall;
import Util.Constants;
import Util.ReadXml;


public class Model extends SimState {
	
	public ObjectGrid2D grid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);

	public Model(long seed) {
		super(seed);
	}

	@Override
	public void start() {
		super.start();
		grid.clear();
		addWalls();
	}

	private void addWalls() {
		List<Wall> wallList = ReadXml.readXmlFile("./resources/MDE-etage1.xml");
		
		for (int iWall = 0; iWall < wallList.size(); ++iWall) {
			Wall wall = wallList.get(iWall);
			List<Point> coords = wall.getListCoord();
			for (Point point : coords) {
				grid.set(point.x, point.y, wall);
			}
		}
		
	}
	
	

}

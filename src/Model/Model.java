package Model;

import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import Agents.People;
import Components.Exit;
import Components.Wall;
import Util.Actions;
import Util.Constants;
import Util.LogConsole;
import Util.Point;
import Util.ReadXml;

@SuppressWarnings("serial")
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
		addExits();
		addAgents();
	}

	private void addWalls() {
		List<Wall> wallList = ReadXml.getWallList();
		
		for (int iWall = 0; iWall < wallList.size(); ++iWall) {
			Wall wall = wallList.get(iWall);
			LogConsole.print(wall.toString(), Actions.Action.ADD.name(), wall.getClass().getName());
			List<Point> coords = wall.getListCoord();
			for (Point point : coords) {
				grid.set(point.x, point.y, wall);
				LogConsole.print(point.toString(), Actions.Action.DRAW.name(), point.getClass().getName());
			}
		}
	}
	
	private void addExits() {
		List<Exit> exitList = ReadXml.getExitList();
		
		for (int iExit = 0; iExit < exitList.size(); ++iExit) {
			Exit exit = exitList.get(iExit);
			LogConsole.print(exit.toString(), Actions.Action.ADD.name(), exit.getClass().getName());
			List<Point> coords = exit.getListCoord();
			for (Point point : coords) {
				grid.set(point.x, point.y, exit);
				LogConsole.print(point.toString(), Actions.Action.DRAW.name(), point.getClass().getName());
			}
		}
	}
	
	private void addAgents() {
		List<People> peopleList = ReadXml.getPeopleList();
		
		for (int iPeople = 0; iPeople < peopleList.size(); ++iPeople) {
			People people = peopleList.get(iPeople);
			LogConsole.print(people.toString(), Actions.Action.ADD.name(), people.getClass().getName());
			List<Point> coords = people.getListCoord();
			for (Point point : coords) {
				grid.set(point.x, point.y, people);
				LogConsole.print(point.toString(), Actions.Action.DRAW.name(), point.getClass().getName());
			}
		}
	}
	
}

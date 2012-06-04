package Model;

import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;
import Agents.People;
import Components.Exit;
import Components.Space;
import Components.Wall;
import Util.Actions;
import Util.Constants;
import Util.Constants.Direction;
import Util.LogConsole;
import Util.ReadXml;

@SuppressWarnings("serial")
public class Model extends SimState implements AgentDataAccessInterface {
	
	public ObjectGrid2D grid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);

	public Model(long seed) {
		super(seed);
	}

	@Override
	public void start() {
		super.start();
		grid.clear();
		addSpace();
		addWalls();
		addExits();
		addAgents();
	}

	private void addWalls() {
		List<Wall> wallList = ReadXml.getWallList();
		
		for (int iWall = 0; iWall < wallList.size(); ++iWall) {
			Wall wall = wallList.get(iWall);
			LogConsole.print(wall.toString(), Actions.Action.ADD.name(), wall.getClass().getName());
			List<Int2D> coords = wall.getListCoord();
			addToGrid(coords, wall);
		}
	}
	
	private void addExits() {
		List<Exit> exitList = ReadXml.getExitList();
		
		for (int iExit = 0; iExit < exitList.size(); ++iExit) {
			Exit exit = exitList.get(iExit);
			LogConsole.print(exit.toString(), Actions.Action.ADD.name(), exit.getClass().getName());
			List<Int2D> coords = exit.getListCoord();
			addToGrid(coords, exit);
		}
	}
	
	private void addAgents() {
		List<People> peopleList = ReadXml.getPeopleList();
		
		for (int iPeople = 0; iPeople < peopleList.size(); ++iPeople) {
			People people = peopleList.get(iPeople);
			LogConsole.print(people.toString(), Actions.Action.ADD.name(), people.getClass().getName());
			List<Int2D> coords = people.getListCoord();
			addToGrid(coords, people);
			schedule.scheduleOnce(people);
		}
	}
	
	private void addSpace() {
		for(int x=0; x<grid.getWidth(); ++x) {
			for(int y=0; y<grid.getHeight(); ++y) {
					grid.set(x, y, new Space());
			}
		}
	}

	@Override
	public boolean canSeeFire(People p) {
		return false;
	}

	@Override
	public void someoneScreams(People p) {	
	}

	@Override
	public Int2D getFirePosition() {
		return null;
	}

	@Override
	public boolean canMakeOneStepFront(People p) {
		int nextLX = p.eyeX, nextLY = p.eyeY;
		int nextRX = p.eyeX, nextRY = p.eyeY;
		switch (p.direction) {
		case NORTH:
			nextLY--;
			nextRX = nextLX + 1;
			nextRY = nextLY;
			break;
		case SOUTH:
			nextLY++;
			nextRX = nextLX - 1;
			nextRY = nextLY;
			break;
		case EAST:
			nextLX++;
			nextRX = nextLX;
			nextRY = nextLY + 1;
			break;
		case WEST:
			nextLX--;
			nextRX = nextLX;
			nextRY = nextLY - 1;
			break;	
		}
		
		if (grid.get(nextLX, nextLY) != null) return false;
		if (grid.get(nextRX, nextRY) != null) return false;
		
		return true;
	}

	@Override
	public boolean canMakeOneStepTo(Direction direction, People p) {
		List<Int2D> coords = p.getListCoord();
		
		switch (direction) {
		case NORTH:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x, coord.y-1); 
				if (obj != p && obj != null) return false;
			}
			break;
		case SOUTH:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x, coord.y+1); 
				if (obj != p && obj != null) return false;
			}
			break;
		case EAST:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x+1, coord.y); 
				if (obj != p && obj != null) return false;
			}
			break;
		case WEST:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x-1, coord.y); 
				if (obj != p && obj != null) return false;
			}
			break;
		}
		
		return true;
	}

	@Override
	public void removeFromGrid(List<Int2D> coords) {
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, null);
		}		
	}

	@Override
	public void addToGrid(List<Int2D> coords, Object obj) {
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, obj);
		}
	}	
}

package Model;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;
import sim.util.IntBag;
import Agents.Fire;
import Agents.People;
import Components.Arrow;
import Components.Exit;
import Components.Shape;
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
	
	private List<Int2D> firePosition = new ArrayList<Int2D>();

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
		addArrows();
	}
	
	private void addSpace() {
		Space space = new Space();
		for (int x = 0; x < grid.getWidth(); ++x) {
			for (int y = 0; y < grid.getHeight(); ++y) {
					grid.set(x, y, space);
			}
		}
	}
	
	public void removeSpace() {
		for (int x = 0; x < grid.getWidth(); ++x) {
			for (int y = 0; y < grid.getHeight(); ++y) {
				if (grid.get(x, y) instanceof Space) grid.set(x, y, null);
			}
		}
	}
	
	public void putFire(int hearthX, int hearthY) {
		Int2D hearth = new Int2D(hearthX, hearthY);
		IntBag xBag = new IntBag(), yBag = new IntBag();
		grid.getNeighborsHamiltonianDistance(hearthX, hearthY, 1, false, xBag, yBag);
		
		Fire fire = new Fire(hearth);
		firePosition.add(hearth);
		schedule.scheduleOnce(fire);
		LogConsole.print(fire.toString(), Actions.Action.ADD.name(), fire.getClass().getName());
		
		List<Int2D> fireCoords = new ArrayList<Int2D>();
		fireCoords.add(hearth);
		for (int i = 0; i < xBag.size(); ++i) {
			int x = xBag.get(i), y = yBag.get(i); 
			if (grid.get(x, y) == null) {
				grid.set(x, y, fire);
				firePosition.add(new Int2D(x, y));
				fireCoords.add(new Int2D(x, y));
			}
		}
		fire.setListCoords(fireCoords);
	}
	
	public void addFirePosition(Int2D position) {
		firePosition.add(position);
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
	
	@Override
	public void addToGrid(List<Int2D> coords, Object obj) {
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, obj);
		}
	}
	
	@Override
	public void removeFromGrid(List<Int2D> coords) {
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, null);
		}		
	}	

	private void addArrows() {
		List<Arrow> arrowList = ReadXml.getArrowList();
		
		for (int iArrow = 0; iArrow < arrowList.size(); ++iArrow) {
			Arrow arrow = arrowList.get(iArrow);
			LogConsole.print(arrow.toString(), Actions.Action.ADD.name(), arrow.getClass().getName());
			List<Int2D> coords = arrow.getListCoord();
			addToGrid(coords, arrow);
		}
	}
	
	
	/**
	 * It checks if there're some walls in the given {@link People}'s vision field and gives their direction from the {@link People}'s point of view
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return Position of walls that p can see. Can be empty 
	 */
	private ArrayList<Direction> getWallsAround(People ppl)
	{
		ArrayList<Direction> result = new ArrayList<Direction>();
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		for (Object obj : fields) {
			if (obj instanceof Wall) result.add(getShapeDirectionFromPeople(ppl, (Wall) obj));
		}
		
		return result;
	}
	
	
	private Direction getShapeDirectionFromPeople(People ppl, Shape shp)
	{
		List<Int2D> shpCoords = shp.getListCoord();
		Int2D shpMid = shpCoords.get(shpCoords.size()/2);
		
		int diffY = shpMid.y - ppl.eyeY, diffX = shpMid.x - ppl.eyeX;
		
		if (Math.abs(diffY) > Math.abs(diffX)) {
			if (diffY < 0) return Direction.NORTH;
			else return Direction.SOUTH;
		} else {
			if (diffX > 0) return Direction.EAST;
			else return Direction.WEST;
		}
	}
	
		
	/**
	 * It returns all objects that a given {@link People} ppl can see
	 * 
	 * @param ppl The observer
	 * 
	 * @return An {@link ArrayList} of {@link Object} which are seeable objects by ppl
	 */
	private ArrayList<Object> getPeopleVisualField(People ppl)
	{
		int vision = ppl.getVisionAbility();
		Object obj = null;
		ArrayList<Object> field = new ArrayList<Object>();
		
		switch(ppl.direction) {
		
		case NORTH:
			for (int j = ppl.eyeY - vision; j <= ppl.earY + vision; j++) {
				for (int i = ppl.eyeX - vision; i <= ppl.eyeX+1 + vision; i++) {
					obj = grid.get(i, j);
					if (obj != ppl && obj != null) field.add(obj);
				}
			}
			break;		

		case SOUTH:
			for (int j = ppl.earY - vision; j <= ppl.eyeY + vision; j++) {
				for (int i = ppl.eyeX-1 - vision; i <= ppl.eyeX + vision; i++) {
					obj = grid.get(i, j);
					if (obj != ppl && obj != null) field.add(obj);
				}
			}
			break;
			
		case EAST:
			for (int j = ppl.eyeY - vision; j <= ppl.eyeY+1 + vision; j++) {
				for (int i = ppl.earX - vision; i <= ppl.eyeX + vision; i++) {
					obj = grid.get(i, j);
					if (obj != ppl && obj != null) field.add(obj);
				}
			}
			break;
			
		case WEST:
			for (int j = ppl.eyeY-1 - vision; j <= ppl.eyeY + vision; j++) {
				for (int i = ppl.eyeX - vision; i <= ppl.earX + vision; i++) {
					obj = grid.get(i, j);
					if (obj != ppl && obj != null) field.add(obj);
				}
			}
			break;
		}
		
		return field;
	}
	
	@Override
	public boolean canSeeFire(People ppl) {
		ArrayList<Object> field = getPeopleVisualField(ppl);
		ArrayList<Direction> wallsDirection = getWallsAround(ppl);
		
		if (wallsDirection.size() <= 0) {
			for (Object obj : field) {
				if (obj instanceof Fire) return true;
			}	
			return false;
		} else {
			// TO DO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			return false;
		}
	}
		
	@Override
	public void someoneScreams(People ppl) {
		ArrayList<Object> field = getPeopleVisualField(ppl);
		for (Object obj : field) {
			if (obj instanceof People) ((People) obj).hearScream();
		}
	}

	@Override
	public Int2D getFirePosition() {
		return firePosition.get(0);
	}
	
	@Override
	public boolean canMakeOneStepFront(People ppl) {
		switch (ppl.direction) {
		case NORTH:
			if (grid.get(ppl.eyeX, ppl.eyeY-1) == null && grid.get(ppl.eyeX+1, ppl.eyeY-1) == null) return true;
		case SOUTH:
			if (grid.get(ppl.eyeX, ppl.eyeY+1) == null && grid.get(ppl.eyeX-1, ppl.eyeY+1) == null) return true;
		case EAST:
			if (grid.get(ppl.eyeX+1, ppl.eyeY) == null && grid.get(ppl.eyeX+1, ppl.eyeY+1) == null) return true;
		case WEST:
			if (grid.get(ppl.eyeX-1, ppl.eyeY) == null && grid.get(ppl.eyeX-1, ppl.eyeY-1) == null) return true;
		}
		return false;
	}

	@Override
	public boolean canMakeOneStepTo(Direction direction, People ppl) {
		List<Int2D> coords = ppl.getListCoord();
		
		switch (direction) {
		case NORTH:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x, coord.y-1); 
				if (obj != ppl && obj != null) return false;
			}
			break;
		case SOUTH:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x, coord.y+1); 
				if (obj != ppl && obj != null) return false;
			}
			break;
		case EAST:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x+1, coord.y);
				if (obj != ppl && obj != null) return false;
			}
			break;
		case WEST:
			for (Int2D coord : coords) {
				Object obj = grid.get(coord.x-1, coord.y); 
				if (obj != ppl && obj != null) return false;
			}
			break;
		}
		
		return true;
	}

	@Override
	public ArrayList<People> getPeopleAround(People ppl) {
		// TODO Auto-generated method stub
		return new ArrayList<People>();
	}

	@Override
	public Exit canSeeAnExit(People ppl) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arrow canSeeAnArrow(People ppl) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

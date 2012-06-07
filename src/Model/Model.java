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

	private void addArrows(){
		List<Arrow> arrowList = ReadXml.getArrowList();
		
		for (int iArrow = 0; iArrow < arrowList.size(); ++iArrow) {
			Arrow arrow = arrowList.get(iArrow);
			LogConsole.print(arrow.toString(), Actions.Action.ADD.name(), arrow.getClass().getName());
			List<Int2D> coords = arrow.getListCoord();
			addToGrid(coords, arrow);
		}
	}
	
	
	
	
	
	/**
	 * It checks if there're  some walls in the given {@link People}'s vision field and gives their direction from the {@link People}'s point of view
	 * 
	 * @param p The given {@link People}
	 * 
	 * @return Position of walls that p can see. Can be empty 
	 */
	private ArrayList<Direction> getWallsAround(People p)
	{
		ArrayList<Direction> result = new ArrayList<Direction>();
		ArrayList<Object> fields = getPeopleFieldsOfView(p);
		for(int i = 0; i < fields.size(); i++)
		{
			if(fields.get(i) instanceof Wall)
			{
				result.add(getShapeDirectionFromPeople(p, (Wall) fields.get(i)));
			}
		}
		
		return result;
	}
	
	
	private Direction getShapeDirectionFromPeople(People p, Shape s)
	{
		if(s.getListCoord().get(0).y < p.getListCoord().get(0).y)
		{
			return Direction.NORTH;
		}
		else if(s.getListCoord().get(0).y > p.getListCoord().get(0).y)
		{
			return Direction.SOUTH;
		}
		else if(s.getListCoord().get(0).x < p.getListCoord().get(0).x)
		{
			return Direction.WEST;
		}
		else if(s.getListCoord().get(0).x > p.getListCoord().get(0).x)
		{
			return Direction.EAST;
		}
		else
		{
			return Direction.UNKNOWN;
		}
	}
	
	
	
	/**
	 * It returns all objects that a given {@link People} p can see
	 * 
	 * @param p The observer
	 * 
	 * @return An {@link ArrayList} of {@link Object} which are seeable objects by p
	 */
	private ArrayList<Object> getPeopleFieldsOfView(People p)
	{
		List<Int2D> coord = p.getListCoord();
		int screamingAbility = p.getScreamsAbility();
		Object tmp = null;
		ArrayList<Object> result = new ArrayList<Object>();
		
		switch(p.getDirection())
		{
		case NORTH:
			for(int i = coord.get(0).y - screamingAbility; i <= coord.get(1).y + screamingAbility; i++)
			{
				for(int j = coord.get(0).x - screamingAbility; j <= coord.get(0).x + screamingAbility + 1; j++)
				{
					tmp = grid.get(i, j);
					if(tmp != p)
					{
						result.add(tmp);
					}
				}
			}
			break;
			
		case SOUTH:
			for(int i = coord.get(1).y - screamingAbility; i <= coord.get(0).y + screamingAbility; i++)
			{
				for(int j = coord.get(0).x - screamingAbility - 1; j <= coord.get(0).x + screamingAbility; j++)
				{
					tmp = grid.get(i, j);
					if(tmp != p)
					{
						result.add(tmp);
					}
				}
			}
			break;
			
		case EAST:
			for(int i = coord.get(0).y - screamingAbility; i <= coord.get(0).y + screamingAbility + 1; i++)
			{
				for(int j = coord.get(1).x - screamingAbility; j <= coord.get(0).x + screamingAbility; j++)
				{
					tmp = grid.get(i, j);
					if(tmp != p)
					{
						result.add(tmp);
					}
				}
			}
			break;
			
		case WEST:
			for(int i = coord.get(0).y - screamingAbility; i <= coord.get(0).y + screamingAbility + 1; i++)
			{
				for(int j = coord.get(1).x - screamingAbility; j <= coord.get(0).x + screamingAbility; j++)
				{
					tmp = grid.get(i, j);
					if(tmp != p)
					{
						result.add(tmp);
					}
				}
			}
			break;
		}
		
		return result;
	}
	
	
	@Override
	public boolean canSeeFire(People p)
	{
		ArrayList<Object> fields = getPeopleFieldsOfView(p);
		ArrayList<Direction> wallsDirection = getWallsAround(p);
		boolean result = false;
		
		if(wallsDirection.size() <= 0)
		{
			for(int i = 0; i < fields.size(); i++)
			{
				if(fields.get(i) instanceof Fire)
				{
					result = true;
				}
			}
			
			return result;
		}
		else
		{
			// TO DO !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			return false;
		}
	}
	
	

	@Override
	public void someoneScreams(People p)
	{
		ArrayList<Object> fields = getPeopleFieldsOfView(p);
		for(int i = 0; i < fields.size(); i++)
		{
			if(fields.get(i) instanceof People)
			{
				((People) fields.get(i)).hearScream();
			}
		}
	}

	@Override
	public Int2D getFirePosition() {
		return firePosition.get(0);
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
	public ArrayList<People> getPeopleAround(People people) {
		// TODO Auto-generated method stub
		
		return new ArrayList<People>();
	}

	@Override
	public Exit canSeeAnExit(People people) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Arrow canSeeAnArrow(People people) {
		// TODO Auto-generated method stub
		return null;
	}
	
}

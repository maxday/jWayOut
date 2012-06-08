package Model;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;
import Agents.Fire;
import Agents.People;
import Components.Arrow;
import Components.Exit;
import Components.Shape;
import Components.Space;
import Components.Wall;
import Util.Actions;
import Util.Constants;
import Util.Utils;
import Util.Constants.Direction;
import Util.LogConsole;
import Util.ReadXml;

@SuppressWarnings("serial")
public class Model extends SimState implements AgentDataAccessInterface {
	
	private ObjectGrid2D grid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
	private ObjectGrid2D hiddenGrid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
	
	public ObjectGrid2D getGrid() {
		return grid;
	}
	
	private List<Int2D> firePositions = new ArrayList<Int2D>();

	public Model(long seed) {
		super(seed);
	}

	@Override
	public void start() {
		super.start();
		
		grid.clear();
		addSpace();
		addWalls();
		addAgents();
		
		hiddenGrid.clear();
		addExits();
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
	
	public void putFire(Int2D hearth) {
		if (Utils.isCoordInGrid(hearth) && grid.get(hearth.x, hearth.y) == null) {
			Fire fire = new Fire(hearth);
			grid.set(hearth.x, hearth.y, fire);
			firePositions.add(hearth);
			schedule.scheduleOnce(fire);
			LogConsole.print(fire.toString(), Actions.Action.ADD.name(), fire.getClass().getName());
		}
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
	
	private void addExits() {
		List<Exit> exitList = ReadXml.getExitList();
		
		for (int iExit = 0; iExit < exitList.size(); ++iExit) {
			Exit exit = exitList.get(iExit);
			LogConsole.print(exit.toString(), Actions.Action.ADD.name(), exit.getClass().getName());
			List<Int2D> coords = exit.getListCoord();
			for (Int2D coord : coords) {
				hiddenGrid.set(coord.x, coord.y, exit);
			}
		}
	}
	
	private void addArrows() {
		List<Arrow> arrowList = ReadXml.getArrowList();
		
		for (int iArrow = 0; iArrow < arrowList.size(); ++iArrow) {
			Arrow arrow = arrowList.get(iArrow);
			LogConsole.print(arrow.toString(), Actions.Action.ADD.name(), arrow.getClass().getName());
			List<Int2D> coords = arrow.getListCoord();
			for (Int2D coord : coords) {
				hiddenGrid.set(coord.x, coord.y, arrow);
			}
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
	
	
	/**
	 * It checks if there're some walls in the given {@link People}'s vision field and gives them from the {@link People}'s point of view
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return Walls that ppl can see. Can be empty 
	 */
	private ArrayList<Wall> getWallsAround(People ppl)
	{
		ArrayList<Wall> walls = new ArrayList<Wall>();
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		for (Object obj : fields) {
			if (obj instanceof Wall) walls.add((Wall) obj);
		}
		return walls;
	}
	
	
	public Direction getShapeDirectionFromPeople(People ppl, Shape shp)
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
					if (Utils.isCoordInGrid(i, j)) {
						obj = grid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
						obj = hiddenGrid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
					}
				}
			}
			break;		

		case SOUTH:
			for (int j = ppl.earY - vision; j <= ppl.eyeY + vision; j++) {
				for (int i = ppl.eyeX-1 - vision; i <= ppl.eyeX + vision; i++) {
					if (Utils.isCoordInGrid(i, j)) {
						obj = grid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
						obj = hiddenGrid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
					}
				}
			}
			break;
			
		case EAST:
			for (int j = ppl.eyeY - vision; j <= ppl.eyeY+1 + vision; j++) {
				for (int i = ppl.earX - vision; i <= ppl.eyeX + vision; i++) {
					if (Utils.isCoordInGrid(i, j)) {
						obj = grid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
						obj = hiddenGrid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
					}
				}
			}
			break;
			
		case WEST:
			for (int j = ppl.eyeY-1 - vision; j <= ppl.eyeY + vision; j++) {
				for (int i = ppl.eyeX - vision; i <= ppl.earX + vision; i++) {
					if (Utils.isCoordInGrid(i, j)) {
						obj = grid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
						obj = hiddenGrid.get(i, j);
						if (obj != ppl && obj != null) field.add(obj);
					}
				}
			}
			break;
		}
		
		
		Wall w = null;
		Shape s = null;
		// Is there a wall in the people's visual field ?
		for(int i = 0; i < field.size(); i++)
		{
			if(field.get(i) instanceof Wall)
			{
				w = (Wall) field.get(i);
				
				// i is a wall, so let's check if it covers an object or not in the list
				for(int j = 0; j < field.size(); j++)
				{
					if(field.get(j) instanceof Shape)
					{
						s = (Shape) field.get(j);
						
						if(getShapeDirectionFromPeople(ppl, w) == getShapeDirectionFromPeople(ppl, s) && isObjectBeyondTheWall(s, w, ppl))
						{
							field.remove(j);
						}
					}
				}
			}
		}
		
		return field;
	}
	
	
	/**
	 * It tells if the given {@link Shape} is beyond the given {@link Wall} from the {@link People} point of view
	 * 
	 * @return A boolean telling if the given {@link Shape} is beyond the given {@link Wall} from the {@link People} point of view
	 */
	private boolean isObjectBeyondTheWall(Shape s, Wall w, People p)
	{
		Direction wallDir = getShapeDirectionFromPeople(p, w);
		switch(wallDir)
		{
		case NORTH:
			if(s.getListCoord().get(0).y < w.getListCoord().get(0).y)
			{
				return true;
			}
			break;
		case SOUTH:
			if(s.getListCoord().get(0).y > w.getListCoord().get(0).y)
			{
				return true;
			}
			break;
		case EAST:
			if(s.getListCoord().get(0).x > w.getListCoord().get(0).x)
			{
				return true;
			}
			break;
		case WEST:
			if(s.getListCoord().get(0).x < w.getListCoord().get(0).x)
			{
				return true;
			}
			break;
		}
		
		return false;
	}
	
	
	@Override
	public boolean canSeeFire(People ppl)
	{
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		// For each object
		for (Object obj : fields) {
			// If there's a fire
			if (obj instanceof Fire) return true;
		}
		return false;
	}
		
	@Override
	public void someoneScreams(People ppl)
	{
		ArrayList<Object> field = getPeopleVisualField(ppl);
		for (Object obj : field) {
			if (obj instanceof People) ((People) obj).hearScream();
		}
	}

	@Override
	public Int2D getFirePosition() {
		return firePositions.get(0);
	}
	
	@Override
	public boolean canMakeOneStepFront(People ppl) {
		switch (ppl.direction) {
		case NORTH:
			if (grid.get(ppl.eyeX, ppl.eyeY-1) == null && grid.get(ppl.eyeX+1, ppl.eyeY-1) == null)
				return true;
		case SOUTH:
			if (grid.get(ppl.eyeX, ppl.eyeY+1) == null && grid.get(ppl.eyeX-1, ppl.eyeY+1) == null)
				return true;
		case EAST:
			if (grid.get(ppl.eyeX+1, ppl.eyeY) == null && grid.get(ppl.eyeX+1, ppl.eyeY+1) == null)
				return true;
		case WEST:
			if (grid.get(ppl.eyeX-1, ppl.eyeY) == null && grid.get(ppl.eyeX-1, ppl.eyeY-1) == null)
				return true;
		}
		ppl.isBlocked = true;
		return false;
	}

	@Override
	public boolean canMakeOneStepTo(Direction direction, People ppl)
	{
		List<Int2D> coords = ppl.getListCoord();
		
		switch (direction)
		{
		case NORTH:
			for (Int2D coord : coords)
			{
				Object obj = grid.get(coord.x, coord.y-1); 
				if (obj != ppl && obj != null)
				{
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case SOUTH:
			for (Int2D coord : coords)
			{
				Object obj = grid.get(coord.x, coord.y+1); 
				if (obj != ppl && obj != null)
				{
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case EAST:
			for (Int2D coord : coords)
			{
				Object obj = grid.get(coord.x+1, coord.y);
				if (obj != ppl && obj != null)
				{
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case WEST:
			for (Int2D coord : coords)
			{
				Object obj = grid.get(coord.x-1, coord.y); 
				if (obj != ppl && obj != null)
				{
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		}
		
		return true;
	}

	@Override
	public ArrayList<People> getPeopleAround(People ppl)
	{
		ArrayList<People> seenPeople = new ArrayList<People>();
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		for (Object obj : fields) {
			if (obj instanceof People) seenPeople.add((People) obj);
		}
		return seenPeople;
	}

	@Override
	public Exit canSeeAnExit(People ppl)
	{
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		for (Object obj : fields) {
			if (obj instanceof Exit) return ((Exit) obj);
		}
		return null;
	}

	@Override
	public Arrow canSeeAnArrow(People ppl)
	{
		ArrayList<Object> fields = getPeopleVisualField(ppl);
		for (Object obj : fields) {
			if (obj instanceof Arrow) return ((Arrow) obj);
		}
		return null;
	}

	@Override
	public boolean isNearArrow(People p, Arrow a)
	{
		switch(getShapeDirectionFromPeople(p, a))
		{
		case NORTH:
			if(p.eyeY - a.getListCoord().get(0).y <= Constants.DISTANCE_TO_ARROW)
				return true;
			break;
			
		case SOUTH:
			if(a.getListCoord().get(0).y - p.eyeY <= Constants.DISTANCE_TO_ARROW)
				return true;
			break;
			
		case WEST:
			if(p.eyeX - a.getListCoord().get(0).x <= Constants.DISTANCE_TO_ARROW)
				return true;
			break;
			
		case EAST:
			if(a.getListCoord().get(0).x - p.eyeX <= Constants.DISTANCE_TO_ARROW)
				return true;
			break;
		}
		
		return false;
	}
	
}

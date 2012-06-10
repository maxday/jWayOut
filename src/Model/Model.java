package Model;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.field.grid.ObjectGrid2D;
import sim.util.Int2D;
import sim.util.IntBag;
import Agents.Fire;
import Agents.People;
import Components.Door;
import Components.Exit;
import Components.Space;
import Components.Wall;
import Util.Actions;
import Util.Constants;
import Util.Constants.Direction;
import Util.LogConsole;
import Util.ReadXml;
import Util.Utils;

@SuppressWarnings("serial")
public class Model extends SimState implements AgentDataAccessInterface {
	
	private ObjectGrid2D grid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
	private ObjectGrid2D hiddenGrid = new ObjectGrid2D(Constants.GRID_WIDTH, Constants.GRID_HEIGHT);
	
	private List<Wall> wallList = new ArrayList<Wall>();
	private List<People> peopleList = new ArrayList<People>();
	private List<Door> doorList = new ArrayList<Door>();
	private List<Exit> exitList = new ArrayList<Exit>();
	
	public Model(long seed)
	{
		super(seed);
	}
	
	@Override
	public void start()
	{
		super.start();
		
		grid.clear();
		addSpace();
		addWalls();
		addAgents();
		
		hiddenGrid.clear();
		addDoors();
		addExits();
	}
	
	
	/*
	 * Methods to add/remove Objects on the grid/hidden grid
	 */
	
	@Override
	public void addToGrid(List<Int2D> coords, Object obj)
	{
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, obj);
		}
	}
	
	@Override
	public void addToGridIfEmpty(List<Int2D> coords, Object obj)
	{
		for (int i = 0; i < coords.size(); ) {
			Int2D coord = coords.get(i);
			if (grid.get(coord.x, coord.y) == null) {
				grid.set(coord.x, coord.y, obj);
				i++;
			} else {
				coords.remove(i);
			}
		}
	}
	
	@Override
	public void removeFromGrid(List<Int2D> coords)
	{
		for (Int2D coord : coords) {
			grid.set(coord.x, coord.y, null);
		}		
	}
	
	private void addSpace()
	{
		Space space = new Space();
		for (int x = 0; x < grid.getWidth(); ++x) {
			for (int y = 0; y < grid.getHeight(); ++y) {
					grid.set(x, y, space);
			}
		}
	}
	
	public void removeSpace()
	{
		for (int x = 0; x < grid.getWidth(); ++x) {
			for (int y = 0; y < grid.getHeight(); ++y) {
				if (grid.get(x, y) instanceof Space) grid.set(x, y, null);
			}
		}
	}
	
	public void addFire(Int2D hearth)
	{
		if (Utils.isCoordInGrid(hearth) && grid.get(hearth.x, hearth.y) == null) {
			Fire fire = new Fire(hearth);
			grid.set(hearth.x, hearth.y, fire);
			schedule.scheduleOnce(fire);
			LogConsole.print(fire.toString(), Actions.Action.ADD.name(), fire.getClass().getName());
		}
	}
	
	private void addWalls()
	{
		wallList = ReadXml.getWallList();
		for (Wall wall : wallList) {
			LogConsole.print(wall.toString(), Actions.Action.ADD.name(), wall.getClass().getName());
			List<Int2D> coords = wall.getListCoord();
			addToGrid(coords, wall);
		}
	}
	
	private void addAgents()
	{
		peopleList = ReadXml.getPeopleList();
		for (People people : peopleList) {
			LogConsole.print(people.toString(), Actions.Action.ADD.name(), people.getClass().getName());
			addToGrid(people.getListCoord(), people);
			schedule.scheduleOnce(people);
		}
	}
	
	private void addDoors()
	{
		doorList = ReadXml.getDoorList();
		for (Door door : doorList) {
			LogConsole.print(door.toString(), Actions.Action.ADD.name(), door.getClass().getName());
			for (Int2D coord : door.getListCoord()) {
				hiddenGrid.set(coord.x, coord.y, door);
			}
		}
	}
	
	private void addExits()
	{
		exitList =  ReadXml.getExitList();
		for (Exit exit : exitList) {
			LogConsole.print(exit.toString(), Actions.Action.ADD.name(), exit.getClass().getName());
			for (Int2D coord : exit.getListCoord()) {
				hiddenGrid.set(coord.x, coord.y, exit);
			}
		}
	}
	
	private Int2D getNeighbour(Int2D here, Direction dir)
	{
		Int2D neighbour = null;
		
		if (dir.equals(Direction.NORTH)) neighbour = new Int2D(here.x, here.y-1);
		else if (dir.equals(Direction.SOUTH)) neighbour = new Int2D(here.x, here.y+1);
		else if (dir.equals(Direction.EAST)) neighbour = new Int2D(here.x+1, here.y);
		else if (dir.equals(Direction.WEST)) neighbour = new Int2D(here.x-1, here.y);
		
		if (Utils.isCoordInGrid(neighbour)) return neighbour;
		else return null;
	}
	
	/*
	 * Methods to represent People visual perception (vision)
	 */
	
	@Override
	public List<Int2D> computeVisionField(People ppl)
	{
		List<Int2D> visionField = new ArrayList<Int2D>();
		
		Int2D noseL = null, noseR = null;
		Direction propDirL = null, propDirR = null;
		int propDistL = ppl.getVisionAbility(), propDistR = ppl.getVisionAbility();
		int propDepth = ppl.getVisionAbility()*2, depth;
		
		if (ppl.direction.equals(Direction.NORTH)) {
			noseL = new Int2D(ppl.eyeX, ppl.eyeY-1);
			noseR = new Int2D(ppl.eyeX+1, ppl.eyeY-1);
			propDirL = Direction.WEST;
			propDirR = Direction.EAST;
		} else if (ppl.direction.equals(Direction.SOUTH)) {
			noseL = new Int2D(ppl.eyeX, ppl.eyeY+1);
			noseR = new Int2D(ppl.eyeX-1, ppl.eyeY+1);
			propDirL = Direction.EAST;
			propDirR = Direction.WEST;
		} else if (ppl.direction.equals(Direction.EAST)) {
			noseL = new Int2D(ppl.eyeX+1, ppl.eyeY);
			noseR = new Int2D(ppl.eyeX+1, ppl.eyeY+1);
			propDirL = Direction.NORTH;
			propDirR = Direction.SOUTH;
		} else if (ppl.direction.equals(Direction.WEST)) {
			noseL = new Int2D(ppl.eyeX-1, ppl.eyeY);
			noseR = new Int2D(ppl.eyeX-1, ppl.eyeY-1);
			propDirL = Direction.SOUTH;
			propDirR = Direction.NORTH;
		} else {
			return visionField;
		}
		
		visionField.add(noseL);
		visionField.add(noseR);
		
		depth = 0;
		while (depth < propDepth && !(grid.get(noseL.x, noseL.y) instanceof Wall)) {
			Int2D neighbour = noseL;
			int dist = 1;
			do {
				neighbour = getNeighbour(neighbour, propDirL);
				if (neighbour != null) {
					visionField.add(neighbour);
					dist++;
				}
			} while (neighbour != null && dist < propDistL && grid.get(neighbour.x, neighbour.y) == null);
			propDistL = dist;
			noseL = getNeighbour(noseL, ppl.direction);
			visionField.add(noseL);
			depth++;
		}
		
		depth = 0;
		while (depth < propDepth && !(grid.get(noseR.x, noseR.y) instanceof Wall)) {
			Int2D neighbour = noseR;
			int dist = 1;
			do {
				neighbour = getNeighbour(neighbour, propDirR);
				if (neighbour != null) {
					visionField.add(neighbour);
					dist++;
				}
			} while (neighbour != null && dist < propDistR && grid.get(neighbour.x, neighbour.y) == null);
			propDistR = dist;
			noseR = getNeighbour(noseR, ppl.direction);
			visionField.add(noseR);
			depth++;
		}
		
		return visionField;
	}
	
	@Override
	public List<People> getVisiblePeople(People ppl)
	{
		List<People> visiblePeople = new ArrayList<People>();
		for (Int2D coord : ppl.getVisionField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if ((obj instanceof People) && (obj != ppl)) visiblePeople.add((People) obj);
		}
		return visiblePeople;
	}
	
	@Override
	public List<Door> getVisibleDoors(People ppl)
	{
		List<Door> visibleDoors = new ArrayList<Door>();
		for (Int2D coord : ppl.getVisionField(this)) {
			Object obj = hiddenGrid.get(coord.x, coord.y);
			if (obj instanceof Door) visibleDoors.add((Door) obj);
		}
		return visibleDoors;
	}
	
	@Override
	public boolean canSeeTheFire(People ppl)
	{
		for (Int2D coord : ppl.getVisionField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof Fire) return true;
		}
		return false;
	}
	
	@Override
	public Fire getClosestVisibleFire(People ppl)
	{
		Int2D eye = new Int2D(ppl.eyeX, ppl.eyeY);
		Fire closestFire = null;
		Int2D closestPos = null;
		for (Int2D coord : ppl.getVisionField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof Fire) {
				if (closestFire == null || coord.distance(eye) < closestPos.distance(eye)) {
					closestFire = (Fire) obj;
					closestPos = coord;
				}
			}
		}
		return closestFire;
	}
	
	@Override
	public Exit canSeeAnExit(People ppl)
	{
		for (Int2D coord : ppl.getVisionField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof Exit) return ((Exit) obj);
		}
		return null;
	}
	
	
	/*
	 * Methods to represent People aural perception (hearing)
	 */
	
	@Override
	public List<Int2D> computeHearingField(People ppl)
	{
		int hearingAbl = ppl.getHearingAbility();
		List<Int2D> hearingField = new ArrayList<Int2D>();
		int explored = 0, toExplore = 0;
		IntBag xPos = new IntBag(), yPos = new IntBag();
		
		for (Int2D coord : ppl.getListCoord()) {
			grid.getNeighborsHamiltonianDistance(coord.x, coord.y, 1, false, xPos, yPos);
			for (int i = 0; i < xPos.size(); i++) {
				Int2D newCoord = new Int2D(xPos.get(i), yPos.get(i));
				if (!(grid.get(newCoord.x, newCoord.y) instanceof Wall)) {
					if (!hearingField.contains(newCoord)) hearingField.add(newCoord);
				}
			}
		}
		
		toExplore = hearingField.size();
		hearingAbl--;
		
		while (hearingAbl > 0) {
			for (int i = explored; i < toExplore; i++) {
				Int2D coord = hearingField.get(i);
				grid.getNeighborsHamiltonianDistance(coord.x, coord.y, 1, false, xPos, yPos);
				for (int j = 0; j < xPos.size(); j++) {
					Int2D newCoord = new Int2D(xPos.get(j), yPos.get(j));
					if (!(grid.get(newCoord.x, newCoord.y) instanceof Wall)) {
						if (!hearingField.contains(newCoord)) hearingField.add(newCoord);
					}
				}
			}
			explored = toExplore;
			toExplore = hearingField.size();
			hearingAbl--;
		}
		
		return hearingField;
	}
	
	@Override
	public List<People> getAudiblePeople(People ppl)
	{
		List<People> audiblePeople = new ArrayList<People>();
		for (Int2D coord : ppl.getHearingField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if ((obj instanceof People) && (obj != ppl)) audiblePeople.add((People) obj);
		}
		return audiblePeople;
	}

	@Override
	public boolean canHearTheFire(People ppl)
	{
		for (Int2D coord : ppl.getHearingField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof Fire) return true;
		}
		return false;
	}
	
	@Override
	public Fire getClosestAudibleFire(People ppl)
	{
		Int2D ear = new Int2D(ppl.earX, ppl.earY);
		Fire closestFire = null;
		Int2D closestPos = null;
		for (Int2D coord : ppl.getHearingField(this)) {
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof Fire) {
				if (closestFire == null || coord.distance(ear) < closestPos.distance(ear)) {
					closestFire = (Fire) obj;
					closestPos = coord;
				}
			}
		}
		return closestFire;		
	}

	
	/*
	 * Methods to make People interact with its environment	
	 */

	@Override
	public boolean canMakeOneStepFront(People ppl)
	{
		switch (ppl.direction) {
		case NORTH:
			if (grid.get(ppl.eyeX, ppl.eyeY-1) == null && grid.get(ppl.eyeX+1, ppl.eyeY-1) == null)
				return true;
			break;
		case SOUTH:
			if (grid.get(ppl.eyeX, ppl.eyeY+1) == null && grid.get(ppl.eyeX-1, ppl.eyeY+1) == null)
				return true;
			break;
		case EAST:
			if (grid.get(ppl.eyeX+1, ppl.eyeY) == null && grid.get(ppl.eyeX+1, ppl.eyeY+1) == null)
				return true;
			break;
		case WEST:
			if (grid.get(ppl.eyeX-1, ppl.eyeY) == null && grid.get(ppl.eyeX-1, ppl.eyeY-1) == null)
				return true;
			break;
		}
		ppl.isBlocked = true;
		return false;
	}

	@Override
	public boolean canMakeOneStepTo(Direction direction, People ppl)
	{	
		switch (direction) {
		case NORTH:
			for (Int2D coord : ppl.getListCoord()) {
				Object obj = grid.get(coord.x, coord.y-1); 
				if (obj != ppl && obj != null) {
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case SOUTH:
			for (Int2D coord : ppl.getListCoord()) {
				Object obj = grid.get(coord.x, coord.y+1); 
				if (obj != ppl && obj != null) {
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case EAST:
			for (Int2D coord : ppl.getListCoord()) {
				Object obj = grid.get(coord.x+1, coord.y);
				if (obj != ppl && obj != null) {
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		case WEST:
			for (Int2D coord : ppl.getListCoord()) {
				Object obj = grid.get(coord.x-1, coord.y); 
				if (obj != ppl && obj != null) {
					ppl.isBlocked = true;
					return false;
				}
			}
			break;
		}
		
		return true;
	}
	
	@Override
	public void someoneScreams(People ppl)
	{
		int screamingAbl = ppl.getScreamingAbility();
		IntBag xPos = new IntBag(), yPos = new IntBag();
		grid.getNeighborsHamiltonianDistance(ppl.eyeX, ppl.eyeY, screamingAbl, false, xPos, yPos);
		
		for (int i = 0; i < xPos.size(); i++) {
			Int2D coord = new Int2D(xPos.get(i), yPos.get(i));
			Object obj = grid.get(coord.x, coord.y);
			if (obj instanceof People) {
				People people = (People) obj;
				if (people.getHearingField(this).contains(coord)) people.hearScream();
			}
		}
	}


	/*
	 * Getters/Setters
	 */
	
	public ObjectGrid2D getGrid()
	{
		return grid;
	}

	public List<Wall> getWallList()
	{
		return wallList;
	}

	public List<People> getPeopleList()
	{
		return peopleList;
	}

	public List<Door> getDoorList()
	{
		return doorList;
	}

	public List<Exit> getExitList()
	{
		return exitList;
	}
	
}

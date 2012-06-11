package Agents;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.portrayal.Oriented2D;
import sim.util.Int2D;
import Components.Door;
import Components.Exit;
import Components.Shape;
import Model.AgentDataAccessInterface;
import Model.Model;
import Util.Constants;
import Util.Constants.Direction;
import Util.Utils;


@SuppressWarnings("serial")
public class People implements Steppable, Oriented2D
{
	private Stoppable stop;
	private String name;
	
	// Geographic coordinates
	public int earX;
	public int earY;
	public int eyeX;
	public int eyeY;
	public Direction direction;
	public Direction seenDirection;
	public int numOfFails;
	
	// Status
	public boolean isBlocked;
	private boolean isWarned;
	
	// Abilities
	private int visionAbility;
	private int hearingAbility;
	private int screamingAbility;
	
	// Level
	private int panicLevel;
	private int charismaLevel;
	private int autonomyLevel;
	private int speedLevel;
	
	private boolean isOut;
	
	// Perception fields
	private List<Int2D> visionField = new ArrayList<Int2D>();
	private boolean visionComputed = false;
	private List<Int2D> hearingField = new ArrayList<Int2D>();
	private boolean hearingComputed = false;
	
	
	/**
	 * Constructor
	 * 
	 * @param name The agent's name
	 * @param earX The agent's left eye X-coordinate
	 * @param earY The agent's left eye Y-coordinate
	 * @param eyeX The agent's left ear X-coordinate
	 * @param eyeY The agent's left ear Y-coordinate
	 */
	public People(String name, String earX, String earY, String eyeX, String eyeY)
	{
		this.name = name;
		
		this.earX = Integer.parseInt(earX);
		this.earY = Integer.parseInt(earY);
		this.eyeX = Integer.parseInt(eyeX);
		this.eyeY = Integer.parseInt(eyeY);
		computeDirection();
		
		seenDirection = Direction.UNKNOWN;
		numOfFails = 0;
		
		isOut = false;
		
		isBlocked = false;
		isWarned = false;
		
		// Generates abilities rates
		visionAbility = (int) (getRandomAbility()*1.5);
		hearingAbility = getRandomAbility();
		screamingAbility = getRandomAbility();
		
		// Generates levels rates
		panicLevel = getRandomAbility();
		charismaLevel = getRandomAbility();
		autonomyLevel = getRandomAbility();
		speedLevel = 1;
	}
	
	/**
	 * Setter on the stop attribute
	 * 
	 * @param stop A {@link Stoppable} object associated to this agent
	 */
	public void setStoppable(Stoppable stop) {
		this.stop = stop;
	}
	
	
	/**
	 * This method returns the list of coordinates where is represented this agent on the view
	 * 
	 * @return A list of coordinates
	 */
	public List<Int2D> getListCoord()
	{
		List<Int2D> coords = new ArrayList<Int2D>();
		coords.add(new Int2D(eyeX, eyeY));
		coords.add(new Int2D(earX, earY));
			
		switch (direction) {
		case NORTH:
			coords.add(new Int2D(eyeX+1, eyeY));
			coords.add(new Int2D(earX+1, earY));
			break;
		case SOUTH:
			coords.add(new Int2D(eyeX-1, eyeY));
			coords.add(new Int2D(earX-1, earY));
			break;
		case EAST:
			coords.add(new Int2D(eyeX, eyeY+1));
			coords.add(new Int2D(earX, earY+1));
			break;
		case WEST:
			coords.add(new Int2D(eyeX, eyeY-1));
			coords.add(new Int2D(earX, earY-1));
			break;
		case UNKNOWN:
			break;			
		}
		
		return coords;
	}
	
	
	/**
	 * This method updates the agent's direction according to its current attributes' values
	 */
	private void computeDirection()
	{
		if (eyeX == earX) {
			if (eyeY > earY) direction =  Direction.SOUTH;
			else direction =  Direction.NORTH;
		} else if (eyeY == earY) {
			if (eyeX > earX) direction = Direction.EAST;
			else direction = Direction.WEST;
		} else {
			direction =  Direction.UNKNOWN;
		}
	}
	
	/**
	 * Getter for direction attribute
	 * 
	 * @return The direction attribute
	 */
	public Direction getDirection()
	{
		return direction;
	}
	
	/**
	 * Getter for seenDirection attribute
	 * 
	 * @return The seenDirection attribute
	 */
	public Direction getSeenDirection()
	{
		return seenDirection;
	}
	
	@Override
	public double orientation2D()
	{
		if (direction.equals(Direction.NORTH)) return 3*Math.PI/2;
		if (direction.equals(Direction.SOUTH)) return Math.PI/2;
		if (direction.equals(Direction.EAST)) return 0;
		if (direction.equals(Direction.WEST)) return Math.PI;	
		return 0;
	}
	
	
	/**
	 * This method makes the agent performing a 90 degrees rotation movement in the clockwise direction
	 */
	private void turnClockwise()
	{	
		earX = eyeX;
		earY = eyeY;
		
		if (direction.equals(Direction.NORTH)) eyeX++;
		else if (direction.equals(Direction.SOUTH))	eyeX--;
		else if (direction.equals(Direction.EAST)) eyeY++;
		else if (direction.equals(Direction.WEST)) eyeY--;
		
		computeDirection();
		visionComputed = false;
	}
	
	
	/**
	 * This method makes the agent performing a 90 degrees rotation movement in the counter-clockwise direction
	 */
	private void turnCounterclockwise()
	{
		eyeX = earX;
		eyeY = earY;
		
		if (direction.equals(Direction.NORTH)) earX++;
		else if (direction.equals(Direction.SOUTH))	earX--;
		else if (direction.equals(Direction.EAST)) earY++;
		else if (direction.equals(Direction.WEST)) earY--;
		
		computeDirection();
		visionComputed = false;
	}
	
	/**
	 * This methods makes the agent turning to a given {@link Direction} 
	 * 
	 * @param newDir The {@link Direction} where the agent is supposed to look
	 */
	private void turnTo(Direction newDir)
	{	
		if (direction != newDir) {
			if ((direction == Direction.NORTH && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.WEST) ||
					(direction == Direction.WEST && newDir == Direction.NORTH))	{
				turnClockwise();
			} else if ((direction == Direction.NORTH && newDir == Direction.WEST) ||
					(direction == Direction.WEST && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.NORTH)) {
				turnCounterclockwise();
			} else if ((direction == Direction.NORTH && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.NORTH) ||
					(direction == Direction.WEST && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.WEST)) {
				turnClockwise();
				turnClockwise();
			}
		}
	}
	
	/**
	 * This method makes the agent walking forward
	 */
	private void oneStepFront()
	{
		earX = eyeX;
		earY = eyeY;
		
		if (direction.equals(Direction.NORTH)) eyeY--;
		else if (direction.equals(Direction.SOUTH))	eyeY++;
		else if (direction.equals(Direction.EAST)) eyeX++;
		else if (direction.equals(Direction.WEST)) eyeX--;
		
		visionComputed = false;
		hearingComputed = false;
	}
	
	
	/**
	 * It generates a random ability's rate, which can take value from 1 to 10 (both included)
	 * 
	 * @return A random rate
	 */
	private int getRandomAbility()
	{
		// return 5;
		Random generator = new Random();
		return (Constants.MIN_ABILITY + generator.nextInt(Constants.MAX_ABILITY - Constants.MIN_ABILITY + 1));
	}
	
	/**
	 * Getter for the visionAbility attribute
	 * 
	 * @return The agent's vision ability
	 */
	public int getVisionAbility()
	{
		return visionAbility;
	}
	
	/**
	 * Getter for the hearingAbility attribute
	 * 
	 * @return The agent's hearing ability
	 */
	public int getHearingAbility()
	{
		return hearingAbility;
	}
	
	/**
	 * Getter for the screamingAbility attribute
	 * 
	 * @return The agent's screaming ability
	 */
	public int getScreamingAbility()
	{
		return screamingAbility;
	}
	
	/**
	 * Getter for the panic level attribute
	 * 
	 * @return The agent's panic level
	 */
	public int getPanicLevel()
	{
		return panicLevel;
	}
	
	/**
	 * Getter for the charismaLevel attribute
	 * 
	 * @return The agent's charisma level
	 */
	public int getCharismaLevel()
	{
		return charismaLevel;
	}
	
	/**
	 * Getter for the autonomyLevel attribute
	 * 
	 * @return The agent's autonomy level
	 */
	public int getAutonomyLevel()
	{
		return autonomyLevel;
	}
	
	/**
	 * Getter for the speedLevel attribute
	 * 
	 * @return The agent's speed level
	 */
	public int getSpeedLevel()
	{
		return speedLevel;
	}
	
	/**
	 * Tells if this people is in warn state or not
	 * 
	 * @return A boolean telling if the agent is in warning state
	 */
	public boolean isWarned()
	{
		return isWarned;
	}
	
	/**
	 * This method returns a list of all fields seen by this agent
	 * 
	 * @param model The associated model
	 * 
	 * @return A list of 2D coordinates
	 */
	public List<Int2D> getVisionField(AgentDataAccessInterface model)
	{
		if (!visionComputed) {
			visionField = model.computeVisionField(this);
			visionComputed = true;
		}
		return visionField;
	}
	
	/**
	 * This method returns a list of all fields heard by this agent
	 * 
	 * @param model The associated model
	 * 
	 * @return A list of 2D coordinates
	 */
	public List<Int2D> getHearingField(AgentDataAccessInterface model)
	{
		if (!hearingComputed) {
			hearingField = model.computeHearingField(this);
			hearingComputed = true;
		}
		return hearingField;
	}
	
	@Override
	public void step(SimState state)
	{
		if (state instanceof AgentDataAccessInterface) {
			AgentDataAccessInterface model = (AgentDataAccessInterface) state;
			
			if (!model.removeFromGrid(getListCoord(), this)) {
				stop.stop();
				isOut = true;
			}
			
			updateStatus(model);
			scream(model);
			if(isWarned)
			{
				move(model);
			}
			else
			{
				randomMove(model);
			}
			
			if (!isOut) {
				model.addToGridIfEmpty(getListCoord(), this);
			}
		}
	}
	
	
	/**
	 * The agent screams if it is in a warmed status (i.e. if it has seen any fire)
	 * 
	 * @param model The model where the agent is stored
	 */
	private void scream(AgentDataAccessInterface model)
	{
		if (isWarned) {
			model.someoneScreams(this);
		}
	}
	

	/**
	 * It updates the agent's status, considering its environment
	 * 
	 * @param model The model to which the agent can ask informations
	 */
	private void updateStatus(AgentDataAccessInterface model)
	{
		if (model.canSeeTheFire(this) || model.canHearTheFire(this)) {
			isWarned = true;
			scream(model);
			incrementPanicLevel(true);
		}
	}
	
	
	/**
	 * This is the main algorithm used by the agent
	 * 
	 * @param model The associated model
	 */
	private void move(AgentDataAccessInterface model)
	{
		
		if(isBlocked)
		{
			if(seenDirection != Direction.UNKNOWN)
			{
				numOfFails++;
			}
			
			if(numOfFails >= 10)
			{
				seenDirection = Direction.UNKNOWN;
				numOfFails = 0;
			}
			
			Model m = (Model) model;
			try {
				if((direction == Direction.NORTH && m.getGrid().get(eyeX, eyeY-1) == null) || (direction == Direction.SOUTH && m.getGrid().get(eyeX-1, eyeY+1) == null))
				{
					goToByOneStep(model, Direction.WEST);
				}
				else if((direction == Direction.NORTH && m.getGrid().get(eyeX+1, eyeY-1) == null) || (direction == Direction.SOUTH && m.getGrid().get(eyeX, eyeY+1) == null))
				{
					goToByOneStep(model, Direction.EAST);
				}
				else if((direction == Direction.WEST && m.getGrid().get(eyeX-1, eyeY-1) == null) || (direction == Direction.EAST && m.getGrid().get(eyeX+1, eyeY) == null))
				{
					goToByOneStep(model, Direction.NORTH);
				}
				else if((direction == Direction.WEST && m.getGrid().get(eyeX-1, eyeY) == null) || (direction == Direction.EAST && m.getGrid().get(eyeX+1, eyeY+1) == null))
				{
					goToByOneStep(model, Direction.SOUTH);
				}
				else
				{
					randomMoveOneStep(model);
				}
			} catch (Exception e) {
				randomMoveOneStep(model);
			}
			
			isBlocked = false;
			saySomething("I got unblocked");
		}
		
		// Can the agent see an exit ?
		Exit exit = model.canSeeAnExit(this);
		if(exit != null)
		{
			saySomething("I go to the exit");
			// The agent can see an exit, so it goes to its direction
			// goToComponent(model, exit);
			if(goToExit(model, exit))
			{
				stop.stop();
				model.removeFromGrid(getListCoord(), this);
				isOut = true;
			}
		}
		else
		{
			// He cannot see any exit
			// So, now, if there's a fire around, it will escape from it
			if(!escapeFromFire(model))
			{
				// The agent cannot see any fire around it
				// Can he see a door ?
				List<Door> doors = doorFilter(model.getVisibleDoors(this));
				if(doors.size() > 0)
				{
					// The agent can see a door at least
					
					if(model.canMakeOneStepTo(doors.get(0).getDoorDirection(), this))
					{
						saySomething("I follow the door direction");
						seenDirection = doors.get(0).getDoorDirection();
						goTo(model, doors.get(0).getDoorDirection());
						goTo(model, doors.get(0).getDoorDirection());
					}
					else
					{
						saySomething("I go to the door");
						goToComponent(model, doors.get(0));
					}
				}
				else
				{
					// The agent cannot see any door around it
					// Can the agent interact with others ?
					if(!interactWithOtherAgents(model))
					{
						// Does this agent remember about its last followed direction ?
						if(seenDirection != Direction.UNKNOWN)
						{
							saySomething("I go to the last seen direction");
							// The agent follows it last known direction
							goTo(model, seenDirection);
						}
						else
						{
							saySomething("I perform a random move (no agent around)");
							// If it can't, then, it will perform a random move
							randomMove(model);
						}
					}
				}
			}
			else
			{
				saySomething("I escape from the fire");
			}
		}
	}
	
	
	/**
	 * This method makes the agent going to a given {@link Exit}
	 * It also tells if the agent is currently on an exit field or not
	 * 
	 * @param model The associated model
	 * @param exit The exit the agent is supposed to go to
	 * 
	 * @return A boolean telling if the agent is on an exit field
	 */
	private boolean goToExit(AgentDataAccessInterface model, Exit exit)
	{
		boolean loop = true;
		int i = 0;
		
		while(i < this.speedLevel && loop)
		{
			goToComponentByOneStep(model, exit);
			
			for(Int2D c : exit.getListCoord())
			{
				if((eyeX == c.x && eyeY == c.y) || (earX == c.x && earY == c.y))
				{
					loop = false;
					break;
				}
			}
			
			i++;
		}
		
		return !loop;
	}
	
	
	
	/**
	 * This method filters a given list of {@link Door} in order to take off all "useless" {@link Door}
	 * 
	 * @param doors The original list of {@link Door}
	 * 
	 * @return The filtered list of {@link Door}
	 */
	private List<Door> doorFilter(List<Door> doors)
	{
		ArrayList<Door> result = new ArrayList<Door>(doors);
		
		for (Door door: doors) {
			for (Int2D coord : door.getListCoord()) {
				if (Utils.areDirectionsOpposite(door.getDoorDirection(), Utils.getDirectionFromCoordinates(this, coord))) {
					result.remove(door);
					break;
				}
			}
		}
		
		return result;
	}
	
	
	/**
	 * This method makes the agent going away from the fire, using its perception abilities
	 * It returns a boolean telling if an action has been made
	 * Priority is given to the vision
	 * 
	 * @param model The associated model
	 * 
	 * @return A boolean telling if an action has been done
	 */
	private boolean escapeFromFire(AgentDataAccessInterface model)
	{
		// The agent can see [and/or] hear a fire somewhere
		// Does he see a fire ?
		if(model.canSeeTheFire(this))
		{
			// The agent sees a fire, so it will escape from it
			goTo(model, Utils.getOppositeDirection(Utils.getDirectionFromCoordinates(this, model.getClosestVisibleFire(this).getHearth())));
			return true;
		}
		else if(model.canHearTheFire(this))
		{
			// The agent cannot see any fire, but can hear one
			// So, same, it will escape from it
			goTo(model, Utils.getOppositeDirection(Utils.getDirectionFromCoordinates(this, model.getClosestAudibleFire(this).getHearth())));
			return true;
		}
		else
		{
			// There's no fire around the agent, so he does nothing
			return false;
		}
	}
	
	
	
	/**
	 * This method analyzes the agents that this agent can see [and/or] hear and may make this agent interacting with the others
	 * Priority is given to the vision
	 * 
	 * @param model The associated model
	 * 
	 * @return A boolean telling if an interaction has been made
	 */
	private boolean interactWithOtherAgents(AgentDataAccessInterface model)
	{
		List<People> audiblePeople = model.getAudiblePeople(this);
		List<People> visiblePeople = model.getVisiblePeople(this);
		
		// Can the agent hear [or/and] see someone else ?
		if(audiblePeople.size() <= 0 && visiblePeople.size() <= 0)
		{
			// It cannot see no one, so it does nothing
			return false;
		}
		else
		{
			// Can the agent see someone else ?
			if(visiblePeople.size() > 0)
			{
				makeDecisionWithOthers(model, visiblePeople);
				return true;
			}
			else if(audiblePeople.size() > 0)
			{
				makeDecisionWithOthers(model, audiblePeople);
				return true;
			}
		}
		
		return false;
	}
	
	
	
	/**
	 * This method takes a decision for this agent considering the given list of other agents
	 * It will analyze all of their charisma level, in order to decide if this agent will follow others or take a self decision
	 * 
	 * @param model The associated model
	 * @param others The other agents
	 */
	private void makeDecisionWithOthers(AgentDataAccessInterface model, List<People> others)
	{
		People bestCharismaAgent = getMostCharismaticPeople(others);
		if(bestCharismaAgent == null || bestCharismaAgent.getCharismaLevel() <= this.charismaLevel)
		{
			// This agent is the most charismatic agent
			// Does this agent remember about its last followed direction ?
			if(seenDirection != Direction.UNKNOWN)
			{
				saySomething("I go to the last seen direction");
				// The agent follows it last known direction
				goTo(model, seenDirection);
			}
			else
			{
				saySomething("Random move (no last seen direction)");
				// The agent doesn't remember about any last direction
				// It will perform a random move
				randomMove(model);
			}
		}
		else
		{
			saySomething("I follow some one");
			// bestCharismaAgent is most charismatic than this agent
			// So this agent will follow it
			followPeople(model, bestCharismaAgent);
		}
	}
	
	
	/**
	 * Makes this {@link People} going to a given {@link Shape}
	 * 
	 * @param model The associated model
	 * @param shape The targeted {@link Shape}
	 */
	private void goToComponent(AgentDataAccessInterface model, Shape shape)
	{
		Int2D shapeCoordinates = new Int2D(Utils.getRandomMasonValue(model, shape.getBeginX(), shape.getEndX()),
				Utils.getRandomMasonValue(model, shape.getBeginY(), shape.getEndY()));
		Direction d = Utils.getDirectionFromCoordinates(this, shapeCoordinates);
		goTo(model, d);
	}
	
	
	/**
	 * This method makes the agent going to a given {@link Shape} using only one step
	 * 
	 * @param model The associated model
	 * @param shape The {@link Shape} whom the agent is supposed to go to
	 */
	private void goToComponentByOneStep(AgentDataAccessInterface model, Shape shape)
	{
		Int2D shapeCoordinates = new Int2D(Utils.getRandomMasonValue(model, shape.getBeginX(), shape.getEndX()),
				Utils.getRandomMasonValue(model, shape.getBeginY(), shape.getEndY()));
		Direction d = Utils.getDirectionFromCoordinates(this, shapeCoordinates);
		goToByOneStep(model, d);
	}
	
	
	/**
	 * It makes this {@link People} follow another given {@link People}
	 * 
	 * @param model The associated model
	 * @param p The {@link People} to follow
	 */
	private void followPeople(AgentDataAccessInterface model, People ppl)
	{
		Direction dir = Direction.UNKNOWN;
		
		// Where is p in comparison with this
		int diffX = ppl.eyeX - eyeX, diffY = ppl.eyeY - eyeY;
		if (Math.abs(diffY) > Math.abs(diffX)) {
			if (diffY < 0) dir = Direction.NORTH;
			else dir = Direction.SOUTH;
		} else {
			if (diffX > 0) dir = Direction.EAST;
			else dir = Direction.WEST;
		}
		
		// moves
		goTo(model, dir);
	}
	
	
	/**
	 * It returns the people who has the best charisma level among the given list
	 * 
	 * @param people The list of people it'll inspect
	 * 
	 * @return It returns the people who has the best charisma level. It can return null if people is empty or null
	 */
	private People getMostCharismaticPeople(List<People> peopleList)
	{
		if (peopleList == null || peopleList.size() <= 0) {
			return null;
		} else {
			People max = peopleList.get(0);
			
			for (People people : peopleList) {
				if (people.getCharismaLevel() > max.getCharismaLevel()) max = people;
			}
						
			return max;
		}
	}
	
	/**
	 * This method is called when the agent is too stressed.
	 * It will perform a random move, away from the fire.
	 * 
	 * @param model The associated model
	 */
	private void randomMove(AgentDataAccessInterface model)
	{
		Int2D firePosition = null;
		Fire fire =  model.getClosestAudibleFire(this);
		if (fire == null) firePosition = new Int2D(eyeX, eyeY);
		else firePosition = fire.getHearth();
		List<Direction> decisions = new ArrayList<Direction>();
				
		// Guess possibles moves according to the fire's position
		if (eyeX < firePosition.x) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.WEST);
		} else if (eyeX > firePosition.x) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.EAST);
		} else {
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		}
		
		if (eyeY > firePosition.y) {
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		} else if (eyeY < firePosition.y) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		} else {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
		}
				
		// Moves randomly
		int choice;
		Direction dir;
		do {
			choice = Utils.getRandomMasonValue(model, 0, decisions.size()-1);
			dir = decisions.remove(choice);
		} while (!decisions.isEmpty() && !model.canMakeOneStepTo(dir, this));
		goTo(model, dir);
	}
	
	/**
	 * This method makes the agent performing a random move, using only one step
	 * 
	 * @param model The associated model
	 */
	private void randomMoveOneStep(AgentDataAccessInterface model)
	{
		Int2D firePosition = null;
		Fire fire =  model.getClosestAudibleFire(this);
		if (fire == null) firePosition = new Int2D(eyeX, eyeY);
		else firePosition = fire.getHearth();
		List<Direction> decisions = new ArrayList<Direction>();
				
		// Guess possibles moves according to the fire's position
		if (eyeX < firePosition.x) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.WEST);
		} else if (eyeX > firePosition.x) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.EAST);
		} else {
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		}
		
		if (eyeY > firePosition.y) {
			decisions.add(Direction.SOUTH);
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		} else if (eyeY < firePosition.y) {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.EAST);
			decisions.add(Direction.WEST);
		} else {
			decisions.add(Direction.NORTH);
			decisions.add(Direction.SOUTH);
		}
				
		// Moves randomly
		int choice;
		Direction dir;
		do {
			choice = Utils.getRandomMasonValue(model, 0, decisions.size()-1);
			dir = decisions.remove(choice);
		} while (!decisions.isEmpty() && !model.canMakeOneStepTo(dir, this));
		goToByOneStep(model, dir);
	}
	
	
	/**
	 * It simply goes forward
	 * 
	 * @param model The associated model
	 */
	private void goForward(AgentDataAccessInterface model)
	{
		for (int i = 0; i < speedLevel; i++) {
			if (model.canMakeOneStepFront(this)) oneStepFront();
			else break;
		}
	}
	
	
	/**
	 * This method makes the agent going forward using only one step
	 * 
	 * @param model The associated model
	 */
	private void goForwardByOneStep(AgentDataAccessInterface model)
	{
		if (model.canMakeOneStepFront(this)) oneStepFront();
	}
	
	
	/**
	 * It goes to a given direction
	 * 
	 * @param dir The direction the agent is supposed to go to
	 */
	private void goTo(AgentDataAccessInterface model, Direction dir)
	{
		turnTo(dir);
		goForward(model);
	}
	
	
	/**
	 * This method makes the agent going to a given {@link Direction} using only one step
	 * 
	 * @param model The associated model
	 * @param dir The direction where the agent is supposed to go to
	 */
	private void goToByOneStep(AgentDataAccessInterface model, Direction dir)
	{
		turnTo(dir);
		goForwardByOneStep(model);
	}
		
	
	/**
	 * It increments this people's panic level and updates its speed level according to its new panic level
	 * 
	 * @param strong Tells if the panic level should be incremented strongly or normally
	 */
	private void incrementPanicLevel(boolean strong)
	{
		if (strong) panicLevel += Constants.STRONG_PANIC;
		else panicLevel++;
		
		if (panicLevel > Constants.MAX_PANIC) {
			panicLevel = Constants.MAX_PANIC;
			speedLevel = Constants.AGENT_VERY_HIGH_SPEED;
		} else {
			// Updates speed level according to the panic level
			if (panicLevel <= Constants.MAX_PANIC/4) speedLevel = Constants.AGENT_SLOW_SPEED;
			else if (panicLevel <= Constants.MAX_PANIC/2) speedLevel = Constants.AGENT_NORMAL_SPEED;
			else if (panicLevel <= 3*Constants.MAX_PANIC/4) speedLevel = Constants.AGENT_HIGH_SPEED;
			else speedLevel = Constants.AGENT_VERY_HIGH_SPEED;
		}
	}
		
	/**
	 * It defines what this people should do when he's hearing screams
	 */
	public void hearScream()
	{
		isWarned = true;
		incrementPanicLevel(false);
		speedLevel = Constants.AGENT_HIGH_SPEED;
	}
	
	/**
	 * This method writes a given message to the standard output, prefixed with this agent's name
	 * Useful for debug mode
	 * 
	 * @param something The message to display
	 */
	public void saySomething(String something)
	{
		System.out.println(name + " : " + something);
	}
	
	@Override
	public String toString()
	{
		NumberFormat formater = new DecimalFormat("00");
		return name + " :\t[eyeX = " + formater.format(eyeX) + "; eyeY = " + formater.format(eyeY) + "; earX = " + formater.format(earX) + "; earY = " + formater.format(earY) + "; isWarned = " + isWarned
				+ "; visionAbility = " + visionAbility + "; hearingAbility = " + screamingAbility + ", panicLevel = " + panicLevel
				+ "; charismaLevel = " + charismaLevel + "; autonomyLevel = " + autonomyLevel + "; speedAbility = " + speedLevel + "]";
	}	
	
}

package Agents;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.Oriented2D;
import sim.util.Int2D;
import Components.Arrow;
import Components.Exit;
import Components.Shape;
import Model.AgentDataAccessInterface;
import Util.Constants;
import Util.Constants.Direction;
import Util.Utils;


@SuppressWarnings("serial")
public class People implements Steppable, Oriented2D
{

	// Geographic coordinates
	public int earX;
	public int earY;
	public int eyeX;
	public int eyeY;
	public Direction direction;
	
	// Status
	private boolean isWarned;
	
	// Abilities
	private int visionAbility;
	private int screamingAbility;
	private int panicLevel;
	private int charismaLevel;
	private int autonomyLevel;
	private int speedLevel;
	
	/**
	 * Default constructor
	 */
	public People(int earX, int earY, int eyeX, int eyeY)
	{	
		this.earX = earX;
		this.earY = earY;
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		computeDirection();
		
		isWarned = false;
		
		// Generates abilities' rate
		visionAbility = getRandomAbility();
		screamingAbility = getRandomAbility();
		panicLevel = getRandomAbility();
		charismaLevel = getRandomAbility();
		autonomyLevel = getRandomAbility();
		speedLevel = 1;
	}

	
	/**
	 * It generates a random ability's rate, which can take value from 1 to 10 (both included)
	 * 
	 * @return A random rate
	 */
	private int getRandomAbility()
	{
		Random generator = new Random();
		return (Constants.MIN_ABILITY + generator.nextInt(Constants.MAX_ABILITY - Constants.MIN_ABILITY + 1));
	}
	
	
	/**
	 * Simple getter for the agent's vision ability
	 * 
	 * @return The agent's vision ability
	 */
	public int getVisionAbility()
	{
		return visionAbility;
	}
	
	
	public int getPanicLevel()
	{
		return panicLevel;
	}
	
	
	public int getScreamsAbility()
	{
		return screamingAbility;
	}
	
	
	public int getAutonomyLevel()
	{
		return autonomyLevel;
	}
	
	
	/**
	 * It gives is charisma level
	 * 
	 * @return Its charisma level
	 */
	public int getCharismaLevel()
	{
		return charismaLevel;
	}

	
	/**
	 * It gives an integer which stands for the speed of the agent
	 * 
	 * @return The agent's speed ability
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
	
	
	@Override
	public void step(SimState state)
	{
		if (state instanceof AgentDataAccessInterface) {
			AgentDataAccessInterface model = (AgentDataAccessInterface) state;
			
			updateStatus(model);
			scream(model);
			move(model);
		}
		
		state.schedule.scheduleOnce(this);
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
		if (model.canSeeFire(this)) {
			isWarned = true;
			scream(model);
			incrementPanicLevel(true);
		}
	}
	
	
	/**
	 * The agent will move and take decision, considering its environment and its current status
	 * 
	 * @param model The model where the agent is stored
	 */
	private void move(AgentDataAccessInterface model)
	{
		List<Int2D> coords = getListCoord();
		model.removeFromGrid(coords);	ArrayList<People> seeablePeople = model.getPeopleAround(this);
		
		People bestCharisma = getMostCharismaticPeople(seeablePeople);
		
		if (bestCharisma == null || bestCharisma.getCharismaLevel() < this.getCharismaLevel())
		{
			selfDecision(model);
		}
		else
		{
			// Following the agent who has the best charisma
			followPeople(model, bestCharisma);
		}
		
		coords = getListCoord();
		model.addToGrid(coords, this);
	}
	
	
	/**
	 * This is invoked when the agent is about to take a decision by its own way of thinking
	 * 
	 * @param model The associated model
	 */
	private void selfDecision(AgentDataAccessInterface model)
	{
		Exit exit = model.canSeeAnExit(this);
		if (exit != null)
		{
			// Exit seeable !
			goToComponent(model, exit);
		}
		else
		{
			// No near exit
			Arrow arrow = model.canSeeAnArrow(this);
			if (arrow != null)
			{
				if(model.getShapeDirectionFromPeople(this, arrow) == direction)
				{
					// The arrow is in front of the agent
					if(model.isNearArrow(this, arrow) && model.canMakeOneStepTo(arrow.getDirection(), this))
					{
						// I can go to the direction pointed by the arrow
						goTo(model, arrow.getDirection());
					}
					else
					{
						// The agent have to get a bit closer to the arrow
						goToComponentByOneStep(model, arrow);
					}
				}
				else
				{
					// The arrow is somewhere which isn't in front of the agent
					direction = arrow.getDirection();
					goTo(model, direction);
				}
			}
			else
			{
				// There's no arrow around
				// It has to either perform a random move, or to get out from a room
				if(panicLevel >= Constants.MAX_PANIC)
				{
					// the agent has a so much high level of panic that it can't think correctly and perform a random move
					randomMove(model);
				}
				else
				{
					// It performs a random move, or try to get out from a room if it's located in it
					randomMove(model);
				}
			}
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
		Int2D shapeCoordinates = new Int2D(Utils.getRandomMasonValue(model, shape.getBeginX(), shape.getEndX()), Utils.getRandomMasonValue(model, shape.getBeginY(), shape.getEndY()));
		Direction d = Utils.getDirectionFromCoordinates(this, shapeCoordinates);
		goTo(model, d);
	}
	
	
	private void goToComponentByOneStep(AgentDataAccessInterface model, Shape shape)
	{
		Int2D shapeCoordinates = new Int2D(Utils.getRandomMasonValue(model, shape.getBeginX(), shape.getEndX()), Utils.getRandomMasonValue(model, shape.getBeginY(), shape.getEndY()));
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
	private People getMostCharismaticPeople(ArrayList<People> peopleList)
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
		Int2D firePosition = model.getFirePosition();
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
		int choice = Utils.getRandomMasonValue(model, 0, decisions.size()-1);
		while(!model.canMakeOneStepTo(decisions.get(choice), this))
		{
			choice = Utils.getRandomMasonValue(model, 0, decisions.size()-1);
		}
		goTo(model, decisions.get(choice));
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
		}
	}
	
	
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
		incrementPanicLevel(false);
		speedLevel = Constants.AGENT_HIGH_SPEED;
	}
	

	@Override
	public String toString()
	{
		return "[earX=" + earX + "; earY=" + earY + "; eyeX=" + eyeX
				+ "; eyeY=" + eyeY + "; isWarned=" + isWarned
				+ "; visionAbility=" + visionAbility + "; hearingAbility="
				+ screamingAbility + ", panicLevel=" + panicLevel
				+ "; charismaLevel=" + charismaLevel + "; autonomyLevel="
				+ autonomyLevel + "; speedAbility=" + speedLevel + "]";
	}

	
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

	
	@Override
	public double orientation2D()
	{	
		switch (direction) {
		case NORTH:
			return 3*Math.PI/2;
		case SOUTH:
			return Math.PI/2;
		case EAST:
			return 0;
		case WEST:
			return Math.PI;
		}
		
		return 0;
	}
	
	
	private void turnClockwise()
	{	
		earX = eyeX;
		earY = eyeY;
		
		switch (direction) {
		case NORTH:
			eyeX++;
			break;
		case SOUTH:
			eyeX--;
			break;
		case EAST:
			eyeY++;
			break;
		case WEST:
			eyeY--;
			break;
		}
		
		computeDirection();
	}
	
	
	private void turnCounterclockwise()
	{
		eyeX = earX;
		eyeY = earY;
		
		switch (direction) {
		case NORTH:
			earX++;
			break;
		case SOUTH:
			earX--;
			break;
		case EAST:
			earY++;
			break;
		case WEST:
			earY--;
			break;
		}
		
		computeDirection();
	}
	
	
	private void turnTo(Direction newDir)
	{	
		if(direction != newDir)
		{
			if((direction == Direction.NORTH && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.WEST) ||
					(direction == Direction.WEST && newDir == Direction.NORTH))
			{
				turnClockwise();
			}
			else if((direction == Direction.NORTH && newDir == Direction.WEST) ||
					(direction == Direction.WEST && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.NORTH))
			{
				turnCounterclockwise();
			}
			else if((direction == Direction.NORTH && newDir == Direction.SOUTH) ||
					(direction == Direction.SOUTH && newDir == Direction.NORTH) ||
					(direction == Direction.WEST && newDir == Direction.EAST) ||
					(direction == Direction.EAST && newDir == Direction.WEST))
			{
				turnClockwise();
				turnClockwise();
			}
		}
	}
	
	
	private void oneStepFront()
	{
		earX = eyeX;
		earY = eyeY;
		
		switch (direction) {
		case NORTH:
			eyeY--;
			break;
		case SOUTH:
			eyeY++;
			break;
		case EAST:
			eyeX++;
			break;
		case WEST:
			eyeX--;
			break;
		}
	}
}

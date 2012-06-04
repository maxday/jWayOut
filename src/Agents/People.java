package Agents;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.portrayal.Oriented2D;
import sim.util.Int2D;
import Model.AgentDataAccessInterface;
import Util.Constants;
import Util.Constants.Direction;



@SuppressWarnings("serial")
public class People implements Steppable, Oriented2D
{

	// Geographic coordinates
	public int earX;
	public int earY;
	public int eyeX;
	public int eyeY;
	private Direction direction;
	
	private boolean isWarned;	
	
	// Abilities
	private int visionAbility;
	private int hearingAbility;
	private int panicLevel;
	private int charismaLevel;
	private int autonomyLevel;
	private int speedLevel;
		
	
	/**
	 * Default constructor
	 */
	public People(int earX, int earY, int eyeX, int eyeY)
	{
		// Generates abilities' rate
		visionAbility = getRandomAbility();
		hearingAbility = getRandomAbility();
		panicLevel = getRandomAbility();
		charismaLevel = getRandomAbility();
		autonomyLevel = getRandomAbility();
		speedLevel = 1;
		
		this.earX = earX;
		this.earY = earY;
		this.eyeX = eyeX;
		this.eyeY = eyeY;
		
		computeDirection();
		
		isWarned = false;
	}

	/**
	 * It generates a random ability's rate, which can take value from 1 to 10 (both included)
	 * 
	 * @return A random rate
	 */
	private int getRandomAbility()
	{
		Random generator = new Random();
		return (generator.nextInt(Constants.MAX_ABILITY) + 1);
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

	@Override
	public void step(SimState arg0)
	{
		if (arg0 instanceof AgentDataAccessInterface)
		{
			AgentDataAccessInterface model = (AgentDataAccessInterface) arg0;
			
			updateStatus(model);
			scream(model);
			move(model);
		}
	}
	
	
	/**
	 * The agent screams if it is in a warmed status (i.e. if it has seen any fire)
	 * 
	 * @param model The model where the agent is stored
	 */
	private void scream(AgentDataAccessInterface model)
	{
		if(isWarned)
		{
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
		if (model.canSeeFire(this))
		{
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
		if(panicLevel == Constants.MAX_PANIC)
		{
			randomMove(model);
		}
	}
	
	
	private void randomMove(AgentDataAccessInterface model)
	{
		Point2D firePosition = model.getFirePosition();
		Direction decisions[] = new Direction[3];
		
		if(eyeX < firePosition.getX())
		{
			decisions[0] = Direction.NORTH;
			decisions[1] = Direction.WEST;
			decisions[2] = Direction.SOUTH;
		}
		else if(eyeX > firePosition.getX())
		{
			decisions[0] = Direction.NORTH;
			decisions[1] = Direction.EAST;
			decisions[2] = Direction.SOUTH;
		}
		else if(eyeX > firePosition.getY())
		{
			decisions[0] = Direction.EAST;
			decisions[1] = Direction.WEST;
			decisions[2] = Direction.SOUTH;
		}
		else if(eyeX < firePosition.getY())
		{
			decisions[0] = Direction.NORTH;
			decisions[1] = Direction.WEST;
			decisions[2] = Direction.EAST;
		}
		
		
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
		
		if (panicLevel > Constants.MAX_PANIC)
		{
			panicLevel = Constants.MAX_PANIC;
			speedLevel = Constants.AGENT_VERY_HIGH_SPEED;
		}
		else
		{
			// Updates speed level according to the panic level
			
			if(panicLevel >= 0 && panicLevel <= Constants.MAX_PANIC/4)
			{
				speedLevel = Constants.AGENT_SLOW_SPEED;
			}
			else if(panicLevel <= Constants.MAX_PANIC/4 && panicLevel <= Constants.MAX_PANIC/2)
			{
				speedLevel = Constants.AGENT_NORMAL_SPEED;
			}
			else if(panicLevel <= Constants.MAX_PANIC/2 && panicLevel <= (Constants.MAX_PANIC*(3/4)))
			{
				speedLevel = Constants.AGENT_HIGH_SPEED;
			}
			else
			{
				speedLevel = Constants.AGENT_VERY_HIGH_SPEED;
			}
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
	 * Tells if this people is in warm state or not
	 * 
	 * @return A boolean telling if the agent is in warning state
	 */
	public boolean isWarned()
	{
		return isWarned;
	}

	@Override
	public String toString()
	{
		return "[earX=" + earX + "; earY=" + earY + "; eyeX=" + eyeX
				+ "; eyeY=" + eyeY + "; isWarned=" + isWarned
				+ "; visionAbility=" + visionAbility + "; hearingAbility="
				+ hearingAbility + ", panicLevel=" + panicLevel
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
	
	public void turnClockwise()
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
	
	public void turnCounterclockwise()
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
	}

}

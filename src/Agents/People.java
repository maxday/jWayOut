package Agents;

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
	private int speedAbility;
		
	
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
		speedAbility = 1;
		
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

	@Override
	public void step(SimState arg0)
	{
		if (arg0 instanceof AgentDataAccessInterface) {
			AgentDataAccessInterface model = (AgentDataAccessInterface) arg0;
			updateStatus(model);
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
			model.someoneScreams(this);
			incrementPanicLevel(true);
		}
	}
	
	/**
	 * It increments this people's panic level
	 * 
	 * @param strong Tells if the panic level should be incremented strongly or normally
	 */
	private void incrementPanicLevel(boolean strong)
	{
		if (strong) panicLevel += Constants.STRONG_PANIC;
		else panicLevel++;
		
		if (panicLevel > Constants.MAX_ABILITY) panicLevel = Constants.MAX_ABILITY;
	}
	
	/**
	 * It defines what this people should do when he's hearing screams
	 */
	public void hearScream()
	{
		incrementPanicLevel(false);
		speedAbility = Constants.AGENT_HIGH_SPEED;
	}
	
	/**
	 * It gives an integer which stands for the speed of the agent
	 * 
	 * @return The agent's speed ability
	 */
	public int getSpeedAbility()
	{
		return speedAbility;
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
	
	/**
	 * Sets the state of this people to warned
	 */
	public void setWarned()
	{
		isWarned = true;
	}

	@Override
	public String toString()
	{
		return "[earX=" + earX + "; earY=" + earY + "; eyeX=" + eyeX
				+ "; eyeY=" + eyeY + "; isWarned=" + isWarned
				+ "; visionAbility=" + visionAbility + "; hearingAbility="
				+ hearingAbility + ", panicLevel=" + panicLevel
				+ "; charismaLevel=" + charismaLevel + "; autonomyLevel="
				+ autonomyLevel + "; speedAbility=" + speedAbility + "]";
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

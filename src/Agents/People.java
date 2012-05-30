package Agents;

import java.util.Random;

import Model.AgentDataAccessInterface;
import Util.Constants;

import sim.engine.SimState;
import sim.engine.Steppable;



@SuppressWarnings("serial")
public class People implements Steppable
{
	// Geographic coordinates
	public int x;
	public int y;
	
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
	public People(int x, int y)
	{
		// Generates abilities' rate
		visionAbility = getRandomAbility();
		hearingAbility = getRandomAbility();
		panicLevel = getRandomAbility();
		charismaLevel = getRandomAbility();
		autonomyLevel = getRandomAbility();
		speedAbility = 1;
		
		this.x = x;
		this.y = y;
		
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
		if(arg0 instanceof AgentDataAccessInterface)
		{
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
		if(model.canSeeFire(this))
		{
			model.someoneScreams(this);
			incrementPanicLevel(true);
		}
		else
		{
			
		}
	}
	
	
	
	/**
	 * It increments this people's panic level
	 * 
	 * @param strong Tells if the panic level should be incremented strongly or normally
	 */
	private void incrementPanicLevel(boolean strong)
	{
		if(strong)
		{
			panicLevel += Constants.STRONG_PANIC;
		}
		else
		{
			panicLevel++;
		}
		
		if(panicLevel > Constants.MAX_ABILITY)
		{
			panicLevel = Constants.MAX_ABILITY;
		}
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
	 * @return A boolean telling if the agent is in warm state
	 */
	public boolean isWarned()
	{
		return isWarned;
	}
	
	
	/**
	 * Sets the state of this people to warmed
	 */
	public void setWarned()
	{
		isWarned = true;
	}
	

}

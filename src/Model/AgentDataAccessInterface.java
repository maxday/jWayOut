package Model;

import Agents.People;

/**
 * This is an interface that the model should implement
 * It describes every method which can be called by an agent
 */
public interface AgentDataAccessInterface
{
	/**
	 * It tells if a given people can see any part of a fire
	 * 
	 * @param p The concerned people
	 * @return A boolean telling if the given people can see the fire or not
	 */
	public boolean canSeeFire(People p);
}

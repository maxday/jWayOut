package Model;

import sim.util.Int2D;
import Agents.People;
import Util.Constants.Direction;

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
	
	
	/**
	 * This method is invoked when a people is screaming
	 * Then, it notifies every people who is in the scream's scope
	 * 
	 * @param p The people who is screaming
	 */
	public void someoneScreams(People p);
	
	
	/**
	 * It returns the fire position on the grid
	 * 
	 * @return A Int2D object where is located the fire
	 */
	public Int2D getFirePosition();
	
	public boolean canMakeOneStepFront(People p);
	
	public boolean canMakeOneStepTo(Direction direction);
	
}

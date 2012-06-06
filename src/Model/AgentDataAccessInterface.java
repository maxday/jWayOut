package Model;

import java.util.ArrayList;
import java.util.List;

import sim.util.Int2D;
import Agents.People;
import Components.Exit;
import Components.Arrow;
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
	
	public boolean canMakeOneStepTo(Direction direction, People p);
	
	
	public void removeFromGrid(List<Int2D> coords);
	
	public void addToGrid(List<Int2D> coords, Object obj);
	
	
	/**
	 * It returns all people that the given person can see, according to its abilities
	 * 
	 * @param people The given people
	 * 
	 * @return A list of all seeable people
	 */
	public ArrayList<People> getPeopleAround(People people);
	
	
	
	/**
	 * It checks if the given {@link People} can see an {@link Exit} or not
	 * 
	 * @param people The subject of the action
	 * 
	 * @return Either the seeable {@link Exit} object, either null value if no {@link Exit} can be seen
	 */
	public Exit canSeeAnExit(People people);
	
	
	/**
	 * It checks if the given {@link People} can see an {@link Arrow} or not.
	 * In case the given {@link People} can see more than one {@link Arrow}, the nearest one will be returned.
	 * 
	 * @param people The subject of the action
	 * 
	 * @return Either the nearest seeable {@link Arrow} object, either null value if no {@link Exit} can be seen
	 */
	public Arrow canSeeAnArrow(People people);
	
}

package Model;

import java.util.List;

import sim.util.Int2D;
import Agents.Fire;
import Agents.People;
import Components.Door;
import Components.Exit;
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
	 * @param ppl The concerned people
	 * @return A boolean telling if the given people can see the fire or not
	 */
	public Fire canSeeAFire(People ppl);
	public Fire canHearAFire(People ppl);	
	
	/**
	 * This method is invoked when a people is screaming
	 * Then, it notifies every people who is in the scream's scope
	 * 
	 * @param ppl The people who is screaming
	 */
	public void someoneScreams(People ppl);
	
	public List<Int2D> getVisionField(People ppl);
	public List<Int2D> getHearingField(People ppl);	
	
	public boolean canMakeOneStepFront(People ppl);
	
	public boolean canMakeOneStepTo(Direction direction, People ppl);
	
	public void removeFromGrid(List<Int2D> coords);
	
	public void addToGrid(List<Int2D> coords, Object obj);
	public void addToGridIfEmpty(List<Int2D> coords, Object obj);
	
	
	/**
	 * It returns all people that the given person can see, according to its abilities
	 * 
	 * @param ppl The given people
	 * 
	 * @return A list of all seeable people
	 */
	public List<People> getVisiblePeople(People ppl);
	
	public List<Door> getVisibleDoors(People ppl);
	
	/**
	 * It checks if the given {@link People} can see an {@link Exit} or not
	 * 
	 * @param ppl The subject of the action
	 * 
	 * @return Either the seeable {@link Exit} object, either null value if no {@link Exit} can be seen
	 */
	public Exit canSeeAnExit(People ppl);
	
}

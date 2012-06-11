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
	 * It returns the closest {@link Fire} that the given {@link People} can see
	 * 
	 * @param ppl The observer {@link People}
	 * 
	 * @return The closest visible {@link Fire}, or null value if there's not
	 */
	public Fire getClosestVisibleFire(People ppl);
	
	/**
	 * It returns the closest {@link Fire} that the given {@link People} can hear
	 * 
	 * @param ppl The observer {@link People}
	 * 
	 * @return The closest audible {@link Fire}, or null value if there's not
	 */
	public Fire getClosestAudibleFire(People ppl);
	
	
	/**
	 * This method tells if the given {@link People} can see a {@link Fire} somewhere
	 * 
	 * @param ppl The observer {@link People}
	 * 
	 * @return A boolean telling if the given {@link People} can see a {@link Fire} somewhere
	 */
	public boolean canSeeTheFire(People ppl);
	
	/**
	 * This method tells if the given {@link People} can hear a {@link Fire} somewhere
	 * 
	 * @param ppl The observer {@link People}
	 * 
	 * @return A boolean telling if the given {@link People} can hear a {@link Fire} somewhere
	 */
	public boolean canHearTheFire(People ppl);
	
	/**
	 * This method is invoked when a people is screaming
	 * Then, it notifies every people who is in the scream's scope
	 * 
	 * @param ppl The people who is screaming
	 */
	public void someoneScreams(People ppl);
	
	/**
	 * This method gives a list of coordinates that are what the given {@link People} can see
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return A list of coordinates
	 */
	public List<Int2D> computeVisionField(People ppl);
	
	/**
	 * This method gives a list of coordinates that are what the given {@link People} can hear
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return A list of coordinates
	 */
	public List<Int2D> computeHearingField(People ppl);	
	
	/**
	 * This method tells if a given {@link People} can make one step front or not
	 * 
	 * @param ppl The subject {@link People}
	 * 
	 * @return A boolean telling the given {@link People} can make one step front
	 */
	public boolean canMakeOneStepFront(People ppl);
	
	
	/**
	 * This method tells if a given {@link People} can make one step to a given {@link Direction} or not
	 * 
	 * @param direction The direction the agent is supposed to go to
	 * @param ppl The subject {@link People}
	 * 
	 * @return A boolean telling the given {@link People} can make one step to a given {@link Direction}
	 */
	public boolean canMakeOneStepTo(Direction direction, People ppl);
	

	
	/**
	 * This method removes the given {@link Object} from the grid, located at the given coordinates
	 * 
	 * @param coords The given coordinates
	 * @param obj The given {@link Object} to remove
	 * 
	 * @return It returns false if the given {@link Object} was nowhere on the grid, or true 
	 */
	public boolean removeFromGrid(List<Int2D> coords, Object obj);
	
	/**
	 * This method adds the given {@link Object} to the grid, with it given coordinates
	 * If there's already a component to one the given coordinates, the existing {@link Object} will be erased
	 * 
	 * @param coords The {@link Object}'s coordinates
	 * @param obj The {@link Object} to add
	 */
	public void addToGrid(List<Int2D> coords, Object obj);
	
	/**
	 * This method adds the given {@link Object} to the grid, with it given coordinates
	 * It won't erase a component if it already exists on a given coordinate
	 * 
	 * @param coords The {@link Object}'s coordinates
	 * @param obj The {@link Object} to add
	 */
	public void addToGridIfEmpty(List<Int2D> coords, Object obj);
	
	
	/**
	 * It returns all people that the given {@link People} can see, according to its abilities
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return A list of all seeable {@link People}
	 */
	public List<People> getVisiblePeople(People ppl);
	
	/**
	 * It returns all people that the given {@link Door} can see, according to its abilities
	 * 
	 * @param ppl The given {@link Door}
	 * 
	 * @return A list of all seeable {@link Door}
	 */
	public List<Door> getVisibleDoors(People ppl);
	
	
	/**
	 * It returns all people that the given {@link People} can hear, according to its abilities
	 * 
	 * @param ppl The given {@link People}
	 * 
	 * @return A list of all audible {@link People}
	 */
	public List<People> getAudiblePeople(People ppl);
	
	/**
	 * It checks if the given {@link People} can see an {@link Exit} or not
	 * 
	 * @param ppl The subject of the action
	 * 
	 * @return Either the seeable {@link Exit} object, either null value if no {@link Exit} can be seen
	 */
	public Exit canSeeAnExit(People ppl);
	
}

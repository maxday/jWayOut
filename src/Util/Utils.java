package Util;

import java.util.List;

import sim.engine.SimState;
import sim.util.Int2D;
import Agents.People;
import Model.AgentDataAccessInterface;
import Util.Constants.Direction;

/**
 * This class contains some useful methods
 * All methods are declared as static as the {@link Utils} class isn't supposed to be implemented
 */

public class Utils
{
	/**
	 * It simply generates a random value according to Mason's way of working
	 * 
	 * @param s The {@link AgentDataAccessInterface} to which the simulation is linked
	 * @param min The minimal value which can be taken
	 * @param max The maximal value which can be taken
	 * 
	 * @return A random number between min and max, both included
	 */
	static public int getRandomMasonValue(AgentDataAccessInterface s, int min, int max)
	{
		return min+((SimState) s).random.nextInt(max-min+1);
	}
	
	
	/**
	 * It tells in which direction is a given position in comparison with a given {@link People}
	 * 
	 * @param people The watcher
	 * @param c The targeted position
	 * 
	 * @return The direction where is the given coordinate from the {@link People}'s point of view
	 */
	static public Direction getDirectionFromCoordinates(People people, Int2D c)
	{
		Direction d = Direction.UNKNOWN;
		List<Int2D> pCoord = people.getListCoord();
		
		if(c.y < pCoord.get(0).y)
		{
			d = Direction.NORTH;
		}
		else if(c.y > pCoord.get(0).y)
		{
			d = Direction.SOUTH;
		}
		else if(c.x > pCoord.get(0).x)
		{
			d = Direction.EAST;
		}
		else if(c.x < pCoord.get(0).x)
		{
			d = Direction.WEST;
		}
		
		return d;
	}
	
}

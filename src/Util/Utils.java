package Util;

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
	 * This method tells if a given coordinate is on the grid or outside
	 * 
	 * @param coord The given coordinate
	 * 
	 * @return A boolean telling if a given coordinate is on the grid
	 */
	static public boolean isCoordInGrid(Int2D coord)
	{
		return (coord.x >= 0 && coord.x < Constants.GRID_WIDTH && coord.y >= 0 && coord.y < Constants.GRID_HEIGHT);
	}


	/**
	 * This method tells if a given coordinate is on the grid or outside
	 * 
	 * @param x The x-coordinate
	 * @param y The y-coordinate
	 * 
	 * @return A boolean telling if a given coordinate is on the grid
	 */
	static public boolean isCoordInGrid(int x, int y)
	{
		return (x >= 0 && x < Constants.GRID_WIDTH && y >= 0 && y < Constants.GRID_HEIGHT);
	}


	/**
	 * It simply generates a random value according to Mason's way of working
	 * 
	 * @param s The {@link AgentDataAccessInterface} to which the simulation is linked
	 * @param val1 The min or the max
	 * @param val2 The min or the max
	 * 
	 * @return A random number between val1 and val2, both included
	 */
	static public int getRandomMasonValue(AgentDataAccessInterface s, int val1, int val2)
	{
		return Math.min(val1, val2) + ((SimState) s).random.nextInt(Math.max(val1,  val2)-Math.min(val1, val2)+1);
	}


	/**
	 * It simply generates a random value according to Mason's way of working
	 * 
	 * @param s The {@link SimState} to which the simulation is linked
	 * @param val1 The min or the max
	 * @param val2 The min or the max
	 * 
	 * @return A random number between val1 and val2, both included
	 */
	static public int getRandomMasonValue(SimState s, int val1, int val2)
	{
		return Math.min(val1, val2) + s.random.nextInt(Math.max(val1,  val2)-Math.min(val1, val2)+1);
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
		int diffY = c.y - people.eyeY, diffX = c.x - people.eyeX;
		
		if (Math.abs(diffY) > Math.abs(diffX)) {
			if (diffY < 0) return Direction.NORTH;
			else return Direction.SOUTH;
		} else {
			if (diffX > 0) return Direction.EAST;
			else return Direction.WEST;
		}
	}


	/**
	 * This method gives the opposite {@link Direction} based on a given {@link Direction}
	 * 
	 * @param d The based {@link Direction}
	 * 
	 * @return The d's opposite {@link Direction}
	 */
	static public Direction getOppositeDirection(Direction d)
	{
		switch(d)
		{
		case NORTH:
			return Direction.SOUTH;
		case SOUTH:
			return Direction.NORTH;
		case EAST:
			return Direction.WEST;
		case WEST:
			return Direction.EAST;
		default:
			return Direction.UNKNOWN;
		}
	}


	/**
	 * This method tells if the two given {@link Direction} are opposite
	 * 
	 * @param d1 The first {@link Direction}
	 * @param d2 The second {@link Direction}
	 * 
	 * @return A boolean telling if the two given directions are opposite
	 */
	static public boolean areDirectionsOpposite(Direction d1, Direction d2)
	{
		switch(d1)
		{
		case NORTH:
			if(d2 == Direction.SOUTH)
				return true;
			else
				return false;
		case SOUTH:
			if(d2 == Direction.NORTH)
				return true;
			else
				return false;
		case EAST:
			if(d2 == Direction.WEST)
				return true;
			else
				return false;
		case WEST:
			if(d2 == Direction.EAST)
				return true;
			else
				return false;
		default:
			return false;
		}
	}

}

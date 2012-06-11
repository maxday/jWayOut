package Components;

import sim.portrayal.Oriented2D;
import Util.Constants.Direction;

public class Door extends Shape implements Oriented2D {
	
	private Direction doorDirection;

	/**
	 * Constructor
	 * 
	 * @param beginX Where the door begins, in X-coordinate
	 * @param beginY Where the door begins, in Y-coordinate
	 * @param endX Where the door ends, in X-coordinate
	 * @param endY Where the door ends, in Y-coordinate
	 * @param direction The pointed direction
	 */
	public Door(String beginX, String beginY, String endX, String endY, String direction) {
		super(beginX, beginY, endX, endY);
		if (direction.equals("NORTH")) doorDirection = Direction.NORTH;
		else if (direction.equals("SOUTH")) doorDirection = Direction.SOUTH;
		else if (direction.equals("EAST")) doorDirection = Direction.EAST;
		else if (direction.equals("WEST")) doorDirection = Direction.WEST;
		else doorDirection = Direction.UNKNOWN;
	}
	
	/**
	 * Getter for the attribute doorDirection
	 * 
	 * @return The door pointed direction
	 */
	public Direction getDoorDirection() {
		return doorDirection;
	}

	/**
	 * The standard toString method
	 */
	public String toString() {
		return "[BeginX = " + beginX + "; BeginY = " + beginY 
				+ "; EndX = " + endX + "; EndY = " + endY
				+ "; Direction = " + direction + "; DoorDirection = " + doorDirection + "]";
	}

	@Override
	public double orientation2D() {
		switch (doorDirection) {
		case NORTH:
			return Math.PI/2*3;
		case SOUTH:
			return Math.PI/2;
		case EAST:
			return 0;
		case WEST:
			return Math.PI;		
		}
		return 0;
	}
}

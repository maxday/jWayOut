package Components;

import java.util.ArrayList;
import java.util.List;

import sim.util.Int2D;
import Util.Constants.Direction;

public abstract class Shape {

	protected int beginX;
	protected int beginY;
	protected int endX;
	protected int endY;
	protected Direction direction;
	
	protected List<Int2D> listCoord = new ArrayList<Int2D>();

	
	/**
	 * Constructor
	 * 
	 * @param beginX Where the shape begins, in X-coordinate
	 * @param beginY Where the shape begins, in Y-coordinate
	 * @param endX Where the shape ends, in X-coordinate
	 * @param endY Where the shape ends, in Y-coordinate
	 */
	public Shape(String beginX, String beginY, String endX, String endY) {
		this.beginX = Integer.parseInt(beginX);
		this.beginY = Integer.parseInt(beginY);
		this.endX = Integer.parseInt(endX);
		this.endY = Integer.parseInt(endY);
		
		computeDirection();
		computeCoord();
	}
	
	/**
	 * This method updates the shape's direction according to it current attributes' values
	 */
	private void computeDirection() {
		if (beginX == endX) {
			if (endY < beginY) direction = Direction.NORTH;
			else direction = Direction.SOUTH;
		} else if (beginY == endY) {
			if (endX > beginX) direction = Direction.EAST;
			else direction = Direction.WEST;
		} else {
			direction = Direction.UNKNOWN;
		}		
	}

	
	/**
	 * This method update the shape's coordinates according to it current diection
	 */
	private void computeCoord() {
		switch (direction) {
		case NORTH:
			for (int y = endY; y <= beginY ; ++y) listCoord.add(new Int2D(beginX, y));
			break;
		case SOUTH:
			for (int y = beginY; y <= endY ; ++y) listCoord.add(new Int2D(beginX, y));
			break;
		case EAST:
			for (int x = beginX; x <= endX; ++x) listCoord.add(new Int2D(x, beginY));
			break;
		case WEST:
			for (int x = endX; x <= beginX; ++x) listCoord.add(new Int2D(x, beginY));	
			break;
		}
	}

	/**
	 * Getter for beginX attribute
	 * 
	 * @return Where the shape begins, as a x coordinate
	 */
	public int getBeginX() {
		return beginX;
	}

	/**
	 * Getter for beginY attribute
	 * 
	 * @return Where the shape begins, as a y coordinate
	 */
	public int getBeginY() {
		return beginY;
	}

	/**
	 * Getter for endX attribute
	 * 
	 * @return Where the shape ends, as a x coordinate
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * Getter for endY attribute
	 * 
	 * @return Where the shape ends, as a y coordinate
	 */
	public int getEndY() {
		return endY;
	}	

	public Direction getDirection() {
		return direction;
	}
	
	public String toString() {
		return "[BeginX = " + beginX + "; BeginY = " + beginY 
				+ "; EndX = " + endX + "; EndY = " + endY + "; Direction = " + direction + "]";
	}

	/**
	 * This method returns the list of coordinates where is represented this shape on the view
	 * 
	 * @return A list of coordinates
	 */
	public List<Int2D> getListCoord() {
		return listCoord;
	}	

}

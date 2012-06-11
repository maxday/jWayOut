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

	public Shape(String beginX, String beginY, String endX, String endY) {
		this.beginX = Integer.parseInt(beginX);
		this.beginY = Integer.parseInt(beginY);
		this.endX = Integer.parseInt(endX);
		this.endY = Integer.parseInt(endY);
		
		computeDirection();
		computeCoord();
	}
	
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

	public int getBeginX() {
		return beginX;
	}

	public int getBeginY() {
		return beginY;
	}

	public int getEndX() {
		return endX;
	}

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

	public List<Int2D> getListCoord() {
		return listCoord;
	}	

}

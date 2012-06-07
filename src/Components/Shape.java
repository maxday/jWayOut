package Components;

import java.util.ArrayList;
import java.util.List;

import Util.Constants.Direction;

import sim.util.Int2D;

public abstract class Shape {

	protected Integer beginX;
	protected Integer beginY;
	protected Integer endX;
	protected Integer endY;
	protected Direction direction;
	
	protected List<Int2D> listCoord = new ArrayList<Int2D>();

	public Shape(String beginX, String beginY, String endX, String endY, String direction) {
		this.beginX = Integer.parseInt(beginX);
		this.beginY = Integer.parseInt(beginY);
		this.endX = Integer.parseInt(endX);
		this.endY = Integer.parseInt(endY);
		if (direction.equals("vertical")){
			if (this.endY > this.beginY) this.direction = Direction.SOUTH;
			else this.direction = Direction.NORTH;
		} else {
			if (this.endX > this.beginX) this.direction = Direction.EAST;
			else this.direction = Direction.WEST;
		}
		computeCoord();
	}

	private void computeCoord() {
		switch (direction) {
		case NORTH:
			for (Integer y = endY; y <= beginY ; ++y) listCoord.add(new Int2D(beginX, y));
		case SOUTH:
			for (Integer y = beginY; y <= endY ; ++y) listCoord.add(new Int2D(beginX, y));
		case EAST:
			for (Integer x = beginX; x <= endX; ++x) listCoord.add(new Int2D(x, beginY));
		case WEST:
			for (Integer x = endX; x <= beginX; ++x) listCoord.add(new Int2D(x, beginY));	
		}
	}

	public Integer getBeginX() {
		return beginX;
	}

	public Integer getBeginY() {
		return beginY;
	}

	public Integer getEndX() {
		return endX;
	}

	public Integer getEndY() {
		return endY;
	}	

	public Direction getDirection() {
		return direction;
	}
	
	public String toString() {
		return "[BeginX = " + beginX + "; BeginY = " + beginY + "; EndX = " + endX + "; EndY = " + endY + "; Direction = " + direction + "]";
	}

	public List<Int2D> getListCoord() {
		return listCoord;
	}	

}

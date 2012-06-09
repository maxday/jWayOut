package Components;

import sim.portrayal.Oriented2D;


public class Arrow extends Shape implements Oriented2D {

	public Arrow(String beginX, String beginY, String endX, String endY) {
		super(beginX, beginY, endX, endY);	
	}
	
	@Override
	public double orientation2D() {
		switch (direction) {
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

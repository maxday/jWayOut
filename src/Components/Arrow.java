package Components;

import sim.portrayal.Orientable2D;


public class Arrow extends Shape implements Orientable2D{

	private String beginX;
	private String beginY;
	private String endX;
	private String endY;
	private String direction;
	private String orientation;
	
	public Arrow(String beginX, String beginY, String endX, String endY, String direction){
		super(beginX, beginY, endX, endY, direction);
		this.beginX = super.getBeginX();
		this.beginY = super.getBeginY();
		this.endX = super.getEndX();
		this.endY = super.getEndY();
		this.direction = super.getDirection();
		this.orientation = "UNKNOW";
	}
	
	@Override
	public double orientation2D() {
		if(direction.equals("vertical")){
			if(Integer.parseInt(endY) > Integer.parseInt(beginY)){
				orientation = "SOUTH";
				return Math.PI/2;
			}
			else{
				orientation = "NORTH";
				return Math.PI/2*3;
			}
		}
		else 
		{
			if(Integer.parseInt(endX) > Integer.parseInt(beginX)){
				orientation = "EAST";
				return 0;
			}
			else{
				orientation = "WEST";
				return Math.PI;
			}
		}	
	}
	
	public String getOrientation()
	{
		return orientation;
	}

	@Override
	public void setOrientation2D(double arg0) {
		
	}

}

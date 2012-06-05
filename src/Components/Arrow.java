package Components;

import sim.portrayal.Orientable2D;


public class Arrow extends Shape implements Orientable2D{

	private String beginX;
	private String beginY;
	private String endX;
	private String endY;
	private String direction;
	
	public Arrow(String beginX, String beginY, String endX, String endY, String direction){
		super(beginX, beginY, endX, endY, direction);
		this.beginX = super.getBeginX();
		this.beginY = super.getBeginY();
		this.endX = super.getEndX();
		this.endY = super.getEndY();
		this.direction = super.getDirection();
	}
	
	@Override
	public double orientation2D() {
		if(direction.equals("vertical")){
			if(Integer.parseInt(endY) > Integer.parseInt(beginY)){
				return Math.PI/2;
			}
			else{
				return Math.PI/2*3;
			}
		}
		else 
		{
			if(Integer.parseInt(endX) > Integer.parseInt(beginX)){
				return 0;
			}
			else{
				return Math.PI;
			}
		}	
	}

	@Override
	public void setOrientation2D(double arg0) {
		
	}

}

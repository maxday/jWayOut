package Components;

import java.util.ArrayList;
import java.util.List;

import sim.util.Int2D;

public abstract class Shape {

	private String beginX;
	private String beginY;
	private String endX;
	private String endY;
	private String direction;
	
	private List<Int2D> listCoord = new ArrayList<Int2D>();

	public Shape(String beginX, String beginY, String endX, String endY, String direction) {
		this.beginX = beginX;
		this.beginY = beginY;
		this.endX = endX;
		this.endY = endY;
		this.direction = direction;
		computeCoord();
	}

	private void computeCoord() {

		if ("vertical".equals(direction)) {
			Integer maxY = null;
			Integer x = Integer.parseInt(beginX);
			if(Integer.parseInt(endY) > Integer.parseInt(beginY)){
				 maxY= Integer.parseInt(endY);
				 for (Integer y = Integer.parseInt(beginY); y <= maxY ; ++y)
					listCoord.add(new Int2D(x, y));
			}
			else{
				maxY = Integer.parseInt(beginY);
				for (Integer y = Integer.parseInt(endY); y <= maxY ; ++y)
					listCoord.add(new Int2D(x, y));
			}	
			
		} else 
		{
			Integer maxX = null;
			Integer y = Integer.parseInt(beginY);
			if(Integer.parseInt(endX) > Integer.parseInt(beginX)){
				maxX = Integer.parseInt(endX);
				for (Integer x = Integer.parseInt(beginX); x <= maxX; ++x)
					listCoord.add(new Int2D(x, y));
			}
			else{
				maxX = Integer.parseInt(beginX);
				for (Integer x = Integer.parseInt(endX); x <= maxX; ++x)
					listCoord.add(new Int2D(x, y));
			}
		}
	}

	public String getBeginX() {
		return beginX;
	}

	public String getBeginY() {
		return beginY;
	}

	public String getEndX() {
		return endX;
	}

	public String getEndY() {
		return endY;
	}	

	public String getDirection() {
		return direction;
	}
	
	public String toString() {
		return "[BeginX = " + beginX + "; BeginY = " + beginY + "; EndX = " + endX + "; EndY = " + endY + "; Direction = " + direction + "]";
	}

	public List<Int2D> getListCoord() {
		return listCoord;
	}
	
	
	

}

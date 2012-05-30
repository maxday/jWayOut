package Components;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Wall {

	private String beginX;
	private String beginY;
	private String endX;
	private String endY;
	private String direction;
	
	private List<Point> listCoord = new ArrayList<Point>();

	public Wall(String beginX, String beginY, String endX, String endY, String direction) {
		this.beginX = beginX;
		this.beginY = beginY;
		this.endX = endX;
		this.endY = endY;
		this.direction = direction;
		computeCoord();
	}

	private void computeCoord() {
		if ("vertical".equals(direction)) {
			Integer maxY = Integer.parseInt(endY);
			Integer x = Integer.parseInt(beginX);
			for (Integer y = Integer.parseInt(beginY); y <= maxY ; ++y)
				listCoord.add(new Point(x, y));
		} else {
			Integer maxX = Integer.parseInt(endX);
			Integer y = Integer.parseInt(beginY);
			for (Integer x = Integer.parseInt(beginX); x <= maxX; ++x)
				listCoord.add(new Point(x, y));
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

	public List<Point> getListCoord() {
		return listCoord;
	}
}

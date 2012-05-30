package Components;

import java.util.HashMap;
import java.util.Map;

public class Wall {

	private String beginX;
	private String beginY;
	private String endX;
	private String endY;
	private String direction;
	
	private Map<Long, Long> listCoord = new HashMap<Long, Long>();

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
			Long maxY = Long.parseLong(endY);
			Long x = Long.parseLong(beginX);
			for (Long y = Long.parseLong(beginY); y <= maxY ; ++y)
				listCoord.put(x, y);
		} else {
			Long maxX = Long.parseLong(endX);
			Long y = Long.parseLong(beginY);
			for (Long x = Long.parseLong(beginX); x <= maxX; ++x)
				listCoord.put(x, y);
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

	public Map<Long, Long> getListCoord() {
		return listCoord;
	}
	
	
	

}

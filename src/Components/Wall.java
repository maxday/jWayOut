package Components;

import java.util.HashMap;
import java.util.Map;

public class Wall {

	private String beginX;
	private String beginY;
	private String endY;
	private String endX;
	private String position;
	
	private Map<Long, Long> listCoord = new HashMap<Long, Long>();

	public Wall(String beginX, String beginY, String endX, String endY, String position) {
		this.beginX = beginX;
		this.beginY = beginY;
		this.endX = endX;
		this.endY = endY;
		this.position = position;
		computeCoord();
	}

	private void computeCoord() {
		if("vertical".equals(position)) {
			for(Long i= Long.parseLong(endY); i<Long.parseLong(endY); ++i)
				listCoord.put(Long.parseLong(endX), i);
		}
		else {
			for(Long i= Long.parseLong(endX); i<Long.parseLong(endX); ++i)
				listCoord.put(i, Long.parseLong(endX));
		}
	}

	public String getBeginX() {
		return beginX;
	}

	public String getBeginY() {
		return beginY;
	}

	public String getEndY() {
		return endY;
	}

	public String getEndX() {
		return endX;
	}

	public String getPosition() {
		return position;
	}
	
	public String toString() {
		return "[BeginX = " + beginX + "; BeginY = " + beginY + "; EndX = " + endX + "; EndY = " + endY + "; Position = " + position + "]";
	}

	public Map<Long, Long> getListCoord() {
		return listCoord;
	}
	
	
	

}

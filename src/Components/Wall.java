package Components;

public class Wall {

	private String beginX;
	private String beginY;
	private String endY;
	private String endX;
	private String position;

	public Wall(String beginX, String beginY, String endX, String endY, String position) {
		this.beginX = beginX;
		this.beginY = beginY;
		this.endX = endX;
		this.endY = endY;
		this.position = position;
		
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

}

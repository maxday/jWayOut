package Util;

@SuppressWarnings("serial")
public class Point extends java.awt.Point{

	public Point(Integer x, Integer y) {
		super(x,y);
	}

	@Override
	public String toString() {
		return "[x=" + this.x + "; y=" + this.y + "]";
	}

}

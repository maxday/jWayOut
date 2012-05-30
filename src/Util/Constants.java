package Util;

public class Constants {

	final static String XML_WALL = "wall";
	final static String XML_EXIT = "exit";
	final static String XML_WALLS = "walls";
	final static String XML_EXITS = "exits";
	
	final static String XML_DIRECTION = "direction";
	final static String XML_BEGIN = "begin";
	final static String XML_END = "end";
	final static String XML_COORD_X = "x";
	final static String XML_COORD_Y = "y";
	
	private final static int GRID_TO_FRAME = 10;
	
	public final static int GRID_HEIGHT = 68;
	public final static int GRID_WIDTH = 123;
	public final static int FRAME_WIDTH = GRID_WIDTH * GRID_TO_FRAME;
	public final static int FRAME_HEIGHT = GRID_HEIGHT * GRID_TO_FRAME;
	
	
	static public final int MAX_ABILITY = 10;
	static public final int STRONG_PANIC = 3;
	
	static public final int AGENT_SLOW_SPEED = 1;
	static public final int AGENT_NORMAL_SPEED = 2;
	static public final int AGENT_HIGH_SPEED = 3;
	static public final int AGENT_VERY_HIGH_SPEED = 4;
	
	public static final String XML_AGENTS = "agents";
	public static final String XML_PEOPLE = "people";
	public static final String XML_EAR = "ear";
	public static final String XML_EYE = "eye";
	
}


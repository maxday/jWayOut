package Util;

public class Constants {

	// XML
	
	final static String XML_WALLS = "walls";
	final static String XML_WALL = "wall";
	final static String XML_DOORS = "doors";
	final static String XML_DOOR = "door";
	final static String XML_EXITS = "exits";
	final static String XML_EXIT = "exit";
	final static String XML_ARROWS = "arrows";
	final static String XML_ARROW = "arrow";
	
	final static String XML_DIRECTION = "direction";
	final static String XML_BEGIN = "begin";
	final static String XML_END = "end";
	final static String XML_COORD_X = "x";
	final static String XML_COORD_Y = "y";
	
	public final static String XML_AGENTS = "agents";
	public final static String XML_PEOPLE = "people";
	public final static String XML_EYE = "eye";
	public final static String XML_EAR = "ear";
	
	
	// VIEW
	
	public final static int GRID_WIDTH = 123;
	public final static int GRID_HEIGHT = 68;
	public final static int GRID_TO_FRAME = 10;
	public final static int FRAME_WIDTH = GRID_WIDTH * GRID_TO_FRAME;
	public final static int FRAME_HEIGHT = GRID_HEIGHT * GRID_TO_FRAME;
	
	public static enum Direction { UNKNOWN, NORTH, SOUTH, EAST, WEST };
	
	
	// AGENTS
	
	public final static int MAX_ABILITY = 15;
	public final static int MIN_ABILITY = 5;
	public final static int STRONG_PANIC = 3;
	public final static int MAX_PANIC = 30;
	
	public final static int AGENT_SLOW_SPEED = 1;
	public final static int AGENT_NORMAL_SPEED = 2;
	public final static int AGENT_HIGH_SPEED = 3;
	public final static int AGENT_VERY_HIGH_SPEED = 4;
	
	public final static int AGENT_HEIGHT = 2;
	public final static int AGENT_WIDTH = 2;
	public static final int NUM_STEP_FIRE_SPREAD = 5;
	
	public static final int DISTANCE_TO_ARROW = 4;
}


package Util;

public class Constants {

	public static enum Direction { UNKNOWN, NORTH, SOUTH, EAST, WEST };
	
	// XML - TAGS
	final static String XML_WALLS = "walls";
	final static String XML_WALL = "wall";
	final static String XML_DOORS = "doors";
	final static String XML_DOOR = "door";
	final static String XML_EXITS = "exits";
	final static String XML_EXIT = "exit";
	final static String XML_AGENTS = "agents";
	final static String XML_PEOPLE = "people";
	final static String XML_BEGIN = "begin";
	final static String XML_END = "end";
	
	// XML - ATTRIBUTES
	final static String XML_DIRECTION = "direction";
	final static String XML_COORD_X = "x";
	final static String XML_COORD_Y = "y";
	final static String XML_EYE = "eye";
	final static String XML_EAR = "ear";
	final static String XML_NAME = "name";	
	
	// VIEW
	public final static int GRID_WIDTH = 101;
	public final static int GRID_HEIGHT = 68;
	public final static int GRID_TO_FRAME = 8;
	public final static int FRAME_WIDTH = GRID_WIDTH * GRID_TO_FRAME;
	public final static int FRAME_HEIGHT = GRID_HEIGHT * GRID_TO_FRAME;
	
	// PEOPLE - ABILITY
	public final static int MAX_ABILITY = 18;
	public final static int MIN_ABILITY = 12;
	public final static int STRONG_PANIC = 3;
	public final static int MAX_PANIC = 50;
	
	// PEOPLE - SPEED
	public final static int AGENT_SLOW_SPEED = 1;
	public final static int AGENT_NORMAL_SPEED = 2;
	public final static int AGENT_HIGH_SPEED = 3;
	public final static int AGENT_VERY_HIGH_SPEED = 4;
	
	
	// FIRE - SPREADING
	public final static int NUM_STEP_FIRE_SPREAD = 2;
}


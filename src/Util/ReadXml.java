package Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import Agents.People;
import Components.Arrow;
import Components.Door;
import Components.Exit;
import Components.Wall;

public class ReadXml {
   
   static private List<Wall> wallList;
   static private List<Door> doorList;
   static private List<Exit> exitList;
   static private List<Arrow> arrowList;
   static private ArrayList<People> peopleList;
   
   static private String beginX;
   static private String endX;
   static private String beginY;
   static private String endY;
   
   public static void readXmlFile(String filename) {
      SAXBuilder sxb = new SAXBuilder();
      Document document = null;
      Element root;
      
	  try {
		  document = sxb.build(new File(filename));
		  root = document.getRootElement();
	      loadWallList(root);
	      loadDoorList(root);
	      loadExitList(root);
	      //loadArrowList(root);
	      loadPeopleList(root);
	  } catch (Exception e) {
		  LogConsole.print("Unable to open the XML file" , Actions.Action.FILE.name(), filename);
	  }
   }

	private static void loadWallList(Element root) {
		wallList = new ArrayList<Wall>();
		List<Element> wallsList = root.getChild(Constants.XML_WALLS).getChildren(Constants.XML_WALL);
		Iterator<Element> i = wallsList.iterator();
		while (i.hasNext()) {
            getAttributes(i);
			Wall theWall = new Wall(beginX, beginY, endX, endY);
			LogConsole.print(theWall.toString(), Actions.Action.READ.name(), theWall.getClass().getName());
			wallList.add(theWall);
		}
	}
	
	private static void loadDoorList(Element root) {
		doorList = new ArrayList<Door>();
		List<Element> doorsList = root.getChild(Constants.XML_DOORS).getChildren(Constants.XML_DOOR);
		Iterator<Element> i = doorsList.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();
			String direction = courant.getAttributeValue(Constants.XML_DIRECTION);
			beginX = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_X);
			beginY = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_Y);
			endX = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_X);
			endY = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_Y);
			Door theDoor = new Door(beginX, beginY, endX, endY, direction);
			LogConsole.print(theDoor.toString(), Actions.Action.READ.name(), theDoor.getClass().getName());
			doorList.add(theDoor);
		}
	}
	
    private static void loadExitList(Element root) {
    	exitList = new ArrayList<Exit>();
		List<Element> exitsList = root.getChild(Constants.XML_EXITS).getChildren(Constants.XML_EXIT);
		Iterator<Element> i = exitsList.iterator();
		while (i.hasNext()) {
            getAttributes(i);
			Exit theExit = new Exit(beginX, beginY, endX, endY);
			LogConsole.print(theExit.toString(), Actions.Action.READ.name(), theExit.getClass().getName());
			exitList.add(theExit);
		}
    	
    }
    
    /*private static void loadArrowList(Element root) {
    	arrowList = new ArrayList<Arrow>();
    	List<Element> arrowsList = root.getChild(Constants.XML_ARROWS).getChildren(Constants.XML_ARROW);
		Iterator<Element> i = arrowsList.iterator();
		while (i.hasNext()) {
            getAttributes(i);
			Arrow theArrow = new Arrow(beginX, beginY, endX, endY);
			LogConsole.print(theArrow.toString(), Actions.Action.READ.name(), theArrow.getClass().getName());
			arrowList.add(theArrow);
		}
    }*/
    
    private static void getAttributes(Iterator<Element> element) {
		Element courant = (Element) element.next();
		beginX = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_X);
		beginY = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_Y);
		endX = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_X);
		endY = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_Y);
    }
    
    private static void loadPeopleList(Element root) {
		peopleList = new ArrayList<People>();
		List<Element> people = root.getChild(Constants.XML_AGENTS).getChildren(Constants.XML_PEOPLE);
		Iterator<Element> i = people.iterator();
		while (i.hasNext()) {
			Element courant = (Element) i.next();
			String eyeX = courant.getChild(Constants.XML_EAR).getAttributeValue(Constants.XML_COORD_X);
			String eyeY = courant.getChild(Constants.XML_EAR).getAttributeValue(Constants.XML_COORD_Y);
			String earX = courant.getChild(Constants.XML_EYE).getAttributeValue(Constants.XML_COORD_X);
			String earY = courant.getChild(Constants.XML_EYE).getAttributeValue(Constants.XML_COORD_Y);
			People thePerson = new People(eyeX, eyeY, earX, earY);
			LogConsole.print(thePerson.toString(), Actions.Action.READ.name(), thePerson.getClass().getName());
			peopleList.add(thePerson);
		}
	}

	public static List<Wall> getWallList() {
		return wallList;
	}
	
	public static List<Door> getDoorList() {
		return doorList;
	}

	public static List<Exit> getExitList() {
		return exitList;
	}
	
	public static List<Arrow> getArrowList() {
		return arrowList;
	}
 
	public static List<People> getPeopleList() {
		return peopleList;
	}	
    
}
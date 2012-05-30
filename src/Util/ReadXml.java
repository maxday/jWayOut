package Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import Agents.People;
import Components.Exit;
import Components.Wall;

public class ReadXml {
   
   static private List<Wall> wallList;
   static private List<Exit> exitList;   
   static private ArrayList<People> peopleList;
   
   static private String beginX;
   static private String endX;
   static private String beginY;
   static private String endY;
   static private String direction;

   
   public static void readXmlFile(String filename) {
	   
      SAXBuilder sxb = new SAXBuilder();
      Document document = null;
      Element root;
      
	  try {
		  document = sxb.build(new File(filename));
		  root = document.getRootElement();
	      loadWallList(root);
	      loadExitList(root);
	      loadPeopleList(root);
	  }
	  catch(Exception e){
		  LogConsole.print("Unable to open the XML file" , Actions.Action.FILE.name(), filename);
	  }
      

     
   }

	private static void loadPeopleList(Element root) {
		peopleList = new ArrayList<People>();
		List<Element> people = root.getChild(Constants.XML_AGENTS).getChildren(Constants.XML_PEOPLE);
		Iterator<Element> i = people.iterator();
		while(i.hasNext()) {
			Element courant = (Element) i.next();
			String eyeX = courant.getChild(Constants.XML_EAR).getAttributeValue(Constants.XML_COORD_X);
			String eyeY = courant.getChild(Constants.XML_EAR).getAttributeValue(Constants.XML_COORD_Y);
			String earX = courant.getChild(Constants.XML_EYE).getAttributeValue(Constants.XML_COORD_X);
			String earY = courant.getChild(Constants.XML_EYE).getAttributeValue(Constants.XML_COORD_Y);
			People thePerson = new People(Integer.parseInt(eyeX), Integer.parseInt(eyeY), Integer.parseInt(earX), Integer.parseInt(earY));
			LogConsole.print(thePerson.toString(), Actions.Action.READ.name(), thePerson.getClass().getName());
			peopleList.add(thePerson);
		}
	}

	private static void loadWallList(Element root) {
		wallList = new ArrayList<Wall>();
		List<Element> wallsList = root.getChild(Constants.XML_WALLS).getChildren(Constants.XML_WALL);
		Iterator<Element> i = wallsList.iterator();
		while(i.hasNext()) {
            getAttributes(i);
			Wall theWall = new Wall(beginX, beginY, endX, endY, direction);
			LogConsole.print(theWall.toString(), Actions.Action.READ.name(), theWall.getClass().getName());
			wallList.add(theWall);
		}
	}
	
    private static void loadExitList(Element root) {
    	exitList = new ArrayList<Exit>();
		List<Element> wallsList = root.getChild(Constants.XML_EXITS).getChildren(Constants.XML_EXIT);
		Iterator<Element> i = wallsList.iterator();
		while(i.hasNext()) {
            getAttributes(i);
			Exit theExit = new Exit(beginX, beginY, endX, endY, direction);
			LogConsole.print(theExit.toString(), Actions.Action.READ.name(), theExit.getClass().getName());
			exitList.add(theExit);
		}
    	
    }
    
    private static void getAttributes(Iterator<Element> element) {
		Element courant = (Element) element.next();
		beginX = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_X);
		beginY = courant.getChild(Constants.XML_BEGIN).getAttributeValue(Constants.XML_COORD_Y);
		endX = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_X);
		endY = courant.getChild(Constants.XML_END).getAttributeValue(Constants.XML_COORD_Y);
		direction = courant.getAttributeValue(Constants.XML_DIRECTION);
    }

	public static List<Wall> getWallList() {
		return wallList;
	}

	public static List<Exit> getExitList() {
		return exitList;
	}
 
	public static List<People> getPeopleList() {
		return peopleList;
	}
    
}
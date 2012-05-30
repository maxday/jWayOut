package Util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.input.SAXBuilder;

import Components.Wall;

public class ReadXml {
   
   public static List<Wall> readXmlFile(String filename) {
	   
      SAXBuilder sxb = new SAXBuilder();
      Document document = null;
      Element root;
      List<Wall> wallList = new ArrayList<Wall>();
      
      try {
    	  document = sxb.build(new File(filename));
      }
      catch(Exception e){
    	  LogConsole.print("Unable to open the XML file" , Actions.Action.FILE.name(), filename);
      }
      
      root = document.getRootElement();
	  List<Element> wallsList = root.getChildren(Constants.XML_WALL);
	  Iterator<Element> i = wallsList.iterator();
	  
      while(i.hasNext()) {
    	  
    	 Element courant = (Element)i.next();
    	 
    	 String beginX = courant.getChild(Constants.XML_BEGIN_WALL).getAttributeValue(Constants.XML_COORD_X);
    	 String beginY = courant.getChild(Constants.XML_BEGIN_WALL).getAttributeValue(Constants.XML_COORD_Y);
    	 String endX = courant.getChild(Constants.XML_END_WALL).getAttributeValue(Constants.XML_COORD_X);
    	 String endY = courant.getChild(Constants.XML_END_WALL).getAttributeValue(Constants.XML_COORD_Y);
    	 String position = courant.getAttributeValue(Constants.XML_DIRECTION);
    	 
    	 Wall theWall = new Wall(beginX, beginY, endX, endY, position);
    	 LogConsole.print(theWall.toString(), Actions.Action.READ.name(), theWall.getClass().getName());
    	 wallList.add(theWall);
    	
      }
      
      return wallList;
   }
}
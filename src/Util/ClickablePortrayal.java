package Util;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import sim.display.GUIState;
import sim.display.Manipulating2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.LocationWrapper;
import sim.portrayal.Portrayal;
import sim.portrayal.SimplePortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;

public class ClickablePortrayal extends RectanglePortrayal2D {
	
	private boolean fireFired = false;

	public ClickablePortrayal(Color black) {
		super(black);
		
	}

	@Override
	public boolean handleMouseEvent(GUIState guistate,
			Manipulating2D manipulating, LocationWrapper wrapper,
			MouseEvent event, DrawInfo2D fieldPortrayalDrawInfo, int type) {
				
		synchronized(guistate.state.schedule) {
			if (!fireFired && type == TYPE_HIT_OBJECT && event.getID() == event.MOUSE_CLICKED) {
				System.out.println("Je mets le feu sur X=" + event.getX() + " Y=" + event.getY());
				fireFired = true;
			}
		}
		return super.handleMouseEvent(guistate, manipulating, wrapper, event,
				fieldPortrayalDrawInfo, type);
	}

	


	
	
	
	
	
}
	        




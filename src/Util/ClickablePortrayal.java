package Util;

import java.awt.Color;
import java.awt.event.MouseEvent;

import sim.display.GUIState;
import sim.display.Manipulating2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.LocationWrapper;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.Int2D;
import Model.Model;

@SuppressWarnings("serial")
public class ClickablePortrayal extends RectanglePortrayal2D {
	
	public ClickablePortrayal(Color color) {
		super(color);
	}

	@Override
	public boolean handleMouseEvent(GUIState guistate, Manipulating2D manipulating, LocationWrapper wrapper,
			MouseEvent event, DrawInfo2D fieldPortrayalDrawInfo, int type) {
		synchronized(guistate.state.schedule) {
			if (type == TYPE_HIT_OBJECT && event.getID() == MouseEvent.MOUSE_CLICKED) {
				if (guistate.state instanceof Model) {
					Model model = (Model) guistate.state;
					model.removeSpace();
					Int2D fire = new Int2D(event.getX()/Constants.GRID_TO_FRAME, event.getY()/Constants.GRID_TO_FRAME);
					model.addFire(fire);
				}
			}
		}
		return super.handleMouseEvent(guistate, manipulating, wrapper, event, fieldPortrayalDrawInfo, type);
	}
	
}
	        




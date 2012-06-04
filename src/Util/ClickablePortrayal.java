package Util;

import java.awt.Color;
import java.awt.event.MouseEvent;
import Components.Fire;
import Components.Space;
import sim.display.GUIState;
import sim.display.Manipulating2D;
import sim.field.grid.ObjectGrid2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.LocationWrapper;
import sim.portrayal.simple.RectanglePortrayal2D;
import sim.util.IntBag;

@SuppressWarnings("serial")
public class ClickablePortrayal extends RectanglePortrayal2D {
	
	private ObjectGrid2D grid;

	public ClickablePortrayal(Color black, ObjectGrid2D grid) {
		super(black);
		this.grid = grid;
	}

	@Override
	public boolean handleMouseEvent(GUIState guistate,
			Manipulating2D manipulating, LocationWrapper wrapper,
			MouseEvent event, DrawInfo2D fieldPortrayalDrawInfo, int type) {
				
		synchronized(guistate.state.schedule) {
			if (type == TYPE_HIT_OBJECT && event.getID() == MouseEvent.MOUSE_CLICKED) {
				System.out.println("Je mets le feu sur X=" + event.getX()/Constants.GRID_TO_FRAME + " Y=" + event.getY()/Constants.GRID_TO_FRAME);
				grid.set(event.getX()/Constants.GRID_TO_FRAME, event.getY()/Constants.GRID_TO_FRAME, new Fire());
				
				
				putFire(event.getX()/Constants.GRID_TO_FRAME, event.getY()/Constants.GRID_TO_FRAME);
				removeSpace();
			}
		}
		return super.handleMouseEvent(guistate, manipulating, wrapper, event,
				fieldPortrayalDrawInfo, type);
	}

	private void putFire(int x, int y) {
		Fire fire = new Fire();
		grid.set(x, y, fire);
		System.out.println("je put");
		IntBag xBag = new IntBag();
		IntBag yBag = new IntBag();
		
		grid.getNeighborsHamiltonianDistance(x,y,2,false, xBag, yBag);
		
		for(int i=0; i<xBag.size(); ++i) {
			if(grid.get(xBag.get(i), yBag.get(i)) instanceof Space) {
				grid.set(xBag.get(i), yBag.get(i), fire);
			}
		}
	}
	
	private void removeSpace() {
		for(int x=0; x<grid.getWidth(); ++x) {
			for(int y=0; y<grid.getHeight(); ++y) {
					if(grid.get(x, y) instanceof Space)
						grid.set(x, y, null);
			}
		}
	}

	
}
	        




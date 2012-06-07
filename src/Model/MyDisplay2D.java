package Model;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.geom.Rectangle2D;

import sim.display.Display2D;
import sim.display.GUIState;

@SuppressWarnings("serial")
public class MyDisplay2D extends Display2D {

	public final GUIState simulation;
	
	public MyDisplay2D(double width, double height, GUIState simulation) {
		
		super(width, height, simulation);
		this.simulation = simulation;

	
		insideDisplay.addMouseMotionListener(new MouseMotionAdapter()
		{
			public void mouseDragged(MouseEvent e)
			{
				if (handleMouseEvent(e)) { repaint(); return; }
			}

			public void mouseMoved(MouseEvent e)
			{
	             if (handleMouseEvent(e)) { repaint(); return; }
	             else
	             {
	            	 //official basic inspector
		             final Point point = e.getPoint();
		        	 createInspectors( new Rectangle2D.Double( point.x, point.y, 1, 1 ), MyDisplay2D.this.simulation );
		          
		        	 //customer inspectors: TODO
		        	 
	             
	             }
			}
		});
		
	}

}

package Model;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;

import sim.display.Display2D;
import sim.display.GUIState;

@SuppressWarnings("serial")
public class MyDisplay2D extends Display2D {

	public final GUIState simulation;
	
	public MyDisplay2D(double width, double height, GUIState simulation) {
		
		super(width, height, simulation);
		this.simulation = simulation;

		insideDisplay.addMouseListener(new MouseListener() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (handleMouseEvent(e)) { repaint(); return; }
				else	{
					final Point point = e.getPoint();
					createInspectors(new Rectangle2D.Double( point.x, point.y, 1, 1 ), MyDisplay2D.this.simulation );
				}
				
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub	
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			
		});
	}
}

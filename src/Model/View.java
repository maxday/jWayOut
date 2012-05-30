package Model;
import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Portrayal;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import Components.Wall;
import Util.Constants;


public class View extends GUIState {
	
	public Display2D display;
	public JFrame displayFrame;
	
	private ObjectGridPortrayal2D gridPortrayal = new ObjectGridPortrayal2D();

	public View(SimState state) {
		super(state);
	}
	
	@Override
	public void init(Controller controller) {
		super.init(controller);
		display = new Display2D(Constants.FRAME_WIDTH, Constants.FRAME_HEIGHT, this);
		display.setClipping(false);
		displayFrame = display.createFrame();
		displayFrame.setTitle("JWayOut");
		controller.registerFrame(displayFrame);
		displayFrame.setVisible(true);
		display.attach(gridPortrayal, "Grid");
	}
	
	@Override
	public void start() {
		super.start();
		setupPortrayals();
	}

	private void setupPortrayals() {
		Model model = (Model)state;
		gridPortrayal.setField(model.grid);
		gridPortrayal.setPortrayalForClass(Wall.class, getWallPortrayal());
		gridPortrayal.setPortrayalForClass(Exit.class, getExitPortrayal());
		display.reset();
		display.setBackdrop(Color.WHITE);
		display.repaint();		
	}

	private Portrayal getWallPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.paint = Color.BLACK;
		r.filled = true;
		return r;
	}
	
	private Portrayal getExitPortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D();
		r.paint = Color.GREEN;
		r.filled = true;
		return r;
	}

}

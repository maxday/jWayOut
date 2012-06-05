package Model;
import java.awt.Color;

import javax.swing.JFrame;

import sim.display.Controller;
import sim.display.Display2D;
import sim.display.GUIState;
import sim.engine.SimState;
import sim.portrayal.Portrayal;
import sim.portrayal.grid.ObjectGridPortrayal2D;
import sim.portrayal.simple.OrientedPortrayal2D;
import sim.portrayal.simple.RectanglePortrayal2D;
import Agents.People;
import Components.Exit;
import Components.Fire;
import Components.Space;
import Components.Wall;
import Util.ClickablePortrayal;
import Util.Constants;


public class View extends GUIState {
	
	public Display2D display;
	public JFrame displayFrame;
	
	public ObjectGridPortrayal2D gridPortrayal = new ObjectGridPortrayal2D();

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
		
		gridPortrayal.setPortrayalForClass(Space.class, getSpacePortrayal());
		gridPortrayal.setPortrayalForClass(Wall.class, getWallPortrayal());
		gridPortrayal.setPortrayalForClass(Exit.class, getExitPortrayal());
		gridPortrayal.setPortrayalForClass(People.class, getPeoplePortrayal());
		gridPortrayal.setPortrayalForClass(Fire.class, getFirePortrayal());

		display.reset();
		display.setBackdrop(Color.WHITE);
		display.repaint();
	}

	private Portrayal getSpacePortrayal() {
		return new ClickablePortrayal(Color.WHITE);
	}

	private Portrayal getWallPortrayal() {
		return new RectanglePortrayal2D(Color.BLACK);
	}
	
	private Portrayal getExitPortrayal() {
		return new RectanglePortrayal2D(Color.GREEN);
	}
	
	private Portrayal getFirePortrayal() {
		return new RectanglePortrayal2D(Color.RED);
	}
	
	private Portrayal getPeoplePortrayal() {
		RectanglePortrayal2D r = new RectanglePortrayal2D(Color.CYAN);
		return new OrientedPortrayal2D(r, Color.BLACK);
	}

}

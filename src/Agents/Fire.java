package Agents;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import Model.Model;
import Util.Constants;

@SuppressWarnings("serial")
public class Fire implements Steppable {
	
	private List<Int2D> coords = new ArrayList<Int2D>();
	private int hasSpread = 0;
	private int step = 0;

	public Fire(Int2D hearth) {
		coords.add(hearth);
	}
	
	public void setListCoords(List<Int2D> coords) {
		this.coords = coords;
		hasSpread = coords.size();
	}
	
	public List<Int2D> getListCoords() {
		return coords;
	}

	@Override
	public void step(SimState state) {
		state.schedule.scheduleOnce(this);
		step++;
		if(step % Constants.NUM_STEP_FIRE_SPREAD != 0) {
			return;
		}
		if (state instanceof Model) {
			Model model = (Model) state;
			model.addFirePosition(coords.get(0));
			
			Int2D spread = computeFireSpread(coords.get(0));
			model.putFire(spread);
		}

	}
	
	private Int2D computeFireSpread(Int2D coord) {
		long xWillBeModify = Math.round(Math.random());
		if(xWillBeModify == 1) {
			return new Int2D(coord.x+1, coord.y);
		}
		return new Int2D(coord.x, coord.y+1);
	}

	public String toString() {
		return "[HearthX = " + coords.get(0).x + "; HearthY = " + coords.get(0).y + "]";
	}

}

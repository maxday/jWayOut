package Agents;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import Model.Model;
import Util.Constants;
import Util.Constants.Direction;
import Util.Utils;

@SuppressWarnings("serial")
public class Fire implements Steppable {
	
	private Int2D hearth;
	private int step = 0;
	private List<Direction> spreadDirection = new ArrayList<Direction>();

	public Fire(Int2D hearth) {
		this.hearth = hearth;
		spreadDirection.add(Direction.NORTH);
		spreadDirection.add(Direction.SOUTH);
		spreadDirection.add(Direction.EAST);
		spreadDirection.add(Direction.WEST);
	}

	@Override
	public void step(SimState state) {
		if (!spreadDirection.isEmpty()) state.schedule.scheduleOnce(this);
		step++;
		if (step % Constants.NUM_STEP_FIRE_SPREAD != 0) return;
		if (state instanceof Model) {
			Model model = (Model) state;
			int dirIndex = Utils.getRandomMasonValue(state, 0, spreadDirection.size()-1);
			Direction dir = spreadDirection.remove(dirIndex);
			
			switch (dir) {
			case NORTH:
				model.addFire(new Int2D(hearth.x, hearth.y-1));
				break;
			case SOUTH:
				model.addFire(new Int2D(hearth.x, hearth.y+1));
				break;
			case EAST:
				model.addFire(new Int2D(hearth.x+1, hearth.y));
				break;
			case WEST:
				model.addFire(new Int2D(hearth.x-1, hearth.y));
				break;
			}
		}
	}

	public String toString() {
		return "[HearthX = " + hearth.x + "; HearthY = " + hearth.y + "]";
	}

}


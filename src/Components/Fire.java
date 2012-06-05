package Components;

import java.util.ArrayList;
import java.util.List;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;
import Model.Model;

@SuppressWarnings("serial")
public class Fire implements Steppable {
	
	private List<Int2D> coords = new ArrayList<Int2D>();
	private int hasSpread = 0;

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
		if (state instanceof Model) {
			Model model = (Model) state;
			if (hasSpread < coords.size()) {
				for (int i = hasSpread + 1; i <= coords.size(); i++) {
					model.addFirePosition(coords.get(i-1));
				}
				hasSpread = coords.size();
			}
		}
	}
	
	public String toString() {
		return "[HearthX = " + coords.get(0).x + "; HearthY = " + coords.get(0).y + "]";
	}

}

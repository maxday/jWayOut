import sim.display.Console;

public class Main {

	public static void main(String[] args) {
		Model model = new Model(System.currentTimeMillis());
		View view = new View(model);
		Console console = new Console(view);
		console.setVisible(true);
	}

}

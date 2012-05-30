import Util.ReadXml;
import sim.display.Console;

public class Main {

	public static void main(String[] args) {
		ReadXml.readXmlFile("./resources/MDE-etage1.xml");
		
		Model model = new Model(System.currentTimeMillis());
		View view = new View(model);
		Console console = new Console(view);
		console.setVisible(true);
	}

}

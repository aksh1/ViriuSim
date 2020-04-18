import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Runner {
    public static void main(String[] args) {
		String configFilePath = "disease.ini";
		ConfigLoader cfgLoader = null;
		try {
			cfgLoader = new ConfigLoader(configFilePath);
		} catch (Exception ex) {
			System.out.println("Cannot load configuration file " + configFilePath);
			System.exit(1);
		}

		JFrame window = new JFrame("Disease Simulator");
		JFrame graphs = new JFrame("Stats for nerds");
		GraphPanel graphPanel = new GraphPanel();
		DiseaseSim sim = new DiseaseSim(cfgLoader, graphPanel);


		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Options");
		JMenuItem menuItem = new JMenuItem("Quit");

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		menu.add(menuItem);
		menu.add(new JMenuItem("Analytics"));
		menuBar.add(menu);


		window.setJMenuBar(menuBar);
		window.setContentPane(sim);
		window.setSize(sim.getBlockSize() * sim.getGridWidth(), sim.getBlockSize() * sim.getGridHeight() + 50);
		window.setLocation(50, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		graphs.setContentPane(graphPanel);
		graphs.setSize(sim.getSimulationLength() / 2, 160);
		graphs.setLocation(50 + sim.getBlockSize() * sim.getGridWidth() + 1, 100);
		graphs.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		graphs.setVisible(true);

		sim.runSim();
	}
}

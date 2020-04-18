import javax.swing.*;
import java.awt.*;
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
		LabelPanel labelPanel = new LabelPanel();
		GraphPanel graphPanel = new GraphPanel();
		DiseaseSim sim = new DiseaseSim(cfgLoader, graphPanel, labelPanel);
		sim.buildSim();


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

		JPanel stats = new JPanel();
		stats.setLayout(new GridLayout(2, 1));
		stats.add(graphPanel);
		stats.add(labelPanel);

		JPanel parent = new JPanel(new GridLayout(2, 1));

		parent.add(sim);
		parent.add(stats);


		window.setJMenuBar(menuBar);
		window.setContentPane(parent);
		window.setSize(sim.getBlockSize() * sim.getGridWidth(), (sim.getBlockSize() * sim.getGridHeight() + 50) + 250);
		window.setLocation(50, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		sim.runSim();
	}
}

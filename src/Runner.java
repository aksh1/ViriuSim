import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Runner {
	static LabelPanel labelPanel;
	static GraphPanel graphPanel;
	static DiseaseSim sim;
	static ConfigLoader cfgLoader;
    public static void main(String[] args) {
		String configFilePath = "disease.ini";
		cfgLoader = null;
		try {
			cfgLoader = new ConfigLoader(configFilePath);
		} catch (Exception ex) {
			System.out.println("Cannot load configuration file " + configFilePath);
			System.exit(1);
		}

		JFrame window = new JFrame("Disease Simulator");
		JFrame statWindow = new JFrame(("Stats for nerds"));
		labelPanel = new LabelPanel();
		graphPanel = new GraphPanel();
		sim = new DiseaseSim(cfgLoader, graphPanel, labelPanel);
		sim.buildSim();


		JMenuBar menuBar = new JMenuBar();

		JMenu menu = new JMenu("Options");
		JMenuItem menuItem = new JMenuItem("Quit");
		JMenuItem restart = new JMenuItem("Restart");

		menuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		restart.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				labelPanel = new LabelPanel();
				graphPanel = new GraphPanel();
				sim = new DiseaseSim(cfgLoader, graphPanel, labelPanel);
				sim.runSim();
			}
		});

		menu.add(restart);
		menu.add(menuItem);
		menu.add(new JMenuItem("Analytics"));
		menuBar.add(menu);

		JPanel panel = new JPanel(new GridLayout(2, 1));
		panel.add(graphPanel);
		panel.add(labelPanel);


		window.setJMenuBar(menuBar);
		window.setContentPane(sim);
		window.setSize(sim.getBlockSize() * sim.getGridWidth(), (sim.getBlockSize() * sim.getGridHeight() + 50) + 250);
		window.setLocation(0, 100);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);

		statWindow.setContentPane(panel);
		statWindow.setSize(600, (sim.getBlockSize() * sim.getGridHeight() + 50) + 250);
		statWindow.setLocation(sim.getBlockSize() * sim.getGridWidth(), 100);
		statWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		statWindow.setVisible(true);

		sim.runSim();
	}
}

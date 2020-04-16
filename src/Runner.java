import javax.swing.*;

public class Runner {
    public static void main(String[] args) {
    	String configFilePath = "disease.ini";
    	ConfigLoader cfgLoader = null;
    	try {
    		cfgLoader = new ConfigLoader(configFilePath);
    	} catch(Exception ex) {
    		System.out.println("Cannot load configuration file " + configFilePath);
    		System.exit(1);
    	}

		JFrame window = new JFrame("Disease Simulator");
		DiseaseSim sim = new DiseaseSim(cfgLoader);


//		JMenuBar menuBar = new JMenuBar();
//
//		JMenu simplePaint = new JMenu("Simple Paint");
//		JMenu optionsMenu = new JMenu("Edit");
//
//		JMenuItem quit = new JMenuItem("Quit");
//		JMenuItem undo = new JMenuItem("Undo");
//
//		undo.addActionListener(listener);
//		quit.addActionListener(listener);
//
//
//		simplePaint.add(quit);
//		optionsMenu.add(undo);
//
//		menuBar.add(simplePaint);
//		menuBar.add(optionsMenu);
//
//
//		window.setJMenuBar(menuBar);
		window.setContentPane(sim);
		window.setSize(600,600);
		window.setLocation(100,100);
		window.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		window.setVisible(true);
    	sim.runSim();
    }
}

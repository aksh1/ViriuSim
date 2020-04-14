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
        new DiseaseSim(cfgLoader).runSim();
    }
}


public class DiseaseSim {

    //Simulation Time
    private int simulation_length = 10;

    private Disease corona;
    private Block[][] country;

    public DiseaseSim(ConfigLoader cfgLoader) {
    	this.loadFromConfiguration(cfgLoader);
    	corona = new Disease();
        corona.loadFromConfiguration(cfgLoader);
        country = new Block[3][3];
    }

    public void loadFromConfiguration(ConfigLoader cfgLoader) {
    	cfgLoader.loadSection("simulation", DiseaseSim.class, this);
    	System.out.println("Configured values:\n  simulation_length = "+simulation_length);
    }

    public void runSim() {
        for (int x = 0; x < country.length; x++) {
            for (int y = 0; y < country[x].length; y++) {
                // population for each block in between 500 and 1000
                country[x][y] = new Block((int) (Math.random() * 500 + 500));
            }
        }
        for (int i = 0; i < simulation_length; i++) {
            cycle(country, corona);
        }
    }

    //"infects" the blocks
    private void cycle(final Block[][] blocks, final Disease disease) {
        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[x].length; y++) {
                System.out.println("Newly infected in block " + x + ", " + y + ": " + blocks[x][y].calculateInfection(disease));
            }
        }
    }
}
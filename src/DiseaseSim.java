
public class DiseaseSim {

    //Simulation Time
    private static final int SIMULATION_LENGTH = 10;

    private Disease corona;
    private Block[][] country;

    public DiseaseSim() {
        corona = new Disease(20, 0); // 20% chance of infection p/ interaction, 0% chance of death p/ day
        country = new Block[3][3];
    }


    public void runSim() {
        for (int x = 0; x < country.length; x++) {
            for (int y = 0; y < country[x].length; y++) {
                // population for each block in between 500 and 1000
                country[x][y] = new Block((int) (Math.random() * 500 + 500));
            }
        }
        for (int i = 0; i < SIMULATION_LENGTH; i++) {
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
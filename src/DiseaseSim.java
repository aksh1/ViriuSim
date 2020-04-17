import javax.swing.*;
import java.awt.*;

public class DiseaseSim extends JPanel {

    //Simulation Time
    private int simulationLength = 10;
    
    private int gridDuration = 2;
    private int gridWidth = 10;
    private int gridHeight = 10;

    private int currentLayer = 0;
    private int cycleWait = 1000;
    
    private Disease disease;
    private Block[][][] country;

    public DiseaseSim(ConfigLoader cfgLoader) {
    	this.loadFromConfiguration(cfgLoader);
    	disease = new Disease();
        disease.loadFromConfiguration(cfgLoader);
        country = new Block[gridDuration][gridWidth][gridHeight];
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public void loadFromConfiguration(ConfigLoader cfgLoader) {
    	cfgLoader.loadSection("simulation", DiseaseSim.class, this);
    	System.out.println("Configured values:\n  simulation_length = "+simulationLength);
    }

    public void runSim() {
    	for (int i = 0; i < gridDuration; i++) {
	        for (int x = 0; x < country[i].length; x++) {
	            for (int y = 0; y < country[i][x].length; y++) {
	                // population for each block in between 500 and 1000
	            	if (i == 0) {
	            		country[i][x][y] = new Block(disease, (int) (Math.random() * 500 + 500));
	            	} else {
	            		country[i][x][y] = new Block();
	            	}
	            }
	        }
    	}
	    for (int i = 0; i < simulationLength; i++) {
	    	int previousLayer = i % gridDuration;
	    	int layer = (i + 1) % gridDuration;
            try {
            	Thread.sleep(cycleWait);
				cycle(country, layer, previousLayer);
				System.out.println("Cycle #"+(i+1));
				showBlocks(country[layer]);
				System.out.println("");
			} catch (Exception e) {
				e.printStackTrace();
			}
        }
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		for (int x = 0; x < country[currentLayer].length; x++) {
			for (int y = 0; y < country[currentLayer][x].length; y++) {
				country[currentLayer][x][y].draw(g, (x * 50), (y * 50) );
			}
		}
	}

	//"infects" the blocks
    private void cycle(final Block[][][] blocks, int layer, int prevLayer) throws Exception {
        for (int x = 0; x < blocks[layer].length; x++) {
            for (int y = 0; y < blocks[layer][x].length; y++) {
            	blocks[layer][x][y].copyBlock(blocks[prevLayer][x][y]);
                calculateInnerInfection(blocks[layer][x][y], blocks[prevLayer][x][y]);
                calculateOuterInfection(blocks, layer, prevLayer, x, y);
                calculateRecovery(blocks[layer][x][y], blocks[prevLayer][x][y]);
                calculateDeath(blocks[layer][x][y], blocks[prevLayer][x][y]);
				System.out.println("Block (" + x + "," + y + ") is " + blocks[layer][x][y].percentInfected() + " infected");
                currentLayer = layer;
            }
        }
        this.repaint();
    }
    
    private void calculateInnerInfection(Block block, Block prevBlock) throws Exception {
        block.peopleGetSick((int)(prevBlock.getHealthy() * disease.getInteractionRate() * disease.getInfectionRate()));
    }
    
    private void calculateOuterInfection(Block[][][] blocks, int layer, int prevLayer, int targetX, int targetY) throws Exception {
    	int travelers = 0;
    	for (int x = Math.max(targetX - 1, 0); x < Math.min(x + 2, blocks[prevLayer].length); x++) {
    		for (int y = Math.max(targetY - 1, 0); y < Math.min(y + 2, blocks[prevLayer][x].length) ; y++) {
    			if (blocks[prevLayer][x][y] != null && (x != targetX || y != targetY)) {
    				travelers += blocks[prevLayer][x][y].getInfected() * disease.getTravelRate();
    			}
    		}
    	}
    	blocks[layer][targetX][targetY].peopleGetSick(Math.min((int)(travelers * disease.getInfectionRate()), blocks[layer][targetX][targetY].getHealthy()));
    }
    
    private void calculateRecovery(Block block, Block prevBlock) throws Exception {
    	block.peopleRecover((int)(disease.getRecoveryRate() * prevBlock.getInfected()));
    }
    
    private void calculateDeath(Block block, Block prevBlock) throws Exception {
    	block.peopleDie((int)(disease.getLethality() * prevBlock.getInfected()));
    }
    
    private void showBlocks(Block[][] blocks) {
    	for (int i=0; i < blocks.length; ++i) {
    		for (int j=0; j < blocks[i].length; ++j) {
    			System.out.print(String.format("  (%3d, %3d, %3d, %3d)", 
    					blocks[i][j].getHealthy(),
    					blocks[i][j].getInfected(),
    					blocks[i][j].getRecovered(),
    					blocks[i][j].getDead()));
    		}
    		System.out.println("");
    	}
    }
}
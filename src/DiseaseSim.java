import javax.swing.*;
import java.awt.*;

public class DiseaseSim extends JPanel {

	private String diseasePreset;
	private boolean verbose = false;
	//Simulation Time
	private int simulationLength = 10;
	private int cycleCount;

	private int gridDuration = 2;
	private int gridWidth = 10;
	private int gridHeight = 10;
	private int blockSize = 50;

	private int currentLayer = -1;
	private int cycleWait = 1000;

	private Disease disease;
	private Block[][][] country;

	private GraphPanel graphPanel;

	public DiseaseSim(ConfigLoader cfgLoader, GraphPanel graphPanel) {
		this.graphPanel = graphPanel;
		this.loadFromConfiguration(cfgLoader);
		disease = new Disease();
		disease.loadFromConfiguration(cfgLoader, diseasePreset);
		country = new Block[gridDuration][gridWidth][gridHeight];
	}

	public void loadFromConfiguration(ConfigLoader cfgLoader) {
		cfgLoader.loadSection("simulation", DiseaseSim.class, this);
		System.out.println("Configured values:\n  simulation_length = " + simulationLength);
	}

	public void runSim() {
		for (int i = 0; i < gridDuration; i++) {
			for (int x = 0; x < country[i].length; x++) {
				for (int y = 0; y < country[i][x].length; y++) {
					// population for each block in between 500 and 1000
					if (i == 0) {
						country[i][x][y] = new Block(disease, (int) (Math.random() * 10000 + 5));
					} else {
						country[i][x][y] = new Block();
	            	}
	            }
	        }
    	}
    	try {
	    	for (int i = 0; i < disease.getRandomInitialInfected(); i++) {
	    		int x = (int)(Math.random() * gridWidth);
	    		int y = (int)(Math.random() * gridHeight);
	    		country[0][x][y].peopleGetSick(1);
	    	}
		    for (int i = 0; i < simulationLength; i++) {
		    	int previousLayer = i % gridDuration;
		    	int layer = (i + 1) % gridDuration;
            	Thread.sleep(cycleWait);
				cycle(country, layer, previousLayer);
				if (isVerbose()) {
					System.out.println("Cycle #" + (i + 1));
					showBlocks(country[layer]);
					System.out.println();
				}
	        }
    	} catch (Exception e) {
			e.printStackTrace();
		}
    }

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (currentLayer >= 0) {
			for (int x = 0; x < country[currentLayer].length; x++) {
				for (int y = 0; y < country[currentLayer][x].length; y++) {
					int pixelX = x * blockSize;
					int pixelY = y * blockSize;
					g.setColor(Color.BLACK);
			        g.drawRect(pixelX - 1,pixelY - 1,blockSize,blockSize);
			        g.setColor(country[currentLayer][x][y].getColor());
			        g.fillRect(pixelX,pixelY,blockSize,blockSize);
				}
			}
		}
	}

	//"infects" the blocks
    private void cycle(final Block[][][] blocks, int layer, int prevLayer) throws Exception {
		int infected = 0;
		int deaths = 0;
		int recovered = 0;
		int allTimeInfected = 0;
		for (int x = 0; x < blocks[layer].length; x++) {
			for (int y = 0; y < blocks[layer][x].length; y++) {
				blocks[layer][x][y].copyBlock(blocks[prevLayer][x][y]);
				calculateInnerInfection(blocks[layer][x][y], blocks[prevLayer][x][y]);
				calculateOuterInfection(blocks, layer, prevLayer, x, y);
				calculateRecovery(blocks[layer][x][y]);
				calculateDeath(blocks[layer][x][y]);
				if (isVerbose()) {
					System.out.println("Block (" + x + "," + y + ") is " + blocks[layer][x][y].percentInfected() + " infected");
				}
				currentLayer = layer;
				recovered += country[layer][x][y].getRecovered();
				infected += country[layer][x][y].getInfected();
				deaths += country[layer][x][y].getDead();
				allTimeInfected = infected + deaths + recovered;
			}
		}
		cycleCount++;
		graphPanel.getCurrentlyInfectedGraph().getPoints().add(new Dot(cycleCount, infected));
		graphPanel.getInfectedGraph().getPoints().add(new Dot(cycleCount, allTimeInfected));
		graphPanel.getDeadGraph().getPoints().add(new Dot(cycleCount, deaths));
		graphPanel.getRecoveredGraph().getPoints().add(new Dot(cycleCount, recovered));

		this.repaint();
		graphPanel.repaint();
	}
    
    private void calculateInnerInfection(Block block, Block prevBlock) throws Exception {
        block.peopleGetSick(Math.min(getAffected(prevBlock.getInfected(), disease.getInteractionRate() * disease.getInfectionRate()), prevBlock.getHealthy()));
    }
    
    private void calculateOuterInfection(Block[][][] blocks, int layer, int prevLayer, int targetX, int targetY) throws Exception {
    	int travelers = 0;
    	for (int x = Math.max(targetX - 1, 0); x < Math.min(targetX + 2, blocks[prevLayer].length); x++) {
    		for (int y = Math.max(targetY - 1, 0); y < Math.min(targetY + 2, blocks[prevLayer][x].length) ; y++) {
    			if (blocks[prevLayer][x][y] != null && (x != targetX || y != targetY)) {
    				travelers += blocks[prevLayer][x][y].getInfected() * disease.getTravelRate();
    			}
    		}
    	}
    	blocks[layer][targetX][targetY].peopleGetSick(Math.min(getAffected(travelers, disease.getInfectionRate()), 
    			                                               blocks[layer][targetX][targetY].getHealthy()));
    } 
    
    private void calculateRecovery(Block block) throws Exception {
    	for (int i = disease.getIncubationDays(); i < disease.getMaxSickDays(); i++) {
    		block.peopleRecover(i, getAffected(block.getInfectedCohort(i), disease.getRecoveryRate()));
    	}
    }
    
    private void calculateDeath(Block block) throws Exception {
    	for (int i = disease.getIncubationDays(); i < disease.getMaxSickDays(); i++) {
    		block.peopleDie(i, getAffected(block.getInfectedCohort(i), disease.getLethality()));
    	}
    }
    
    private void showBlocks(Block[][] blocks) {
    	for (int i=0; i < blocks.length; ++i) {
			for (int j = 0; j < blocks[i].length; ++j) {
				System.out.print(String.format("  (%3d, %3d, %3d, %3d)",
						blocks[i][j].getHealthy(),
						blocks[i][j].getInfected(),
						blocks[i][j].getRecovered(),
						blocks[i][j].getDead()));
			}
			System.out.println();
		}
    }
    
    private static int getAffected(int population, double rate) {
    	int affected = 0;
    	if (rate != 0 && population < 2 / rate) {
    		for (int i = 0; i < population; i++) {
    			if (Math.random() < rate) {
    				affected++;
    			}
    		}
    	} else {
    		affected = (int)(population * rate);
    	}
    	return affected;
    }

	public int getGridWidth() {
		return gridWidth;
	}

	public int getGridHeight() {
		return gridHeight;
	}

	public int getBlockSize() {
		return blockSize;
	}

	public void setBlockSize(int blockSize) {
		this.blockSize = blockSize;
	}

	public boolean isVerbose() {
		return verbose;
	}

	public int getSimulationLength() {
		return simulationLength;
	}
	

	
}
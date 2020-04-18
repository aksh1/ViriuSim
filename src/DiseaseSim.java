import javax.swing.*;

import org.ini4j.Ini;

import java.awt.*;
import java.util.Iterator;
import java.util.Set;
import java.util.Map.Entry;

public class DiseaseSim extends JPanel {

	private ConfigLoader configLoader;
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
	
	private String image = null;
	private int pixelsPerBlock;

	private Disease disease;
	private Block[][][] country;

	private GraphPanel graphPanel;
	private LabelPanel labelPanel;

	public DiseaseSim(ConfigLoader cfgLoader, GraphPanel graphPanel, LabelPanel labelPanel) {
		this.configLoader = cfgLoader;
		this.graphPanel = graphPanel;
		this.labelPanel = labelPanel;
		this.loadFromConfiguration(cfgLoader);
		disease = new Disease();
		disease.loadFromConfiguration(cfgLoader, diseasePreset);
	}

	public void loadFromConfiguration(ConfigLoader cfgLoader) {
		cfgLoader.loadSection("simulation", DiseaseSim.class, this);
		System.out.println("Configured values:\n  simulation_length = " + simulationLength);
	}

	public void buildSim() {
    	try {
			CountryMap countryMap = null;
			double densityMultiplier = 0.0;
			if (image != null) {
				Ini.Section imageSection = configLoader.getIniHandle().get("image:"+image);
				countryMap = new CountryMap(imageSection.getOrDefault("filePath", "/dev/null"),
						Integer.parseInt(imageSection.getOrDefault("maxColorDistance", "0")),
						Integer.parseInt(imageSection.getOrDefault("colorSamplesPerAxis", "3")));
				
				Ini.Section densityMapSection = configLoader.getIniHandle().get("density:" + imageSection.get("densityMap"));
				for (Iterator<Entry<String, String>> it = densityMapSection.entrySet().iterator(); it.hasNext(); ) {
					Entry<String, String> entry = it.next();
					String key = entry.getKey();
					String value = entry.getValue();
					String[] splitKey = key.split(",\\s*");
					countryMap.addColorDensity(new Color(Integer.parseInt(splitKey[0]), Integer.parseInt(splitKey[1]), Integer.parseInt(splitKey[2])),
							Integer.parseInt(value));
				}
				countryMap.setBoxSize(pixelsPerBlock, pixelsPerBlock);
				gridWidth = countryMap.getWidth() / pixelsPerBlock;
				gridHeight = countryMap.getHeight() / pixelsPerBlock;
				int densitySum = 0;
				for (int x = 0; x < gridWidth; x++) {
					for (int y = 0; y < gridHeight; y++) {
						densitySum += countryMap.getPopulationDensity(x, y);
					}
				}
				densityMultiplier = Integer.parseInt(imageSection.getOrDefault("areaPopulation", "1000")) / densitySum;
			}
			country = new Block[gridDuration][gridWidth][gridHeight];
			for (int i = 0; i < gridDuration; i++) {
				for (int x = 0; x < country[i].length; x++) {
					for (int y = 0; y < country[i][x].length; y++) {
						int blockPopulation;
						if (countryMap != null) {
							blockPopulation = (int)(countryMap.getPopulationDensity(x, y) * densityMultiplier);
						} else {
							blockPopulation = (int) (Math.random() * 10000 + 5);
						}
						if (blockPopulation != 0) {
							if (i == 0) {
								country[i][x][y] = new Block(disease, blockPopulation);
							} else {
								country[i][x][y] = new Block();
			            	}
						}
		            }
		        }
	    	}
    	} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void runSim() {
		try {
			int toInfect = disease.getRandomInitialInfected();
	    	while (toInfect > 0) {
	    		int x = (int)(Math.random() * gridWidth);
	    		int y = (int)(Math.random() * gridHeight);
	    		if (country[0][x][y] != null) {
	    			country[0][x][y].peopleGetSick(1);
	    			toInfect--;
	    		}
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
					if (country[currentLayer][x][y] != null) {
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
	}

	//"infects" the blocks
    private void cycle(final Block[][][] blocks, int layer, int prevLayer) throws Exception {
		int infected = 0;
		int deaths = 0;
		int recovered = 0;
		int allTimeInfected = 0;
		for (int x = 0; x < blocks[layer].length; x++) {
			for (int y = 0; y < blocks[layer][x].length; y++) {
				if (blocks[layer][x][y] != null) {
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
		}
		cycleCount++;
		graphPanel.getCurrentlyInfectedGraph().getPoints().add(new Dot(cycleCount, infected));
		graphPanel.getInfectedGraph().getPoints().add(new Dot(cycleCount, allTimeInfected));
		graphPanel.getDeadGraph().getPoints().add(new Dot(cycleCount, deaths));
		graphPanel.getRecoveredGraph().getPoints().add(new Dot(cycleCount, recovered));
		labelPanel.updateLabels(deaths, recovered, infected, allTimeInfected);

		this.repaint();
		graphPanel.repaint();
		labelPanel.repaint();
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

	public boolean isVerbose() {
		return verbose;
	}

	public int getSimulationLength() {
		return simulationLength;
	}
	

	
}
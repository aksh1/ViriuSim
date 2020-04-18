public class Disease {
	private String name;
    private double infectionRate;
    private double interactionRate;
    private double travelRate;
    private double recoveryRate;
    private double lethality;
    private int incubationDays = 5;
    private int maxSickDays = 10;
    private int maxInitialInfected = 10;
    private int randomInitialInfected = 0;

    public void loadFromConfiguration(ConfigLoader cfgLoader, String presetName) {
    	cfgLoader.loadSection(presetName, Disease.class, this);
    	System.out.println("Configured values:\n  name = "+name+"\n  infectionRate = "+infectionRate+"\n  lethality = "+lethality);
    }

    public double getInfectionRate() {
        return infectionRate;
    }

    public double getLethality() {
        return lethality;
    }
    
    public String getName() {
		return name;
	}

	public double getInteractionRate() {
		return interactionRate;
	}

	public int getMaxInitialInfected() {
		return maxInitialInfected;
	}

	public double getRecoveryRate() {
		return recoveryRate;
	}

	public double getTravelRate() {
		return travelRate;
	}

	public int getRandomInitialInfected() {
		return randomInitialInfected;
	}

	public int getMaxSickDays() {
		return maxSickDays;
	}

	public int getIncubationDays() {
		return incubationDays;
	}
	
	
	
}

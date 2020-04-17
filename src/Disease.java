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

    //use the following getters/setters to take into account mutation
    public double getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(int infectionRate) {
        this.infectionRate = infectionRate;
    }

    public double getLethality() {
        return lethality;
    }

    public void setLethality(int lethality) {
        this.lethality = lethality;
    }
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getInteractionRate() {
		return interactionRate;
	}

	public void setInteractionRate(double interactionRate) {
		this.interactionRate = interactionRate;
	}

	public int getMaxInitialInfected() {
		return maxInitialInfected;
	}

	public void setMaxInitialInfected(int maxInitialInfected) {
		this.maxInitialInfected = maxInitialInfected;
	}

	public void setInfectionRate(double infectionRate) {
		this.infectionRate = infectionRate;
	}

	public void setLethality(double lethality) {
		this.lethality = lethality;
	}

	public double getRecoveryRate() {
		return recoveryRate;
	}

	public void setRecoveryRate(double recoveryRate) {
		this.recoveryRate = recoveryRate;
	}

	public double getTravelRate() {
		return travelRate;
	}

	public void setTravelRate(double travelRate) {
		this.travelRate = travelRate;
	}

	public int getRandomInitialInfected() {
		return randomInitialInfected;
	}

	public void setRandomInitialInfected(int randomInitialInfected) {
		this.randomInitialInfected = randomInitialInfected;
	}

	public int getMaxSickDays() {
		return maxSickDays;
	}

	public void setMaxSickDays(int maxSickDays) {
		this.maxSickDays = maxSickDays;
	}

	public int getIncubationDays() {
		return incubationDays;
	}

	public void setIncubationDays(int incubationDays) {
		this.incubationDays = incubationDays;
	}
	
	
	
}

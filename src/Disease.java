public class Disease {
	private String name;
    private double infectionRate;
    private double interactionRate;
    private double travelRate;
    private double recoveryRate;
    private double lethality;// didn't implement this yet
    private int maxInitialInfected = 10;

    public void loadFromConfiguration(ConfigLoader cfgLoader) {
    	cfgLoader.loadSection("disease", Disease.class, this);
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
	
}

public class Disease {
	String name;
    double infectionRate;
    double lethality;// didn't implement this yet

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
}

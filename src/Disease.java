
public class Disease {
    int infectionRate;
    int lethality;// didn't implement this yet

    public Disease(int rate, int lethal) {
        infectionRate = rate;
        lethality = lethal;
    }


    //use the following getters/setters to take into account mutation
    public int getInfectionRate() {
        return infectionRate;
    }

    public void setInfectionRate(int infectionRate) {
        this.infectionRate = infectionRate;
    }

    public int getLethality() {
        return lethality;
    }

    public void setLethality(int lethality) {
        this.lethality = lethality;
    }
}

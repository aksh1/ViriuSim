import java.awt.Color;

public class Block {

    private int population;
    private int infected;
    private int interactions;
    private int dead;
    private Color color;

    public Block(int population) {
        setPopulation(population);
        setInfected((int) (Math.random() * 10)); // 0 <= infected < 10
        setInteractions((int) (Math.random() * 20)); // 0 <= average interactions per day < 20
    }

    public int calculateInfection(Disease disease) {
        int infected = getInfected() * getInteractions() * disease.getInfectionRate() / 100;

        if (getInfected() + infected > getPopulation()) {
            infected = getPopulation() - getInfected();
        }
        setInfected(getInfected() + infected);

        int interactions = getInteractions() - (infected / 50);
        if (getInteractions() - (infected / 50) < 1) {
            interactions = 1;
        }
        setInteractions(interactions); //interactions go down as disease spreads

        return infected;
    }


    //Getters & Setters

    public int getDead() {
        return dead;
    }

    public void setDead(int dead) {
        this.dead = dead;
    }

    public int getPopulation() {
        return population;
    }

    public void setPopulation(int population) {
        this.population = population;
    }

    public int getInfected() {
        return infected;
    }

    public void setInfected(int infected) {
        this.infected = infected;
    }

    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor() {
        if (infected <= 0){
            color = Color.GREEN;
        }
        else if (infected/population >= 0.75){
            color = Color.RED;
        }
        else{
            color = new Color (235, 235-((infected/population)*200), 52);
        }
    }
}

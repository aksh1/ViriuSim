import java.awt.*;

public class Block extends Component{

    private int healthy;
    private int infected;
    private int dead;
    private int recovered;
    private Disease disease;

    public Block(Disease disease, int population) {
    	this.disease = disease;
        infected = (int) (Math.random() * disease.getMaxInitialInfected()); // 0 <= infected < max initial infected
        healthy = Math.max(population - infected, 0);
        dead = 0;
        recovered = 0;
    }
    
    public Block() {} //intentionally empty

    public void copyBlock(Block that) {
    	healthy = that.healthy;
    	infected = that.infected;
    	dead = that.dead;
    	recovered = that.recovered;
    	disease = that.disease;
    }
    
    //Getters
    public int getDead() {
        return dead;
    }

    public int getPopulation() {
        return getHealthy() + getInfected();
    }

    public int getInfected() {
        return infected;
    }
    
    public int getHealthy() {
		return healthy;
	}
	
    public int getRecovered() {
		return recovered;
	}

	public Color getColor() {
        if (percentInfected() <= 10){
            return Color.GREEN;
        }
        else if (percentInfected() >= 0.75){
            return Color.RED;
        }
        else{
            return new Color (235, 235-((getInfected()/getPopulation())*200), 52);
        }
    }
	
	// changes
	public void peopleGetSick(int newlySickCount) throws Exception {
		if (newlySickCount > getHealthy()) {
			throw new Exception("can't have more sick than healthy");
		}
		healthy  -= newlySickCount;
		infected += newlySickCount;
	}
	
	public void peopleDie(int newlyDeadCount) throws Exception {
		if (newlyDeadCount > getInfected()) {
			throw new Exception("can't have more dead than infected");
		}
		infected -= newlyDeadCount;
		dead     += newlyDeadCount;
	}
	
	public void peopleRecover(int newlyRecoveredCount) throws Exception {
		if (newlyRecoveredCount > getInfected()) {
			throw new Exception("can't have more recovered than infected");
		}
		infected  -= newlyRecoveredCount;
		recovered += newlyRecoveredCount;
	}

	public double percentInfected () {
        return (double) getInfected()/getPopulation() * 100;
    }

    public void draw(Graphics g, int x, int y) {
        g.setColor(Color.BLACK);
        g.drawRect(x,y,50,50);
        g.setColor(getColor());
        g.fillRect(x,y,50,50);
    }
}

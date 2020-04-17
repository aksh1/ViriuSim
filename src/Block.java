import java.awt.*;

public class Block extends Component{

    private int healthy;
    private int[] infectedCohort;
    private int dead;
    private int recovered;
    private Disease disease;
    private int initPopulation;

    public Block(Disease disease, int population) {
    	this.disease = disease;
        infectedCohort = new int[disease.getMaxSickDays()];
    	infectedCohort[0] = (int) (Math.random() * disease.getMaxInitialInfected()); // 0 <= infected < max initial infected
        healthy = Math.max(population - infectedCohort[0], 0);
        initPopulation = population;
        dead = 0;
        recovered = 0;
    }
    
    public Block() {} //intentionally empty

    public void copyBlock(Block that) {
    	disease = that.disease;
    	if (infectedCohort == null) {
    		infectedCohort = new int[disease.getMaxSickDays()];
    	}
    	infectedCohort[0] = 0;
    	for (int i = 1; i < disease.getMaxSickDays(); i++) {
    		infectedCohort[i] = that.infectedCohort[i - 1];
    	}
    	
    	healthy = that.healthy;
    	dead = that.dead + (int)(that.infectedCohort[that.infectedCohort.length - 1] / 2);
    	recovered = that.recovered + (int)(that.infectedCohort[that.infectedCohort.length - 1] / 2);
    	initPopulation = that.initPopulation;
    }
    
    //Getters
    public int getDead() {
        return dead;
    }

    public int getPopulation() {
        return getHealthy() + getInfected() + getRecovered();
    }

    public int getInfected() {
        int sum = 0;
    	for (int i = 0; i < infectedCohort.length; i++) {
        	sum += infectedCohort[i];
        }
    	return sum;
    }
    
    public int getHealthy() {
		return healthy;
	}
	
    public int getRecovered() {
		return recovered;
	}

	public Color getColor() {
		double r = percentInfected() / 100;
		double g = percentHealthy() / 100;
		double b = percentRecovered() / 100;
		double d = 1 - percentDead() / 100;
		
		if (b > 255) {
			b = 255;
		}
		
        return new Color ((int)(255 * r * d), (int)(255 * g * d), (int)(255 * b * d));
    }
	
	// changes
	public void peopleGetSick(int newlySickCount) throws Exception {
		if (newlySickCount > getHealthy()) {
			throw new Exception("can't have more sick than healthy");
		}
		healthy  -= newlySickCount;
		infectedCohort[0] += newlySickCount;
	}
	
	public void peopleDie(int cohortNum, int newlyDeadCount) throws Exception {
		if (newlyDeadCount > getInfected()) {
			throw new Exception("can't have more dead than infected");
		}
		infectedCohort[cohortNum] -= newlyDeadCount;
		dead += newlyDeadCount;
	}
	
	public void peopleRecover(int cohortNum, int newlyRecoveredCount) throws Exception {
		if (newlyRecoveredCount > getInfected()) {
			throw new Exception("can't have more recovered than infected");
		}
		infectedCohort[cohortNum] -= newlyRecoveredCount;
		recovered += newlyRecoveredCount;
	}

	public double percentHealthy() {
        return (double) getHealthy()/getPopulation() * 100;
    }
	
	public double percentInfected() {
        return (double) getInfected()/getPopulation() * 100;
    }
	
	public double percentDead() {
		return (double) getDead()/initPopulation * 100;
	}
	
	public double percentRecovered() {
		return (double) getRecovered()/getPopulation() * 100;
	}

	public int getInfectedCohort(int i) {
		return infectedCohort[i];
	}
	
	
}

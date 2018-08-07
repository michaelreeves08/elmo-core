package Models;

public class RaceAttribute {
	Race race;
	double confidence;
	
	public RaceAttribute(Race race, double confidence) {
		this.race = race;
		this.confidence = confidence;
	}

	public Race getRace() {
		return race;
	}

	public double getConfidence() {
		return confidence;
	}
}

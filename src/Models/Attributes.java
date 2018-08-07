package Models;

import java.util.ArrayList;

public class Attributes {
	int age;
	
	ArrayList<RaceAttribute> raceAttributes = new ArrayList<>();
	String glasses;
	
	Gender gender;
	
	public Attributes(int age, ArrayList<RaceAttribute> raceAttributes, String glasses, Gender gender) {
		this.age = age;
		this.raceAttributes = raceAttributes;
		this.glasses = glasses;
		this.gender = gender;
	}

	public int getAge() {
		return age;
	}

	public ArrayList<RaceAttribute> getRaceAttributes() {
		return raceAttributes;
	}

	public String getGlasses() {
		return glasses;
	}

	public Gender getGender() {
		return gender;
	}
	
}

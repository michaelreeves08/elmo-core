package utils;

import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import Models.FaceDimensions;
import Models.Race;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundPlayerUtils {
	
	private static String location = "Sound//";
	private static Random rand = new Random();
	
	public static void InterpretAttributes(FaceDimensions face) {
		Race race = DetectionUtils.GetMaxAttribute(face.getAttributes().getRaceAttributes()).getRace();
		
		File[] dir = new File(location + race.name()).listFiles();
		
			Clip clip;
			try {
				System.out.println(dir[rand.nextInt(dir.length)].toURI().toString());
				AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(dir[rand.nextInt(dir.length)].toURI().toString().replaceFirst("file:/", "")));
				
				clip = AudioSystem.getClip();
				clip.open(audioInputStream);
		        clip.start();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	}
	
	public static void PlayRace(Race race) {
		File[] dir = new File(location + race.name()).listFiles();
		MediaPlayer player = new MediaPlayer(new Media(dir[rand.nextInt(dir.length)].toURI().toString()));
		player.play();
	}
}
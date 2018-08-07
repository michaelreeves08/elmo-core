package utils;


import java.util.ArrayList;
import java.util.Arrays;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import Models.Attributes;
import Models.FaceDimensions;
import Models.Gender;
import Models.Race;
import Models.RaceAttribute;

public class DetectionUtils {
	public static FaceDimensions SettingsBuilder(JSONObject obj) {
		JSONObject face;
		try {
			face = obj
					.getJSONArray("images")
					.getJSONObject(0)
					.getJSONArray("faces")
					.getJSONObject(0);
			
			JSONObject demographics = face.getJSONObject("attributes");
			
			System.out.println(obj);
			
			return new FaceDimensions(
					new Point(face.getInt("leftEyeCenterX"), face.getInt("leftEyeCenterY")),
					new Point(face.getInt("rightEyeCenterX"), face.getInt("rightEyeCenterY")),
					new Point(face.getInt("chinTipX"), face.getInt("chinTipY")),
					face.getInt("width"),
					face.getInt("height"),
					face.getInt("eyeDistance"),
					new Attributes(
						demographics.getInt("age"),
						new ArrayList<RaceAttribute>(Arrays.asList(
								new RaceAttribute(Race.Asian, demographics.getDouble("asian")),
								new RaceAttribute(Race.Black, demographics.getDouble("black")),
								new RaceAttribute(Race.Hispanic, demographics.getDouble("hispanic")),
								new RaceAttribute(Race.White, demographics.getDouble("white"))
							)),
						demographics.getString("glasses"),
						new Gender()
						)
				);
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public static RaceAttribute GetMaxAttribute(ArrayList<RaceAttribute> raceAttributes) {
		RaceAttribute max = raceAttributes.get(0);
		for(RaceAttribute att : raceAttributes) 
			if(att.getConfidence() > max.getConfidence())
				max = new RaceAttribute(att.getRace(), att.getConfidence());
		return max;
		
	}
	
	public static void PlaceDims(Mat img, FaceDimensions dims) {
		
		DisplayRace(img, GetMaxAttribute(dims.getAttributes().getRaceAttributes()));

		GenerateFeature(img, dims.getLeftEye(), "Left Eye", 100, 15, false);
		GenerateFeature(img, dims.getRightEye(), "Right Eye", 100, 15, true);
		GenerateFeature(img, dims.getChin(), "Chin", 100, 15, false);
		
		Attributes attributes = dims.getAttributes();
		DisplayHUD(img, GetAttributes(attributes));
		
		RaceAttribute x =  GetMaxAttribute(attributes.getRaceAttributes());
	}
	
	private static String[] GetAttributes(Attributes attributes) {
		
		ArrayList<String> hud = new ArrayList<>();
		
		hud.add("Predictions: ");
		hud.add("Age - " + Integer.toString(attributes.getAge()));
		hud.add("Facial Confidence:");
		
		for(RaceAttribute att : attributes.getRaceAttributes()) 
			hud.add(att.getRace().name() + ": " + Double.toString(att.getConfidence()));
		 
		return hud.toArray(new String[hud.size()]);
	}
	
	private static void DisplayRace(Mat img, RaceAttribute race) {
		Core.putText(img, "Race: ", new Point(40, img.height() - 40), Core.FONT_HERSHEY_SIMPLEX, 1.5, new Scalar(0, 0, 0), 4);
		Core.putText(img, race.getRace().name(), new Point(180, img.height() - 40), Core.FONT_HERSHEY_SIMPLEX, 2.0, new Scalar(255, 255, 255), 4);
	}
	
	private static void DisplayHUD(Mat img, String[] lines) {
		for(int i = 0; i < lines.length; i++) {
			Core.putText(img, lines[i], new Point(10, (i * 20) + 20), Core.FONT_HERSHEY_SIMPLEX, .7, new Scalar(0, 0, 0), 2);
		}
	}
	
	private static void GenerateFeature(Mat img, Point point, String label, int lineSize, int circleRadius, boolean flip) {
		
		if(flip) {
			Core.line(img, point, new Point(point.x - lineSize, point.y - lineSize), new Scalar(255, 0, 0), 2);
			Core.putText(img, label, new Point(point.x - lineSize, point.y - lineSize), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
		}
		else {
			Core.line(img, point, new Point(point.x + lineSize, point.y - lineSize), new Scalar(255, 0, 0), 2);
			Core.putText(img, label, new Point(point.x + lineSize, point.y - lineSize), Core.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
		}
		
	}
}

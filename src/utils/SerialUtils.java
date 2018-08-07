package utils;

import java.awt.Point;

public class SerialUtils {
	
	//Calibration constants
	static int rC = 120;
	static int lC = 60;
	static int tC = 116;
	static int bC = 70;
	static int xOffset = 17;
	static int yOffset = -17;
	
	public static String FormatArduinoCommand(Point p) {
		return "X" + ( (p.x + xOffset)) + ":Y" + (180 - (p.y + yOffset));
	}
	
	public static Point ScalePoint(Point desPoint, Point frameDims)
    {
        return new Point(
        	    120 - (int)(desPoint.x / (frameDims.x / (rC - lC))),
        		116 - (int)(desPoint.y / (frameDims.y / (tC - bC)))
        );
    }
}

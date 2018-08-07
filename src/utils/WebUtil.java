package utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtil {
	
	public static void ShootMemes() {
		MakeGetRequest("http://192.168.1.42:2789/action/fan/on");
	}
	public static void StopMemes() {
		MakeGetRequest("http://192.168.1.42:2789/action/fan/off");
	}
	
	public static void MakeGetRequest(String urlString){ 
		
		try {
			URL url = new URL(urlString);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			con.setRequestMethod("GET");
			con.getInputStream();

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
}

package utils;

import java.net.HttpURLConnection;
import java.net.URL;

public class WebUtils {
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

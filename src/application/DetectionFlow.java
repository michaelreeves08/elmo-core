package application;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public class DetectionFlow {
	
	private static String location = "img//";
	private HttpClient client = HttpClientBuilder.create().build();
	private JSONObject attributes;
	
	public DetectionFlow(Image img) {
		String localName = saveImg(img);
		String serverKey = PostImg(localName);
		System.out.println("FUCKING FILENAME: " + serverKey);
		attributes = GetFeatures(serverKey);
		DeleteImage(localName);
	}
	
	public JSONObject GetAttributes() {
		return attributes;
	}
	
	
	private JSONObject GetFeatures(String serverKey) {

		HttpPost post = new HttpPost("http://api.kairos.com/detect");
		post.setHeader("Content-Type", "application/json");
		post.setHeader("app_id", "eat");
		post.setHeader("app_key", "my_ass");

		StringEntity entity;
		
		entity = new StringEntity("{\"image\":\"http://138.68.232.7/img/" + serverKey + "\"}", "UTF-8");
		entity.setContentType("application/json");
		post.setEntity(entity);
		

		try {
			HttpResponse res = client.execute(post);
			
			HttpEntity resEntity = res.getEntity();
			String jsonString = EntityUtils.toString(resEntity);
			
			if(res.getStatusLine().getStatusCode() == 200) {
				return new JSONObject(jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new JSONObject();
	}
	
	private String PostImg(String key) {
		File file = new File(location + key);
		HttpPost post = new HttpPost("http://138.68.232.7:3000/upload");
		FileBody body = new FileBody(file, ContentType.IMAGE_PNG);
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
		
		builder.addPart("file", body);
		HttpEntity entity = builder.build();
		
		post.setEntity(entity);
		try {
			HttpResponse res = client.execute(post);
			
			if(res.getStatusLine().getStatusCode() == 200)
				return res.getFirstHeader("FileName").getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static String saveImg(Image img) {
		String key = Integer.toString(10000000 + (int)(Math.random() * (99999999 - 10000000))) + ".png";
		System.out.println(key);
		
		try {
			ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", new File(location + key));
			return(key);
		} catch (IOException e) {
			e.printStackTrace();
			return("");
		}
	}
	
	private static void DeleteImage(String imgLocation) {
		try {
			Files.delete(Paths.get(location + imgLocation));
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

package application;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

import Models.FaceDimensions;
import Models.Race;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import utils.CvUtils;
import utils.DetectionUtils;
import utils.SoundPlayerUtils;

public class FXController {
	@FXML
	private Button startButton;
	@FXML
	private ImageView cvFrame;
	
	private boolean playVideo = true;
	private boolean camActive = false;
	private ScheduledExecutorService frameTimer;
	private Timer previewTimer = new Timer();
	private VideoCapture cap ;
	private int faceSize = 0;
	private CascadeClassifier cascade = new CascadeClassifier();
	private Mat displayImg;
	private DetectionSettings faceSettings = new DetectionSettings(1.3, 12, 0 , new Size(1, 1), new Size(2000, 2000));

	@FXML
	protected void initialize() {
		cascade.load("Resource\\face.xml");
		displayImg = Highgui.imread("Resource\\sh.png", BufferedImage.TYPE_3BYTE_BGR);
		
	}
	
	Image img;
	
	@FXML
	protected void startCamera(ActionEvent event) {
		cvFrame.setFitWidth(640);
		cvFrame.setPreserveRatio(true);
		cap = new VideoCapture();

		if(!camActive) {
			cap.open(0);
			if(cap.isOpened()) {
				camActive = true;
				
				Runnable frameGrabber = new Runnable(){
					@Override
					public void run() {
						
						if(playVideo) {
							Mat frame = grabFrame();
							Image tempImg = CvUtils.matToImage(frame);
							updateImageView(cvFrame, tempImg);
						}
					}
				};
				frameTimer = Executors.newSingleThreadScheduledExecutor();
				frameTimer.scheduleAtFixedRate(frameGrabber, 0, 33, TimeUnit.MILLISECONDS);
				
				startButton.setText("Stop");
			}
			else {
				System.err.println("Succ");
			}
		}
		else {
			camActive = false;
			startButton.setText("Start");
			stopAcquisition();
		}
	}
	private void stopAcquisition()
	{
		if (this.frameTimer!=null && !this.frameTimer.isShutdown())
		{
			try
			{
				this.frameTimer.shutdown();
				this.frameTimer.awaitTermination(33, TimeUnit.MILLISECONDS);
			}
			catch (InterruptedException e)
			{
				System.err.println("Exception in stopping the frame capture, trying to release the camera now... " + e);
			}
		}
		
		if (this.cap.isOpened())
		{
			this.cap.release();
		}

	}
	
	
	Point firstFrameDims;	
	private Mat grabFrame() {
		Mat frame = new Mat();
		
		if(cap.isOpened()) {
			try {
				cap.read(frame);
				
				//Get frame dimensions for angular scaling
				if(firstFrameDims == null) {firstFrameDims = new Point(frame.width(), frame.height());}
				
				if(!frame.empty()) {
					img = CvUtils.matToImage(frame);
					processDetections(frame);
				}
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		return frame;
	}
	
	int threshold = 0;
	boolean thresholdMet = false;
	
	private void processDetections(Mat frame) {
		Rect[] detections = detectObjects(frame, faceSettings);
		
		if(!thresholdMet) {
			if(detections.length > 0) {	threshold += 2;} 
			else if(threshold > 0) { threshold--;}
			thresholdMet = threshold > 25;
		}
		
		System.out.println(threshold);
		
		
		drawDetections(frame, detections, thresholdMet ? new Scalar(0, 0, 255) : new Scalar(0, 255, 0));
		
		if(thresholdMet) {
			thresholdMet = false;
			threshold = 0;
			
			JSONObject attributes = new DetectionFlow(img).GetAttributes();
			
			FaceDimensions dims = DetectionUtils.SettingsBuilder(attributes);
			
			
			DetectionUtils.PlaceDims(frame, dims);
			SoundPlayerUtils.InterpretAttributes(dims);
			
			Image tempImg = CvUtils.matToImage(frame);
			updateImageView(cvFrame, tempImg);
			playVideo = false;
			
			previewTimer.schedule(new TimerTask() {
				@Override
				public void run() {
					playVideo = true;
				}
			}, 1000 * 5);
			
		}
	}
	
	
	
	private Rect[] detectObjects(Mat frame, DetectionSettings set) {
		MatOfRect detections = new MatOfRect();
		Mat grey = new Mat();
		
		Imgproc.cvtColor(frame, grey, Imgproc.COLOR_BGR2GRAY);
		Imgproc.equalizeHist(grey, grey);
		
		if(faceSize == 0) {
			int height = grey.rows();
			if(Math.round(height * 0.2f) > 0) {
				faceSize = Math.round(height * 0.2f);
			}
		}
		cascade.detectMultiScale(
				grey,
				detections,
				set.getScaleFactor(), 
				set.getMinNeighbors(), 
				set.getFlags(), 
				set.getMinSize(),
				set.getMaxSize()
				);
		
		//Face Settings
		cascade.detectMultiScale(grey, detections, 1.3, 12, 0 , new Size(1, 1), new Size(2000, 2000));
		
		return detections.toArray();
	}
	
	
	private void drawDetections(Mat frame, Rect[] detectArray, Scalar color) {
		
		for(int i = 0; i < detectArray.length; i++) {
			Core.rectangle(frame, detectArray[i].tl(), detectArray[i].br(), color, 3);
		}
	}
	

	
	private void updateImageView(ImageView view, Image img)
	{
		CvUtils.onFXThread(view.imageProperty(), img);
	}
	
	protected void setClosed()
	{
		this.stopAcquisition();
	}
}

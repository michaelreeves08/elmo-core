package utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Mat;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

public final class CvUtils {
	
	public static Image matToImage(Mat frame) {
		try {
			return SwingFXUtils.toFXImage(matToBufferedImage(frame), null);
		} catch(Exception e) {
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	private static BufferedImage matToBufferedImage(Mat og) {
		BufferedImage img = null;
		int width = og.width(), height = og.height(), channels = og.channels();
		byte[] srcPixels = new byte[width * height * channels];
		og.get(0, 0, srcPixels);
		
		if(og.channels() > 1) {
			img = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		}
		else {
			img = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		}
		
		final byte[] targetPixels = ((DataBufferByte) img.getRaster().getDataBuffer()).getData();
		System.arraycopy(srcPixels, 0, targetPixels, 0, srcPixels.length);
		return img;
	}
	
	public static <T> void onFXThread(final ObjectProperty<T> prop, final T val) {
		Platform.runLater(() -> {
			prop.set(val);
		});
	}
}

package utils;

import java.io.InputStream;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

/**
 * A small class for loading Images 
 */
public class ImageServer {
	private static final int DEFAULT_BUTTON_HEIGHT = 20;
	private static final String BUTTON_FILE_TYPE = ".svg";
	private static final ImageLoader LOADER = new ImageLoader();
	
	private final Display display;
	
	public ImageServer(Display display) {
		this.display = display;
	}
	
	/**
	 * Loads a squared Image from a static path 
	 */
	public Image createImageForButton(String name) {
		
		InputStream stream = getClass().getClassLoader().getResourceAsStream(name + BUTTON_FILE_TYPE);
		LOADER.load(stream);
        ImageData[] imageDataArray = LOADER.data;
        
        int height = DEFAULT_BUTTON_HEIGHT;
        ImageData rescaledImageData = imageDataArray[0].scaledTo(height, height);
        return new Image(display, rescaledImageData);
	}
}

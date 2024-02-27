package utils;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.widgets.Display;

public class ImageServer {
	private static final int DEFAULT_BUTTON_HEIGHT = 20;
	private static final String BUTTON_PATH = "assets/buttons/";
	private static final String BUTTON_FILE_TYPE = ".svg";
	private static final ImageLoader LOADER = new ImageLoader();
	
	private final Display display;
	
	public ImageServer(Display display) {
		this.display = display;
	}
	
	public Image createImageForButton(String name) {
        LOADER.load(BUTTON_PATH + name + BUTTON_FILE_TYPE);
        ImageData[] imageDataArray = LOADER.data;
        
        int height = DEFAULT_BUTTON_HEIGHT;
        ImageData rescaledImageData = imageDataArray[0].scaledTo(height, height);
        return new Image(display, rescaledImageData);
	}
}

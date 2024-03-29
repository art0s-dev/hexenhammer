package unittests.utils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.swt.graphics.Image;
import org.junit.jupiter.api.Test;

import unittests.gui.SWTGuiTestCase;
import utils.ImageServer;

class ImageServerTest extends SWTGuiTestCase {

	@Test
	void testCanLoadImage() {
		Image image = new ImageServer(display).createImageForButton("plus");
		assertNotNull(image);
	}
	
	@Test
	void testIsNullWhenImageIsNotThere() {
		assertThrows(Exception.class, () -> {
			new ImageServer(display).createImageForButton("somethingReallyUnusual");
		});
	}

}

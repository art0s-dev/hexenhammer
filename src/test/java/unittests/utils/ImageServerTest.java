package unittests.utils;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Image;
import org.junit.function.ThrowingRunnable;
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
		assertThrows(SWTException.class, new ThrowingRunnable() {
			public void run() {
				new ImageServer(display).createImageForButton("somethingReallyUnusual");
			}	
		});
	}

}

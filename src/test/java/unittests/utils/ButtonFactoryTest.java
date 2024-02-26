package unittests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.widgets.Button;
import org.junit.jupiter.api.Test;

import unittests.gui.SWTGuiTestCase;
import utils.ButtonFactory;

class ButtonFactoryTest extends SWTGuiTestCase{

	@Test
	void testSetLabel() {
		String testString = "Das ist ein Test";
		ButtonFactory buttonFactory = new ButtonFactory(shell);
		Button button = buttonFactory.createCheckBox(testString);
		
		assertEquals(testString, button.getText());
	}

}

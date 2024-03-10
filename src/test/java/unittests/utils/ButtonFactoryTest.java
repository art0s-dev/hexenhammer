package unittests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.junit.jupiter.api.Test;

import unittests.gui.SWTGuiTestCase;
import utils.GuiFactory;

class ButtonFactoryTest extends SWTGuiTestCase{

	@Test
	void testSetLabel() {
		String testString = "Das ist ein Test";
		GuiFactory buttonFactory = new GuiFactory(shell);
		Button button = buttonFactory.createCheckBox();
		button.setText(testString);
		assertEquals(testString, button.getText());
	}
	
	@Test
	void testInputFieldShallBeEmpty() {
		GuiFactory buttonFactory = new GuiFactory(shell);
		Text text = buttonFactory.createTextInput("Test");
		
		assertEquals("", text.getText());
	}

}

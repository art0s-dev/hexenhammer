package unittests.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static utils.View.placeholder;

import org.eclipse.swt.widgets.Control;
import org.junit.jupiter.api.Test;

import unittests.gui.SWTGuiTestCase;


class ViewTest extends SWTGuiTestCase{

	@Test
	void testPlaceholdersGetAttachtedToParentElement() {
		int numberOfPlaceholder = 6;
		placeholder(numberOfPlaceholder, shell);
		Control[] children = shell.getChildren();
		assertEquals(numberOfPlaceholder, children.length + 1);
	}
}

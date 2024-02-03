package unit.gui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unit.UnitView;

class UnitViewTest extends SWTGuiTestCase {
	
	UnitView view;
	
	@BeforeEach
	void setup() {
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		view = new UnitView(mainTab);
		view.draw();
	}

	@Test
	void testViewCanBeCreatedAndShowALabel() {
		assertEquals("Units", view.getUnitTab().getText());
	}
	
	@Test
	void testUnitEditorWhenItsEmptyShowsCheckBoxDefaultFalse() {
		view.drawEditor(null);
		assertFalse(view.getCheckBoxAddOneToHit().getSelection());
	}

}

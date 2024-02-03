package unittests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;

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
	
	@Test
	void testUnitEditorWhenModelWithAddOneToHitIsGivenThenButtonIsChecked() {
		Unit unit = mock(Unit.class);
		when(unit.has(SpecialRuleUnit.ADD_ONE_TO_HIT)).thenReturn(true);
		view.drawEditor(unit);
		assertTrue(view.getCheckBoxAddOneToHit().getSelection());
	}

}

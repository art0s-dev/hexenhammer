package unittests.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;

class UnitViewTest extends SWTGuiTestCase {
	
	UnitView view;
	boolean selectionTriggerWasActivated;
	
	@BeforeEach
	void setup() {
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		view = new UnitView(mainTab);
		view.draw();
		selectionTriggerWasActivated = false;
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
	
	@Test
	void listWidgetCanTriggerSelectionEvent() {
		List list = new List(shell, SWT.NONE);
		
		list.add("1");
		list.add("2");
		
		list.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				selectionTriggerWasActivated = true;
            }
		});
		
		list.notifyListeners(SWT.Selection, new Event());
		
		list.select(0);
		assertTrue(selectionTriggerWasActivated);
	}
	
	@Test
	void listWidgetCanSwitchBetweenEntries(){
		List list = new List(shell, SWT.NONE);
		
		list.add("1");
		list.add("2");
		list.select(0);
		assertEquals(list.getSelectionIndex(), 0);
		list.select(1);
		assertEquals(list.getSelectionIndex(), 1);
	}
	
	@Test
	void testViewCanRedrawEditor() {
		Unit unit1 = mock(Unit.class);
		when(unit1.has(SpecialRuleUnit.ADD_ONE_TO_HIT)).thenReturn(true);
		Unit unit2 = mock(Unit.class);
		when(unit2.has(SpecialRuleUnit.ADD_ONE_TO_HIT)).thenReturn(false);
		
		view.drawEditor(unit1);
		assertTrue(view.getCheckBoxAddOneToHit().getSelection());
		view.drawEditor(unit2);
		assertFalse(view.getCheckBoxAddOneToHit().getSelection());
	}

}

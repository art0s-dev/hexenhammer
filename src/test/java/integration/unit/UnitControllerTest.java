package integration.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;

class UnitControllerTest extends SWTGuiTestCase {
	
	UnitView view;
	Unit unit1;
	Unit unit2;
	UnitList unitList;
	UnitController controller;
	UnitRepository unitRepo;
	
	@BeforeEach
	void setup() {
		unit1 = mock(Unit.class);
		unit2 = mock(Unit.class);
		view = new UnitView(new TabFolder(shell, SWT.NONE));
	}

	@Test
	void testSwitchingUnitsInUnitsList() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		
		List selectionList = view.getSelectionList();
		selectionList.select(0);
		assertTrue(view.getNameInput().getText().equals(nameUnit1));
		selectionList.setSelection(1);
		selectionList.notifyListeners(SWT.Selection, new Event());
		assertTrue(view.getNameInput().getText().equals(nameUnit2));
	}
	
	@Test
	void testCheckBoxesStayCheckedAfterSwitching() {
		unit1 = new Unit();
		
		refresh();
		
		List selectionList = view.getSelectionList();
		Button checkbox = view.getCheckBoxRerollWound();
		
		//Check the checkbox
		selectionList.select(0);
		checkbox.setSelection(true);
		checkbox.notifyListeners(SWT.Selection, new Event());
		assertTrue(checkbox.getSelection());
		
		//Switch units forth and back to the original unit
		selectionList.setSelection(1);
		selectionList.select(1);
		selectionList.notifyListeners(SWT.Selection, new Event());
		
		selectionList.setSelection(0);
		selectionList.select(0);
		selectionList.notifyListeners(SWT.Selection, new Event());
		
		//Checkbox still has to be checked
		assertTrue(checkbox.getSelection());
	}
	
	private void refresh() {
		ArrayList<Unit> list = new ArrayList<>();
		list.add(unit1);
		list.add(unit2);	
		unitList = new UnitList(list);
		unitRepo = mock(UnitRepository.class);
		when(unitRepo.load()).thenReturn(unitList);
		
		controller = new UnitController(view,unitRepo);
		controller.loadModels();
		controller.initView();
		controller.injectListener();
	}

}

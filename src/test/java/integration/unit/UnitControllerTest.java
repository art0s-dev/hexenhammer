package integration.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Unit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;

@TestMethodOrder(MethodOrderer.Random.class)
class UnitControllerTest extends SWTGuiTestCase {
	
	static UnitView view;
	Unit unit1;
	Unit unit2;
	UnitList unitList;
	UnitController controller;
	UnitRepository unitRepo;
	
	@BeforeEach
	void setupEach() {
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
	void testCanEnterNewUnitName() {
		String nameUnit1 = "My new unit";
		unit1 = new Unit();
		unit1.setName(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		
		String newUnitName = "My new testable unit";
		List selectionList = view.getSelectionList();
		selectionList.select(0);
		assertTrue(view.getNameInput().getText().equals(nameUnit1));
		view.getNameInput().setText(newUnitName);
		view.getNameInput().notifyListeners(SWT.Modify, new Event());
		
		selectionList.select(1);
		selectionList.notifyListeners(SWT.Selection, new Event());
		selectionList.select(0);
		selectionList.notifyListeners(SWT.Selection, new Event());
		assertTrue(view.getNameInput().getText().equals(newUnitName));
	}

	@Test
	void testCheckBoxesStayCheckedAfterSwitching() {
		unit1 = new Unit();
		refresh();
		List selectionList = view.getSelectionList();

		Function<Button, Boolean> testToggler = (btn) -> {
			selectionList.select(0);
			btn.setSelection(true);
			btn.notifyListeners(SWT.Selection, new Event());
			
			//Switch units forth and back to the original unit
			selectionList.setSelection(1);
			selectionList.select(1);
			selectionList.notifyListeners(SWT.Selection, new Event());
			
			btn.setSelection(false);
			btn.notifyListeners(SWT.Selection, new Event());
			
			selectionList.setSelection(0);
			selectionList.select(0);
			selectionList.notifyListeners(SWT.Selection, new Event());
			
			//Checkbox still has to be checked
			return btn.getSelection();
		};
		
		assertTrue(testToggler.apply(view.getCheckBoxAddOneToHit()), "OneToHit");
		assertTrue(testToggler.apply(view.getCheckBoxLethalHits()), "LethalHits");
		assertTrue(testToggler.apply(view.getCheckBoxRerollOnesToHit()), "RerollOnesHit");
		assertTrue(testToggler.apply(view.getCheckBoxRerollHitRoll()), "RerollHit");
		assertTrue(testToggler.apply(view.getCheckBoxAddOneToWound()), "OneToWound");
		assertTrue(testToggler.apply(view.getCheckBoxRerollOnesToWound()), "RerollOnesToWound");
		assertTrue(testToggler.apply(view.getCheckBoxRerollWound()), "RerollWound");
		assertTrue(testToggler.apply(view.getCheckBoxIgnoreCover()), "IgnoreCover");
	}

	@Test
	void testViewListCanAddItems() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		
		view.getSelectionList().select(0); //That would be unit1
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		//We assume that the method changes the selection auomatically to the new item
		
		int numberOfUnits = 7;
		assertEquals(numberOfUnits -1 , view.getSelectionList().getSelectionIndex());
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

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

import core.Probability;
import core.Unit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;
import utils.GuiFactory;
import utils.I18n;

@TestMethodOrder(MethodOrderer.Random.class)
class UnitControllerTest extends SWTGuiTestCase {
	
	static UnitView view;
	Unit unit1;
	Unit unit2;
	UnitList unitList;
	UnitController controller;
	UnitRepository unitRepo;
	Unit wraithGuard;
	
	@BeforeEach
	void setupEach() {
		unit1 = mock(Unit.class);
		unit2 = mock(Unit.class);
		view = new UnitView(new TabFolder(shell, SWT.NONE), new I18n());
		wraithGuard = Unit.builder()
				.name("Wraithguard")
				.movement((byte) 6)
				.toughness((byte) 7)
				.armorSave(Probability.TWO_UP)
				.hitPoints((byte) 3)
				.leadership((byte) 6)
				.objectControl((byte) 1)
				.build();
	}
	
	@Test
	void userCanEnterUnitName() {
		refresh();
		view.getInputName().setText(wraithGuard.getName());
		switchUnits();
		assertEquals(wraithGuard.getName(), view.getInputName().getText());
	}
	
	@Test
	void userCanEnterUnitMovement() {
		refresh();
		view.getInputMovement().setSelection(wraithGuard.getMovement());
		switchUnits();
		assertEquals(wraithGuard.getMovement(), (byte) view.getInputMovement().getSelection());
	}
	
	@Test 
	void userCanEnterUnitsToghness() {
		refresh();
		view.getInputToughness().setSelection(wraithGuard.getToughness());
		switchUnits();
		assertEquals(wraithGuard.getToughness(), (byte) view.getInputToughness().getSelection());
	}
	
	@Test
	void userCanEnterArmorSave() {
		refresh();
		view.getInputArmorSave().select(GuiFactory.mapProbabilityToComboSelection(wraithGuard.getArmorSave()));
		switchUnits();
		assertEquals(wraithGuard.getArmorSave(), 
				GuiFactory.mapComboSelectionToProbability(view.getInputArmorSave().getSelectionIndex()));
	}
	
	@Test 
	void userCanEnterHitPoins() {
		refresh();
		view.getInputHitPoints().setSelection(wraithGuard.getHitPoints());
		switchUnits();
		assertEquals(wraithGuard.getHitPoints(), (byte) view.getInputHitPoints().getSelection());
	}
	
	@Test 
	void userCanEnterLeadership() {
		refresh();
		view.getInputLeadership().setSelection(wraithGuard.getLeadership());
		switchUnits();
		assertEquals(wraithGuard.getLeadership(), (byte) view.getInputLeadership().getSelection());
	}
	
	@Test 
	void userCanEnterObjectiveControl() {
		refresh();
		view.getInputObjectControl().setSelection(wraithGuard.getObjectControl());
		assertEquals(wraithGuard.getObjectControl(), (byte) view.getInputObjectControl().getSelection());
	}
	
	@Test
	void userCanUseComboBox() {
		refresh();
		view.getSelectionList().select(1);
		view.getInputInvulnerableSave().select(1);
		view.getInputInvulnerableSave().notifyListeners(SWT.Selection, new Event());
		float invulSaveProbability = 
				GuiFactory.mapComboSelectionToProbability(view.getInputInvulnerableSave().getSelectionIndex());
		assertEquals(Probability.SIX_UP, invulSaveProbability);
	}

	@Test @Disabled
	void testSwitchingUnitsInUnitsList() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		
		List selectionList = view.getSelectionList();
		selectionList.select(0);
		assertTrue(view.getInputName().getText().equals(nameUnit1));
		selectionList.setSelection(1);
		selectionList.notifyListeners(SWT.Selection, new Event());
		assertTrue(view.getInputName().getText().equals(nameUnit2));
	}
	
	@Test @Disabled
	void testCanEnterNewUnitName() {
		String nameUnit1 = "My new unit";
		unit1 = Unit.builder().build();
		unit1.setName(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		
		String newUnitName = "My new testable unit";
		List selectionList = view.getSelectionList();
		selectionList.select(0);
		assertTrue(view.getInputName().getText().equals(nameUnit1));
		view.getInputName().setText(newUnitName);
		view.getInputName().notifyListeners(SWT.Modify, new Event());
		
		switchUnits();
		assertTrue(view.getInputName().getText().equals(newUnitName));
	}

	@Test
	void testCheckBoxesStayCheckedAfterSwitching() {
		unit1 = Unit.builder().build();
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
		
		assertTrue(testToggler.apply(view.getCheckBoxAddOneToHit()));
		assertTrue(testToggler.apply(view.getCheckBoxLethalHits()));
		assertTrue(testToggler.apply(view.getCheckBoxRerollOnesToHit()));
		assertTrue(testToggler.apply(view.getCheckBoxRerollHitRoll()));
		assertTrue(testToggler.apply(view.getCheckBoxAddOneToWound()));
		assertTrue(testToggler.apply(view.getCheckBoxRerollOnesToWound()));
		assertTrue(testToggler.apply(view.getCheckBoxRerollWound()));
		assertTrue(testToggler.apply(view.getCheckBoxIgnoreCover()));
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
	
	@Test
	void testViewDeleteItemIfSelected() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		view.getSelectionList().select(1);
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
		
		assertEquals(1, view.getSelectionList().getItemCount());
	}
	
	@Test
	void testViewDeleteAllItems() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		refresh();
		switchUnits();
		
		assertEquals(0, view.getSelectionList().getItemCount());
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
	
	private void switchUnits() {
		view.getSelectionList().select(1);
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
		view.getSelectionList().select(0);
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
	}

}

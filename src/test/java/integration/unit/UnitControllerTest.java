package integration.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Unit;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;
import utils.GuiFactory;
import utils.I18n;
import weapon.WeaponController;
import weapon.WeaponList;
import weapon.WeaponRepository;
import weapon.WeaponView;

@TestMethodOrder(MethodOrderer.Random.class)
class UnitControllerTest extends SWTGuiTestCase {
	
	UnitView view;
	Unit unit1;
	Unit unit2;
	UnitList unitList;
	UnitController controller;
	UnitRepository unitRepo;
	Unit wraithGuard;
	Weapon bolter;
	Weapon chainsword;

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
		
		bolter = Weapon.builder()
				.name("bolter")
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.toHit(Probability.THREE_UP)
				.strength((byte)4)
				.armorPenetration((byte) 1)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.range(Range.SHOOTING)
				.build();
		
		chainsword = Weapon.builder()
				.name("chainsword")
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.toHit(Probability.FOUR_UP)
				.strength((byte)4)
				.armorPenetration((byte) 0)
				.range(Range.MELEE)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.build();
		
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
	
	@Test
	void userCanEnterUnitName() {
		view.getInputName().setText(wraithGuard.getName());
		switchUnits();
		assertEquals(wraithGuard.getName(), view.getInputName().getText());
	}
	
	@Test
	void userCanEnterUnitMovement() {
		view.getInputMovement().setSelection(wraithGuard.getMovement());
		switchUnits();
		assertEquals(wraithGuard.getMovement(), (byte) view.getInputMovement().getSelection());
	}
	
	@Test 
	void userCanEnterUnitsToghness() {
		view.getInputToughness().setSelection(wraithGuard.getToughness());
		switchUnits();
		assertEquals(wraithGuard.getToughness(), (byte) view.getInputToughness().getSelection());
	}
	
	@Test
	void userCanEnterArmorSave() {
		view.getInputArmorSave().select(GuiFactory.mapProbabilityToComboSelection(wraithGuard.getArmorSave()));
		switchUnits();
		assertEquals(wraithGuard.getArmorSave(), 
				GuiFactory.mapComboSelectionToProbability(view.getInputArmorSave().getSelectionIndex()));
	}
	
	@Test 
	void userCanEnterHitPoins() {
		view.getInputHitPoints().setSelection(wraithGuard.getHitPoints());
		switchUnits();
		assertEquals(wraithGuard.getHitPoints(), (byte) view.getInputHitPoints().getSelection());
	}
	
	@Test 
	void userCanEnterLeadership() {
		view.getInputLeadership().setSelection(wraithGuard.getLeadership());
		switchUnits();
		assertEquals(wraithGuard.getLeadership(), (byte) view.getInputLeadership().getSelection());
	}
	
	@Test 
	void userCanEnterObjectiveControl() {
		view.getInputObjectControl().setSelection(wraithGuard.getObjectControl());
		assertEquals(wraithGuard.getObjectControl(), (byte) view.getInputObjectControl().getSelection());
	}
	
	@Test
	void userCanUseComboBox() {
		view.getSelectionList().select(1);
		view.getInputInvulnerableSave().select(1);
		view.getInputInvulnerableSave().notifyListeners(SWT.Selection, new Event());
		float invulSaveProbability = 
				GuiFactory.mapComboSelectionToProbability(view.getInputInvulnerableSave().getSelectionIndex());
		assertEquals(Probability.SIX_UP, invulSaveProbability);
	}

	@Test
	void testSwitchingUnitsInUnitsList() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
		List selectionList = view.getSelectionList();
		selectionList.select(1);
		selectionList.notifyListeners(SWT.Selection, new Event());
		assertTrue(view.getInputName().getText().equals(nameUnit2));
	}
	
	@Test
	void testCanEnterNewUnitName() {
		String nameUnit1 = "My new unit";
		unit1 = Unit.builder().build();
		unit1.setName(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		String newUnitName = "My new testable unit";
		view.getInputName().setText(newUnitName);
		view.getInputName().notifyListeners(SWT.Modify, new Event());
		switchUnits();
		assertTrue(view.getInputName().getText().equals(newUnitName));
	}
	
	@Test
	void testCheckBoxAddOneToHits() {
		view.getCheckBoxAddOneToHit().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxAddOneToHit().getSelection());
	}
	
	@Test
	void testCheckBoxLethalHits() {
		view.getCheckBoxLethalHits().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxLethalHits().getSelection());
	}
	
	@Test
	void testCheckBoxRerollOnes() {
		view.getCheckBoxRerollOnesToHit().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxRerollOnesToHit().getSelection());
	}
	
	@Test
	void testCheckBoxRerollHit() {
		view.getCheckBoxRerollHitRoll().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxRerollHitRoll().getSelection());
	}
	
	@Test
	void testCheckBoxAddOneToWound() {
		view.getCheckBoxAddOneToWound().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxAddOneToWound().getSelection());
	}
	
	@Test
	void testCheckBoxRerollOnesToWound() {
		view.getCheckBoxRerollOnesToWound().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxRerollOnesToWound().getSelection());
	}
	
	@Test
	void testCheckBoxRerollWound() {
		view.getCheckBoxRerollWound().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxRerollWound().getSelection());
	}
	
	@Test
	void testCheckBoxIgnoreCover() {
		view.getCheckBoxIgnoreCover().setSelection(true);
		switchUnits();
		assertTrue(view.getCheckBoxIgnoreCover().getSelection());
	}

	@Test
	void testViewListCanAddItems() {
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		
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
		switchUnits();
		assertEquals(0, view.getSelectionList().getItemCount());
	}
	
	@Test
	void testWeaponControllerHasNotBeenSetMeansWeaponryIsNotEnabled() {
		assertFalse(view.getEquipButton().isEnabled());
	}
	
	@Test
	void testWeaponControllerHasBeenSetAndWeaponsAreEmptyMeansWeaponryIsStillNotEnabled() {
		WeaponView weaponView = new WeaponView(shell, new I18n());
		WeaponController weaponController = new WeaponController(weaponView, new WeaponRepository());
		weaponController.loadModels();
		weaponController.initView();
		weaponController.injectListener();
		controller.setWeaponController(weaponController);
		
		assertFalse(view.getEquipButton().isEnabled());
	}
	
	@Test
	void testWeaponControllerHasBeenSetAndWeaponsAreSetMeansWeaponryIsEnabled() {
		_addController();
		assertTrue(view.getEquipButton().isEnabled());
	}
	
	@Test
	void testWhenWeaponControllerAndListIsSetUserCanEquipWeapons() {
		_addController();
		controller.updateWeaponry();
		
		//When Choosing a weapon on the Repo List we then expect the selection to be set
		view.getAllWeaponsList().select(0);
		view.getAllWeaponsList().setSelection(0);
		boolean somethingWasSelected = view.getAllWeaponsList().getSelectionIndex() > -1;
		assert somethingWasSelected;
		
		view.getWeaponQuantityInput().setSelection(10);
		view.getEquipButton().notifyListeners(SWT.Selection, new Event());
		boolean weaponGotInserted = view.getEquipmentList().getItems().length > 0;
		assert weaponGotInserted;
		
		assertEquals("10x bolter", view.getEquipmentList().getItem(0));
	}
	
	private void switchUnits() {
		view.getSelectionList().select(1);
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
		view.getSelectionList().select(0);
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
	}
	
	private void _addController() {
		ArrayList<Weapon> list = new ArrayList<>();
		list.add(bolter);
		list.add(chainsword);	 
		WeaponList weaponList = new WeaponList(list);
		
		WeaponView weaponView = new WeaponView(shell, new I18n());
		WeaponRepository weaponRepository = mock(WeaponRepository.class);
		when(weaponRepository.load()).thenReturn(weaponList);
		WeaponController weaponController = new WeaponController(weaponView, weaponRepository);
		weaponController.loadModels();
		weaponController.initView();
		weaponController.injectListener();
		controller.setWeaponController(weaponController);
		controller.initView();
		controller.injectListener();
	}

}

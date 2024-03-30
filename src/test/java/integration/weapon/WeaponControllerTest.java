package integration.weapon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.Probability;
import core.Probability.Dice;
import core.Unit;
import core.Unit.Type;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;
import unittests.gui.SWTGuiTestCase;
import utils.GuiFactory;
import utils.I18n;
import weapon.WeaponController;
import weapon.WeaponList;
import weapon.WeaponRepository;
import weapon.WeaponView;

class WeaponControllerTest extends SWTGuiTestCase{
	
	Weapon bolter;
	Weapon chainsword;
	WeaponView view;
	
	@BeforeEach
	void setup() {
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
		
		view = new WeaponView(shell, new I18n());
		WeaponRepository repo = mock(WeaponRepository.class);
		
		ArrayList<Weapon> list = new ArrayList<>();
		list.add(bolter);
		list.add(chainsword);
		
		when(repo.load()).thenReturn(new WeaponList(list));
		WeaponController controller = new WeaponController(view, repo);
		
		controller.loadModels();
		controller.initView();
		controller.injectListener();
	}

	@Test
	void testAddingAWeapon() {
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		assertEquals(3, view.getSelectionList().getItemCount());
	}
	
	@Test
	void testSwitchingWeapons() {
		//Bolter is First has armor pen of 1
		view.getSelectionList().select(1);
		view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		byte armorPenetration = (byte) view.getInputArmorPenetration().getSelection();
		assertEquals(armorPenetration, chainsword.getArmorPenetration());
	}
	
	@Test
	void testSwitchingBetweenMeeleeAndShooting() {
		view.getWeaponRangeMeelee().notifyListeners(SWT.Selection, new Event());//now it should me a meelee weapon.
		view.getWeaponRangeShooting().notifyListeners(SWT.Selection, new Event()); //now a shooting weapon
		assertTrue(view.getWeaponRangeShooting().getSelection());
	}
	
	@Test
	void testSwitchingBetweenMeeleeAndShootingKeepsPersistence() {
		view.getWeaponRangeMeelee().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertTrue(view.getWeaponRangeMeelee().getSelection());
	}
	
	@Test
	void testSwitchingBetweenFixedAttackNumberAndDicesDeactivatesFixedNumber() {
		view.getRadioAttackInputDice().notifyListeners(SWT.Selection, new Event());
		assertFalse(view.getInputAttackInputFixedNumber().isEnabled());
		assertFalse(view.getRadioAttackInputFixedNumber().getSelection()); //is either Dice or fixed
	}
	
	@Test
	void testSwitchingBetweenDiceAndFixedAttackDeactivatesDice() {
		view.getRadioAttackInputFixedNumber().notifyListeners(SWT.Selection, new Event());
		
		assertTrue(view.getInputAttackInputFixedNumber().isEnabled());
		assertTrue(view.getRadioAttackInputFixedNumber().getSelection()); 
		assertFalse(view.getRadioAttackInputDice().getSelection());
	}
	
	@Test
	void testAddingMoreAttacks() {
		view.getInputAttackInputFixedNumber().setSelection(12);
		view.getInputAttackInputFixedNumber().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(12, view.getInputAttackInputFixedNumber().getSelection());
	}
	
	@Test
	void testAddingDiceQuantity() {
		view.getInputAttackInputDice().setSelection(12);
		view.getInputAttackInputDice().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(12, view.getInputAttackInputDice().getSelection());
	}
	
	@Test
	void testChangingDice() {
		int index = GuiFactory.mapDiceToComboSelection(Dice.d6);
		view.getInputAttackInputDiceChooser().select(index);
		view.getInputAttackInputDiceChooser().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(index, view.getInputAttackInputDiceChooser().getSelectionIndex());
	}
	
	@Test
	void testDeleteAllItems() {
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
		view.getDeleteButton().notifyListeners(SWT.Selection, new Event());
		assertEquals(0, view.getSelectionList().getItemCount());
	}
	
	@Test
	void testDamageInputSwitchBetweenFixedNumberAndDice() {
		view.getRadioDamageInputDice().setSelection(true);
		view.getRadioDamageInputDice().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertTrue(view.getRadioDamageInputDice().getSelection());
	}
	
	@Test
	void testDamageInputSwitchBetweenDiceAndFixedNumber() {
		view.getRadioDamageInputFixedNumber().setSelection(true);
		view.getRadioDamageInputFixedNumber().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertTrue(view.getRadioDamageInputFixedNumber().getSelection());
	}
	
	@Test
	void testDamageInputFixedNumber() {
		view.getInputDamageInputFixedNumber().setSelection(15);
		view.getInputDamageInputFixedNumber().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(15, view.getInputDamageInputFixedNumber().getSelection());
	}
	
	@Test
	void testDamageInputDiceQuantity() {
		view.getInputDamageInputDice().setSelection(15);
		view.getInputDamageInputDice().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(15, view.getInputDamageInputDice().getSelection());
	}
	
	@Test
	void testDamageInputChangingDice() {
		int index = GuiFactory.mapDiceToComboSelection(Dice.d6);
		view.getInputDamageInputDiceChooser().select(index);
		view.getInputDamageInputDiceChooser().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(index, view.getInputDamageInputDiceChooser().getSelectionIndex());
	}
	
	@Test
	void testToHitCombo() {
		view.getInputToHit().select(GuiFactory.mapProbabilityToComboSelection(Probability.TWO_UP));
		view.getInputToHit().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		int index = view.getInputToHit().getSelectionIndex();
		float selectedProbability = GuiFactory.mapComboSelectionToProbability(index);
		assertEquals(Probability.TWO_UP, selectedProbability);
	}
	
	@Test 
	void testStrengthInput() {
		view.getInputStrenght().setSelection(12);
		view.getInputStrenght().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		assertEquals(12, view.getInputStrenght().getSelection());
	}
	
	@Test
	void testSwitchAntiTypeProbability() {
		float selectedProbability = Probability.TWO_UP;
		view.getAntiTypeProbabilityCombo().select(GuiFactory.mapProbabilityToComboSelection(selectedProbability));
		view.getAntiTypeProbabilityCombo().notifyListeners(SWT.Selection, new Event());
		assert view.getAntiTypeProbabilityCombo().getSelectionIndex() > 4;
		_switchWeapons();
		int index = view.getAntiTypeProbabilityCombo().getSelectionIndex();
		float probability = GuiFactory.mapComboSelectionToProbability(index);
		assertEquals(selectedProbability, probability);
	}
	
	@Test
	void testSwitchAntiType() {
		view.getAntiTypeUnitTypeCombo().select(GuiFactory.mapTypeEnumToComboSelection(Unit.Type.MONSTER));
		view.getAntiTypeUnitTypeCombo().notifyListeners(SWT.Selection, new Event());
		_switchWeapons();
		int index = view.getAntiTypeUnitTypeCombo().getSelectionIndex();
		Type unitType = GuiFactory.mapUnitTypeComboSelectionToEnum(index);
		assertEquals(Unit.Type.MONSTER, unitType);
	}
	
	private void _switchWeapons() {
		view.getSelectionList().select(1);
		view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		view.getSelectionList().select(0);
		view.getSelectionList().notifyListeners(SWT.Selection, new Event());
	}
	
	
	
	
}
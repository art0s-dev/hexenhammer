package unittests.weapon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Probability.Dice;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;
import core.Weapon.SpecialRuleWeapon;
import unittests.gui.SWTGuiTestCase;
import utils.GuiFactory;
import utils.I18n;
import weapon.WeaponView;

class WeaponViewTest extends SWTGuiTestCase {
	
	WeaponView view;
	Weapon weapon;

	@BeforeEach
	void setup() {
		view = new WeaponView(shell, new I18n());
		view.draw();
		weapon = mock(Weapon.class);
	}
	
	@Test
	void testUnitEditorWhenItsEmptyShowsCheckBoxDefaultFalse() {
		view.drawEditor(null);
		assertFalse(view.getCheckBoxHeavyAndStationary().getSelection());
	}
	
	@Test
	void testWeaponSpecialRuleIsSetTrueThenCheckboxIsTrue() {
		when(weapon.has(SpecialRuleWeapon.TORRENT)).thenReturn(true);
		view.drawEditor(weapon);
		assertTrue(view.getCheckBoxTorrent().getSelection());
	}
	
	@Test
	void testEditorDrawsUnitAndCanReadWeaponRange() {
		when(weapon.getRange()).thenReturn(Range.SHOOTING);
		view.drawEditor(weapon);
		assertTrue(view.getWeaponRangeShooting().getSelection());
	}
	
	@Test
	void testEditorDrawsUnitThatHasNoRange_thenDefaultIsAlwaysShooting() {
		when(weapon.getRange()).thenReturn(null);
		view.drawEditor(weapon);
		assertTrue(view.getWeaponRangeShooting().getSelection());
	}
	
	@Test
	void testEditorDrawsUnit_whenNoAttackInputIsGiven_thenDisplayFixedNumberZero() {
		when(weapon.getAttackInput()).thenReturn(Optional.empty());
		view.drawEditor(weapon);
		assertTrue(view.getRadioAttackInputFixedNumber().getSelection());
		assertFalse(view.getRadioAttackInputDice().getSelection());
		assertEquals(0, view.getInputAttackInputFixedNumber().getSelection());
	}
	
	@Test
	void testEditorDrawsUnit_whenAttackInputIsEntered_thenDisplayItCorrectly() {
		Optional<UserNumberInput> attackInput = 
				Optional.of(UserNumberInput.withDice((byte)5, Dice.d6));
		
		when(weapon.getAttackInput()).thenReturn(attackInput);
		view.drawEditor(weapon);
		assertTrue(view.getRadioAttackInputDice().getSelection());
		assertFalse(view.getRadioAttackInputFixedNumber().getSelection());
		assertEquals(5, view.getInputAttackInputDice().getSelection());
		int index = view.getInputAttackInputDiceChooser().getSelectionIndex();
		assertEquals(Dice.d6, GuiFactory.mapComboSelectionToDice(index));
	}
	
	@Test
	void testEditorDrawsUnit_whennAttackInputIsDice_thenFixedNumberInputIsDisabled() {
		Optional<UserNumberInput> attackInput = 
				Optional.of(UserNumberInput.withDice((byte)5, Dice.d6));
		
		when(weapon.getAttackInput()).thenReturn(attackInput);
		view.drawEditor(weapon);
		assertFalse(view.getInputAttackInputFixedNumber().isEnabled());
		assertTrue(view.getInputAttackInputDice().isEnabled());
		assertTrue(view.getInputAttackInputDiceChooser().isEnabled());
	}
	
	@Test
	void testEditorDrawsUnit_whennAttackInputIsFixed_thenDiceInputIsDisabled() {
		Optional<UserNumberInput> attackInput = Optional.of(UserNumberInput.withNumber((byte) 22));
		when(weapon.getAttackInput()).thenReturn(attackInput);
		view.drawEditor(weapon);
		assertTrue(view.getInputAttackInputFixedNumber().isEnabled());
		assertFalse(view.getInputAttackInputDice().isEnabled());
		assertFalse(view.getInputAttackInputDiceChooser().isEnabled());
	}
	
	

}

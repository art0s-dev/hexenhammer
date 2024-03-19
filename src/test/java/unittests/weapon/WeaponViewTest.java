package unittests.weapon;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.Weapon;
import core.Weapon.SpecialRuleWeapon;
import unittests.gui.SWTGuiTestCase;
import utils.I18n;
import weapon.WeaponView;

class WeaponViewTest extends SWTGuiTestCase {
	
	WeaponView view;

	@BeforeEach
	void setup() {
		view = new WeaponView(shell, new I18n());
		view.draw();
	}
	
	@Test
	void testUnitEditorWhenItsEmptyShowsCheckBoxDefaultFalse() {
		view.drawEditor(null);
		assertFalse(view.getCheckBoxHeavyAndStationary().getSelection());
	}
	
	@Test @Disabled
	void testWeaponSpecialRuleIsSetTrueThenCheckboxIsTrue() {
		Weapon weapon = mock(Weapon.class);
		when(weapon.has(SpecialRuleWeapon.TORRENT)).thenReturn(true);
		view.drawEditor(weapon);
		assertTrue(view.getCheckBoxTorrent().getSelection());
	}

}

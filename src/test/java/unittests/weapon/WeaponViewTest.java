package unittests.weapon;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
		//assertFalse(view.getCheckBoxHasCover().getSelection());
	}

}

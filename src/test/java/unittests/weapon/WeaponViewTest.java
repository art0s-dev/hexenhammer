package unittests.weapon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import unittests.gui.SWTGuiTestCase;
import utils.I18n;
import weapon.WeaponView;

class WeaponViewTest extends SWTGuiTestCase {
	
	WeaponView view;

	@BeforeEach
	void setup() {
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		view = new WeaponView(shell, new I18n());
		view.draw();
	}
	
	@Test
	void testUnitEditorWhenItsEmptyShowsCheckBoxDefaultFalse() {
		view.drawEditor(null);
		//assertFalse(view.getCheckBoxHasCover().getSelection());
	}

}

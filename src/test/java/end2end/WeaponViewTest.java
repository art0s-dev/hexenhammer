package end2end;

import org.eclipse.swt.widgets.Shell;

import utils.I18n;
import weapon.WeaponView;

public class WeaponViewTest extends SWTEnd2EndTestcase {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Shell shell = before();
		test(shell);
		after(shell);
	}

	private static void test(Shell shell) {
		I18n i18n = new I18n();
		i18n.setLanguage(I18n.german());
		WeaponView view = new WeaponView(shell, i18n);
		view.draw();
	}
}


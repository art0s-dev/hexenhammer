package end2end;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import utils.I18n;
import weapon.WeaponView;

public class WeaponViewTest {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setSize(1200, 900);
		shell.setLayout(new GridLayout(1, true));
		
		WeaponView view = new WeaponView(shell, new I18n());
		view.draw();
		
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose ();
	}
}


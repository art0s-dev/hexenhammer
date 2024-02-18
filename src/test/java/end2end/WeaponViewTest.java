package end2end;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import weapon.WeaponView;

public class WeaponViewTest {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		mainTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		WeaponView view = new WeaponView(mainTab);
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


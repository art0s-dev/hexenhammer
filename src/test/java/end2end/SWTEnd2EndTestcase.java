package end2end;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Shell;

public abstract class SWTEnd2EndTestcase {

	public static Shell before() {
		Shell shell = new Shell();
		shell.setSize(1200, 900);
		shell.setLayout(new GridLayout(1, true));
		return shell;
	}
	
	public static void after(Shell shell) {
		shell.open();
		while (!shell.isDisposed ()) {
			if (!shell.getDisplay().readAndDispatch ()) {
				shell.getDisplay().sleep ();
			}
		}
		shell.getDisplay().dispose();
	}
}

package end2end;

import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import menu.MenuView;

public class MenuViewTest {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		
		MenuView menu = new MenuView(shell);
		menu.draw();

		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose ();
	}
}

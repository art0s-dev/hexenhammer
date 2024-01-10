package gui;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class MainWindow {
	
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setSize(1920, 1080);
		shell.setLayout(new GridLayout(1, false));
		
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
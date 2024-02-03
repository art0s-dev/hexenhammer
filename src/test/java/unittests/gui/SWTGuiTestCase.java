package unittests.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.jupiter.api.BeforeEach;

public class SWTGuiTestCase {
	
	public Shell shell;
	public Display display;
	
	@BeforeEach
	void setUpSwtGui() {
		display = Display.getDefault();
		shell = new Shell(display, SWT.CLOSE); 
	}
}

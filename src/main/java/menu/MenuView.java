package menu;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Shell;

public class MenuView {
	private final Shell shell;
	
	public MenuView(Shell shell) {
		this.shell = shell;
	}
	
	public void draw() {
		CoolBar menuBar = new CoolBar(shell, SWT.NONE);
		menuBar.setLayout(new FillLayout());
		GridData grid = new GridData(SWT.CENTER, SWT.LEFT, false, true);
		grid.heightHint = 60;
		menuBar.setLayoutData(grid);
		
		Button btn = new Button(menuBar, SWT.NONE);
		btn.setText("Test");
		
		Button btn1 = new Button(menuBar, SWT.NONE);
		btn1.setText("Test");
		
		Button btn2 = new Button(menuBar, SWT.NONE);
		btn2.setText("Test");
		
		Button btn3 = new Button(menuBar, SWT.NONE);
		btn3.setText("Test");
		
		
		
	}
}

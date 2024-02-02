package gui;

import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import core.Unit;

public class MainWindow {
	
	List<Unit> unitList;
	Unit selection;
	
	public static void main(String[] args) {
		//Set the shell
		Display display = new Display();
		Shell shell = new Shell();
		//shell.setSize(1920, 1080); 
		shell.setLayout(new FillLayout());
		
		//Build the Tab Folder
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		mainTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		_UnitView unitView = new _UnitView(mainTab);
		unitView.draw();

		//Main loop
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
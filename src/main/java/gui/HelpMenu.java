package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

public class HelpMenu {
	
	private final static String HELP_URL = "https://github.com/art0s-dev/hexenhammer/wiki"; 
	private final static String BUGREPORT_URL = "https://github.com/art0s-dev/hexenhammer/issues";
	
	private final Menu mainMenu;
	
	public HelpMenu(Menu mainMenu) {
		this.mainMenu = mainMenu;
	}
	
	public void create() {
		MenuItem mntmReportABug = new MenuItem(mainMenu, SWT.CASCADE);
		mntmReportABug.setText("Help");
		
		Menu menu_1_1 = new Menu(mntmReportABug);
		mntmReportABug.setMenu(menu_1_1);
		
		MenuItem mntmReportABug_1 = new MenuItem(menu_1_1, SWT.NONE);
		mntmReportABug_1.setText("Report a bug");
		mntmReportABug_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			     Program.launch(BUGREPORT_URL);
			}
		});
		
		MenuItem mntmGetHelpFrom = new MenuItem(menu_1_1, SWT.NONE);
		mntmGetHelpFrom.setText("Get help from the wiki");
		mntmGetHelpFrom.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			     Program.launch(HELP_URL);
			}
		});
	}

}

package gui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import gui.tabs.EnemiesTab;
import gui.tabs.ITab;
import gui.tabs.ResultsTab;
import gui.tabs.UnitTab;
import gui.tabs.WeaponsTab;
import lombok.val;

public class MainWindow {
	
	private final Display display;
	private final Shell shell;
	private TabFolder mainFolder;
	private Menu mainMenu;
	
	private MainWindow(Display display, Shell shell) {
		this.display = display;
		this.shell = shell;
	}
	
	public static void main(String[] args) {
		val mainWindow = new MainWindow(new Display(), new Shell());
		mainWindow._setShell();
		mainWindow._createTabs();
		mainWindow._createMainMenu();
		mainWindow._createHelpMenu();
		mainWindow._mainLoop();
	}

	private void _setShell() {
		shell.setSize(1920, 1080);
		shell.setLayout(new GridLayout(1, false));
	}
	
	private void _createTabs() {
		mainFolder = new TabFolder(shell, SWT.NONE);
		mainFolder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
		List<ITab> tabs = new ArrayList<ITab>();
		tabs.add(new EnemiesTab(mainFolder));
		tabs.add(new ResultsTab(mainFolder));
		tabs.add(new UnitTab(mainFolder));
		tabs.add(new WeaponsTab(mainFolder));
		tabs.stream().forEach(tab -> tab.createTab());
	}
	
	private void _createMainMenu() {
		mainMenu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(mainMenu);
		mainMenu.setOrientation(SWT.RIGHT_TO_LEFT);
	}
	
	private void _createHelpMenu() {
		val helpMenu = new HelpMenu(mainMenu);
		helpMenu.create();
	}
	
	private void _mainLoop() {
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	
}
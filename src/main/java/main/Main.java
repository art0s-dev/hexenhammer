package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import statistics.StatisticsController;
import statistics.StatisticsView;
import unit.UnitController;
import unit.UnitRepository;
import unit.UnitView;
import utils.I18n;
import utils.Lambda;
import weapon.WeaponController;
import weapon.WeaponRepository;
import weapon.WeaponView;

public class Main {
	public static void main(String[] args) {
		I18n i18n = new I18n();
		i18n.setLanguage(I18n.german());
		
		//Set GUI
		Shell shell = new Shell();
		shell.setSize(1980, 1200);
		shell.setLayout(new GridLayout(1, true));
		shell.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		shell.setText(i18n.get("general.slogan"));
		
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		
		Menu helpMenu = new Menu(shell, SWT.DROP_DOWN);
		
		MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
		fileItem.setText(i18n.get("menu.HelpMenu.title"));
		fileItem.setMenu(helpMenu);
		
		MenuItem helpItem = new MenuItem(helpMenu, SWT.PUSH);
		helpItem.setText(i18n.get("menu.HelpMenu.wiki"));
		helpItem.addSelectionListener(Lambda.select(() -> {
			Program.launch("https://github.com/art0s-dev/hexenhammer/wiki");
		}));
		
		MenuItem issueItem = new MenuItem(helpMenu, SWT.PUSH);
		issueItem.setText(i18n.get("menu.HelpMenu.issue"));
		issueItem.addSelectionListener(Lambda.select(() -> {
			Program.launch("https://github.com/art0s-dev/hexenhammer/issues");
		}));

		
		//Set the Tabs
		TabFolder folder = new TabFolder(shell, SWT.NONE);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//Set Unit Tab
		TabItem unitTab = new TabItem(folder, SWT.NONE);
		unitTab.setText(i18n.get("unit.UnitView"));
		Composite unitComposite = new Composite(folder, SWT.NONE);
		unitComposite.setLayout(new GridLayout(1, true));
		unitComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		UnitView unitView = new UnitView(unitComposite, i18n);
		unitTab.setControl(unitComposite);
		UnitController unitController = new UnitController(unitView, new UnitRepository());
		unitController.loadModels();
		unitController.initView();
		unitController.injectListener();
		
		//Set Weapon Tab
		TabItem weaponTab = new TabItem(folder, SWT.NONE);
		weaponTab.setText(i18n.get("weapon.WeaponView"));
		Composite weaponComposite = new Composite(folder, SWT.NONE);
		weaponComposite.setLayout(new GridLayout(1, true));
		weaponComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		WeaponView weaponView = new WeaponView(weaponComposite, i18n);
		weaponTab.setControl(weaponComposite);
		WeaponController weaponController = new WeaponController(weaponView, new WeaponRepository());
		weaponController.setUnitController(unitController);
		weaponController.loadModels();
		weaponController.initView();
		weaponController.injectListener();
		unitController.setWeaponController(weaponController);
		
		//Set Statistics Tab
		TabItem statisticsTab = new TabItem(folder, SWT.NONE);
		statisticsTab.setText(i18n.get("statistics.StatisticsView"));
		Composite statisticsComposite = new Composite(folder, SWT.NONE);
		statisticsComposite.setLayout(new GridLayout(1, true));
		statisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		StatisticsView statisticsView = new StatisticsView(statisticsComposite, i18n);
		statisticsTab.setControl(statisticsComposite);
		StatisticsController statisticsController = 
				new StatisticsController(statisticsView, unitController);
		statisticsController.loadModels();
		statisticsController.initView();
		statisticsController.injectListener();
		
		//Main Loop
		shell.open();
		while (!shell.isDisposed ()) {
			if (!shell.getDisplay().readAndDispatch ()) {
				shell.getDisplay().sleep ();
			}
		}
		shell.getDisplay().dispose();
	}
}

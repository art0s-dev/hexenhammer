package main;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import unit.UnitController;
import unit.UnitRepository;
import unit.UnitView;
import utils.I18n;
import weapon.WeaponController;
import weapon.WeaponRepository;
import weapon.WeaponView;

public class Main {
	public static void main(String[] args) {
		Shell shell = before();
		shell.setSize(1920, 1200);
		shell.setLayout(new GridLayout(1, true));
		I18n i18n = new I18n();
		i18n.setLanguage(I18n.german());
		
		TabFolder folder = new TabFolder(shell, SWT.NONE);
		folder.setLayout(new GridLayout(1, true));
		folder.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		TabItem unitTab = new TabItem(folder, SWT.NONE);
		unitTab.setText("Units");
		Composite unitComposite = new Composite(folder, SWT.NONE);
		unitComposite.setLayout(new GridLayout(1, true));
		unitComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		UnitView unitView = new UnitView(unitComposite, i18n);
		unitTab.setControl(unitComposite);
		UnitController unitController = new UnitController(unitView, new UnitRepository());
		unitController.loadModels();
		unitController.initView();
		unitController.injectListener();
		
		TabItem weaponTab = new TabItem(folder, SWT.NONE);
		weaponTab.setText("Weapons");
		Composite weaponComposite = new Composite(folder, SWT.NONE);
		weaponComposite.setLayout(new GridLayout(1, true));
		weaponComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		WeaponView weaponView = new WeaponView(weaponComposite, i18n);
		weaponTab.setControl(weaponComposite);
		WeaponController weaponController = new WeaponController(weaponView, new WeaponRepository());
		weaponController.loadModels();
		weaponController.initView();
		weaponController.injectListener();
		
		after(shell);
	}
	
	public static Shell before() {
		Shell shell = new Shell();
		shell.setSize(1200, 900);
		shell.setLayout(new GridLayout(1, true));
		shell.setText("Hexenhammer 40K - Your statistic servitor");
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

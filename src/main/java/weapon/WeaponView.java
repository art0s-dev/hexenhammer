package weapon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import arch.Model;
import arch.ModelList;
import arch.View;
import lombok.val;

public class WeaponView implements View{
	
	//Dependencys
	private final TabFolder mainTab;
	private final Display display;
	
	//language string
	private static final String TAB_NAME = "Weapons";
	
	//"Recycled" Widgets - you mostly have to use draw() before using these
	private Composite compositeWeaponEditor;
	
	public WeaponView(TabFolder mainTab) {
		this.mainTab = mainTab;
		this.display = mainTab.getDisplay();
	}

	@Override
	public void draw() {
		TabItem weaponTab = new TabItem(mainTab, SWT.NONE);
		weaponTab.setText(TAB_NAME);
		
		Composite compositeWeapons = new Composite(mainTab, SWT.NONE);
		compositeWeapons.setLayout(new FillLayout());
		weaponTab.setControl(compositeWeapons);
		
		SashForm sashFormUnits = new SashForm(compositeWeapons, SWT.HORIZONTAL);
		sashFormUnits.SASH_WIDTH = 3;
		sashFormUnits.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		
		val layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		
		//List View
		Composite compositeWeaponList = new Composite(sashFormUnits, SWT.NONE);
		compositeWeaponList.setLayout(layout);
		compositeWeaponList.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		//Editor View
		Composite compositeUnitList = new Composite(sashFormUnits, SWT.NONE);
		compositeUnitList.setLayout(layout);
		compositeUnitList.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
	}

	@Override
	public void drawList(ModelList modelList) {
		
	}

	@Override
	public void drawEditor(Model model) {
		
	}
	
	
}

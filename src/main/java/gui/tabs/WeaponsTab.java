package gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class WeaponsTab implements ITab {
	private final TabFolder mainFolder;
	
	public WeaponsTab(TabFolder mainFolder) {
		this.mainFolder = mainFolder;
	}

	public void createTab() {
		TabItem tbtmWeapons = new TabItem(mainFolder, SWT.NONE);
		tbtmWeapons.setText("Weapons");
	}
}

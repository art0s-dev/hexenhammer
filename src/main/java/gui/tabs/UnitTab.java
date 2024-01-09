package gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class UnitTab implements ITab {
	private final TabFolder mainFolder;
	
	public UnitTab(TabFolder mainFolder) {
		this.mainFolder = mainFolder;
	}

	public void createTab() {
		TabItem tbtmUnits = new TabItem(mainFolder, SWT.NONE);
		tbtmUnits.setText("Units");
	}
}

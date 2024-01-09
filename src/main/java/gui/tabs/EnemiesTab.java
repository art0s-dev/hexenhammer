package gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class EnemiesTab implements ITab {
	
	private final TabFolder mainFolder;
	
	public EnemiesTab(TabFolder mainFolder) {
		this.mainFolder = mainFolder;
	}

	public void createTab() {
		TabItem tbtmNewItem = new TabItem(mainFolder, SWT.NONE);
		tbtmNewItem.setText("Enemies");
	}

}

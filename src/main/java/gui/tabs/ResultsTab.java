package gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

public class ResultsTab implements ITab {

	private final TabFolder mainFolder;
	
	public ResultsTab(TabFolder mainFolder) {
		this.mainFolder = mainFolder;
	}

	public void createTab() {
		TabItem tbtmNewItem_1 = new TabItem(mainFolder, SWT.NONE);
		tbtmNewItem_1.setText("Results");
	}

}

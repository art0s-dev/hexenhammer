package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;

public final class Theme {
	public final static GridData GRID_CENTER = new GridData(SWT.CENTER, SWT.CENTER, false, false);
	public final static GridData GRID_FILL = new GridData(SWT.FILL, SWT.FILL, true, true);
	public final static GridData DEFAULT_GRID_DATA = new GridData(SWT.FILL, SWT.FILL, true, false);
	public final static int DEFAULT_VERTICAL_INDENT_COMBO = 15;
	public final static GridData FULL_WIDTH_GROUP = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
	
	public final static GridData getFullWidthGroupWithOptimalComboHeight() {
		GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		return groupGridData;
	}
}

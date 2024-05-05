package utils;

import static org.eclipse.swt.SWT.CENTER;
import static org.eclipse.swt.SWT.FILL;

import org.eclipse.swt.layout.GridData;

public final class Theme {
	public final static GridData GRID_CENTER = new GridData(CENTER, CENTER, false, false);
	public final static GridData GRID_FILL = new GridData(FILL, FILL, true, true);
	public final static GridData DEFAULT_GRID_DATA = new GridData(FILL, FILL, true, false);
	public final static int DEFAULT_VERTICAL_INDENT_COMBO = 15;
	public final static GridData FULL_WIDTH_GROUP = new GridData(FILL, FILL, true, false, 4, 1);
	
	public final static GridData getFullWidthGroupWithOptimalComboHeight() {
		GridData groupGridData = new GridData(FILL, FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		return groupGridData;
	}
}

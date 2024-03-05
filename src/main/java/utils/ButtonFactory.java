package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

/**
 * Use this class if you need
 * to paint like 20 Checkboxes and you don't want to manually
 * define all buttons
 */
public class ButtonFactory {
	private final Composite parent;
	
	public ButtonFactory(Composite parent) {
		this.parent = parent;
	}
	
	public Button createCheckBox(String label) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(label);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return button;
	}
}

package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;

public class ButtonFactory {
	private final Composite parent;
	private final Display display;
	
	public ButtonFactory(Composite parent) {
		this.parent = parent;
		this.display = parent.getShell().getDisplay();
	}
	
	public Button createCheckBox(String label) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(label);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return button;
	}
}

package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class ButtonFactory {
	public final Composite parent;
	
	public ButtonFactory(Composite parent) {
		this.parent = parent;
	}
	
	public Button createCheckBox(String label) {
		Button button = new Button(parent, SWT.CHECK);
		button.setText(label);
		return button;
	}
}

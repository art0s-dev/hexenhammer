package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

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
	
	public Button createCheckBox() {
		Button button = new Button(parent, SWT.CHECK);
		button.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return button;
	}
	
	public Text createTextInput(String label) {
		Label inputLabel = new Label(parent, SWT.NONE);
		inputLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		inputLabel.setText(label);
		Text text = new Text(parent, SWT.NONE);
		text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		return text;
	}
}

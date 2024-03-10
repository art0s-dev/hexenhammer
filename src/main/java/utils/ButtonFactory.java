package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/**
 * Use this class if you need to paint like 20 
 * Checkboxes and you don't want to manually
 * define all buttons
 */
public class ButtonFactory {
	private final Composite parent;
	private final GridData defaultLayout = new GridData(SWT.FILL, SWT.CENTER, true, false);
	
	public ButtonFactory(Composite parent) {
		this.parent = parent;
	}
	
	public Button createCheckBox() {
		Button button = new Button(parent, SWT.CHECK);
		button.setLayoutData(defaultLayout);
		return button;
	}
	
	public Text createTextInput(String label) {
		Label inputLabel = new Label(parent, SWT.NONE);
		inputLabel.setLayoutData(defaultLayout);
		inputLabel.setText(label);
		Text text = new Text(parent, SWT.NONE);
		text.setLayoutData(defaultLayout);
		return text;
	}
}

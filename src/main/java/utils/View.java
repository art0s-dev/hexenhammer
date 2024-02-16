package utils;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public class View {
	public static void placeholder(int quantity ,Composite attachTo) {
		for(int i = 1; i < quantity; i++) {
			new Label(attachTo, SWT.NONE);
		}
	}
}

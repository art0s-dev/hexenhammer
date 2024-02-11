package utils;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

public class Lambda {
	public static SelectionListener select(Runnable fn) {
		return new SelectionListener() {
			public void widgetSelected(SelectionEvent e) {
				fn.run();
			}

			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
		};
	}
	
	public static ModifyListener modify(Runnable fn) {
		return new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fn.run();
			}
		};
	}
}

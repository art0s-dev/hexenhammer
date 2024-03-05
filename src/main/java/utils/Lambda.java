package utils;

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

/**
 * This util consists of lambdas mainly 
 * for the gui development of swt to remove boilerplate code
 */
public class Lambda {
	
	/**
	 * Lambda for when a widget is Selected
	 */
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
	
	/**
	 * Lambda for when a widget is modified
	 */
	public static ModifyListener modify(Runnable fn) {
		return new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				fn.run();
			}
		};
	}
}

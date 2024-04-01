package end2end;

import org.eclipse.swt.widgets.Shell;

import statistics.StatisticsView;
import utils.I18n;

public class StatisticsViewTest extends SWTEnd2EndTestcase {
	
	/**
	 * Just the main method for running an SWT application 
	 */
	public static void main(String[] args) {
		Shell shell = before();
		test(shell);
		after(shell);
	}

	private static void test(Shell shell) {
		StatisticsView view = new StatisticsView(shell, new I18n());
		view.draw();
	}
}

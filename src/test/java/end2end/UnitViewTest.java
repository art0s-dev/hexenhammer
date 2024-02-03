package end2end;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import arch.IModel;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitView;

public class UnitViewTest {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new FillLayout());
		
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		mainTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		UnitView view = new UnitView(mainTab);
		view.draw();
		
		
		Unit spaceMarines = new Unit();
		spaceMarines.setName("My new favorite unit");
		spaceMarines.add(SpecialRuleUnit.LETHAL_HITS);
		
		Unit anotherUnit = new Unit();
		anotherUnit.setName("AnotherUnit");
		anotherUnit.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		
		ArrayList<IModel> unitList = new ArrayList<>();
		unitList.add(spaceMarines);
		unitList.add(anotherUnit);
		
		view.drawEditor(spaceMarines);
		view.drawList(unitList);

		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose ();
	}
}

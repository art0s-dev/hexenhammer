package end2end;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;

import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;

public class UnitViewTest {
	
	/**
	 * Just the main method for running an SWT application 
	 */
	public static void main(String[] args) {
		Display display = new Display();
		Shell shell = new Shell();
		shell.setLayout(new GridLayout(1, true));
		
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		mainTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		UnitView view = new UnitView(mainTab);
		
		Unit spaceMarines = new Unit();
		spaceMarines.setName("My new favorite unit");
		spaceMarines.add(SpecialRuleUnit.LETHAL_HITS);
		
		Unit anotherUnit = new Unit();
		anotherUnit.setName("AnotherUnit");
		anotherUnit.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		
		ArrayList<Unit> list = new ArrayList<>();
		list.add(spaceMarines);
		list.add(anotherUnit); 
		
		UnitList unitList = new UnitList(list);
		
		UnitRepository unitRepo = mock(UnitRepository.class);
		when(unitRepo.load()).thenReturn(unitList);
		
		UnitController unitController = new UnitController(view, unitRepo);
		unitController.loadModels();
		unitController.initView();
		
		unitController.injectListener();

		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) {
				display.sleep ();
			}
		}
		display.dispose ();
	}
}

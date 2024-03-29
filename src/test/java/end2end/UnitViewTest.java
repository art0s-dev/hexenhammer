package end2end;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Shell;

import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import utils.I18n;

public class UnitViewTest extends SWTEnd2EndTestcase {
	
	/**
	 * Just the main method for running an SWT application 
	 */
	public static void main(String[] args) {
		Shell shell = before();
		test(shell);
		after(shell);
	}

	private static void test(Shell shell) {
		I18n i18n = new I18n();
		i18n.setLanguage(I18n.german());
		UnitView view = new UnitView(shell, i18n);
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.setName("My new favorite unit");
		spaceMarines.add(SpecialRuleUnit.LETHAL_HITS);
		
		Unit anotherUnit = Unit.builder().build();
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
	}
}

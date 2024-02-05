package integration.unit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.Test;

import arch.Model;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import unit.UnitController;
import unit.UnitList;
import unit.UnitRepository;
import unit.UnitView;
import unittests.gui.SWTGuiTestCase;

class UnitControllerTest extends SWTGuiTestCase{

	@Test
	void testSwitchingUnitsInUnitsList() {
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		
		Unit unit1 = mock(Unit.class);
		String nameUnit1 = "My new favorite unit";
		when(unit1.getName()).thenReturn(nameUnit1);
		when(unit1.has(SpecialRuleUnit.LETHAL_HITS)).thenReturn(true);
		
		Unit unit2 = mock(Unit.class);
		String nameUnit2 = "AnotherUnit";
		when(unit2.getName()).thenReturn(nameUnit2);
		when(unit2.has(SpecialRuleUnit.ADD_ONE_TO_WOUND)).thenReturn(true);
		
		ArrayList<Unit> list = new ArrayList<>();
		list.add(unit1);
		list.add(unit2);
	
		UnitList unitList = mock(UnitList.class);
		when(unitList.getUnits()).thenReturn(list);
		
		UnitRepository unitRepo = mock(UnitRepository.class);
		when(unitRepo.load()).thenReturn(unitList);
		
		UnitView view = new UnitView(mainTab);
		
		UnitController controller = new UnitController(view,unitRepo);
		controller.loadModels();
		controller.initView();
		controller.injectListener();
		
		assertTrue(view.getNameInput().getText().equals(nameUnit1));
		view.getSelectionList().select(1);
		assertTrue(view.getNameInput().getText().equals(nameUnit2));
	}

}

package unit.gui;

import static org.mockito.Mockito.mock;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.junit.jupiter.api.Test;

import gui._UnitView;

class UnitViewTest {

	@Test
	void whenNoUnitListIsSet_thenNoUnitsAreAddedToTheRosterList() {
		Display display = mock(Display.class);
		TabFolder mainTab = mock(TabFolder.class);
		
		_UnitView view = new _UnitView(mainTab);
		view.draw();
		
	}

}

package unit;

import java.util.ArrayList;

import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;

import arch.IController;
import arch.Model;

public class UnitController implements IController {
	
	private final UnitView view;
	private final UnitRepository unitRepository;
	private UnitList unitList;
	
	public UnitController(UnitView view, UnitRepository unitRepository) {
		this.view = view;
		this.unitRepository = unitRepository;
	}

	@Override
	public void loadModels() {
		unitList = (UnitList) unitRepository.load();
	}
	
	@Override
	public void initView() {
		view.draw();
		view.drawList(unitList);
		view.drawEditor(unitList.getUnits().get(0));
	}

	@Override
	public void injectListener() {
		view.getSelectionList().addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = view.getSelectionList().getSelectionIndex();
				view.drawEditor(unitList.getUnits().get(index));
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				widgetSelected(e);
			}
			
		});
	}

	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
		
	}
	
	

	

}

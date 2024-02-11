package unit;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Button;

import arch.IController;
import arch.Model;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import utils.Lambda;

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
		view.getSelectionList().addSelectionListener(Lambda.select(()->{
			int index = view.getSelectionList().getSelectionIndex();
			view.drawEditor(unitList.getUnits().get(index));
		}));
		
		view.getCheckBoxRerollWound().addSelectionListener(Lambda.select(()->{
			toggle(view.getCheckBoxRerollWound(), SpecialRuleUnit.REROLL_WOUND_ROLL);
	    }));
	}

	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
		
	}
	
	private void toggle(Button btn, SpecialRuleUnit rule) {
		int index = view.getSelectionList().getSelectionIndex();
		Unit selectedUnit = unitList.getUnits().get(index);
		boolean selected = btn.getSelection();
		
		if(selected) {
			selectedUnit.add(rule);
		} else {
			selectedUnit.remove(rule);
		}
		
		unitList.getUnits().set(index, selectedUnit);
	}
	
	

	

}

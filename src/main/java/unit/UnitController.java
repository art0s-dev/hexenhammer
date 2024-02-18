package unit;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.swt.widgets.Button;

import arch.Controller;
import arch.Model;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import utils.Lambda;

public class UnitController implements Controller {
	
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
			view.drawEditor(unitList.getUnits().get(getIndex()));
		}));
		
		view.getNameInput().addModifyListener(Lambda.modify(()->{
			Unit selectedUnit = unitList.getUnits().get(getIndex());
			selectedUnit.setName(view.getNameInput().getText());
			unitList.getUnits().set(getIndex(), selectedUnit);
		}));
		
		view.getButtonAddUnit().addSelectionListener(Lambda.select(()->{
			view.getSelectionList().add("NEW TODO");
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
		}));
		
		HashMap<Button, SpecialRuleUnit> checkboxes = new HashMap<>();
		checkboxes.put(view.getCheckBoxAddOneToHit(), SpecialRuleUnit.ADD_ONE_TO_HIT);
		checkboxes.put(view.getCheckBoxLethalHits(), SpecialRuleUnit.LETHAL_HITS);
		checkboxes.put(view.getCheckBoxRerollOnesToHit(), SpecialRuleUnit.REROLL_ONES_TO_HIT);
		checkboxes.put(view.getCheckBoxRerollHitRoll(), SpecialRuleUnit.REROLL_HIT_ROLL);
		checkboxes.put(view.getCheckBoxAddOneToWound(), SpecialRuleUnit.ADD_ONE_TO_WOUND);
		checkboxes.put(view.getCheckBoxRerollOnesToWound(), SpecialRuleUnit.REROLL_ONES_TO_WOUND);
		checkboxes.put(view.getCheckBoxRerollWound(), SpecialRuleUnit.REROLL_WOUND_ROLL);
		checkboxes.put(view.getCheckBoxIgnoreCover(), SpecialRuleUnit.IGNORE_COVER);
		
		checkboxes.forEach((btn, rule) -> {
			btn.addSelectionListener(Lambda.select(() -> toggle(btn, rule)));
		});
		
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
	
	private int getIndex() {
		return view.getSelectionList().getSelectionIndex();
	}
	
	

	

}

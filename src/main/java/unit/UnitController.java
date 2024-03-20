package unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

import arch.Controller;
import arch.Model;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import lombok.val;
import utils.GuiFactory;
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
		_injectSelectionListListener();
		_injectNameInputListener();
		_injectAddListener();
		_injectRemoveListener();
		_injectNumberInputListeners();
		_injectComboListeners();
		_injectUnitTypeComboListener();
		_injectCheckboxesListeners();
	}
	
	
	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
	}

	private void _injectSelectionListListener() {
		view.getSelectionList().addSelectionListener(Lambda.select(()->{
			view.drawEditor(unitList.getUnits().get(_getIndex()));
		}));
	}
	
	private void _injectNameInputListener() {
		view.getInputName().addModifyListener(Lambda.modify(()->{
			Unit selectedUnit = unitList.getUnits().get(_getIndex());
			String newName = view.getInputName().getText();
			
			selectedUnit.setName(newName);
			unitList.getUnits().set(_getIndex(), selectedUnit);
			view.getSelectionList().setItem(_getIndex(), newName);
		}));
	}
	
	private void _injectAddListener() {
		view.getAddButton().addSelectionListener(Lambda.select(()->{
			String nameOfNewUnit = "New unit";
			view.getSelectionList().add(nameOfNewUnit);
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
			
			Unit unit = Unit.builder()
					.name(nameOfNewUnit)
					.build();
			
			unitList.getUnits().add(unit);
			view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		}));
	}
	
	private void _injectRemoveListener() {
		view.getDeleteButton().addSelectionListener(Lambda.select(()->{
			val thereAreNoUnits = unitList.getUnits().isEmpty();
			if(thereAreNoUnits) {
				return;
			}
			
			int currentUnit = _getIndex();
			int unitBeforeDeletedUnit = currentUnit - 1;
			
			unitList.getUnits().remove(currentUnit);
			view.drawList(unitList);
			view.getSelectionList().select(unitBeforeDeletedUnit);
		}));
	}
	
	private void _injectNumberInputListeners() {
		Unit unit = _getUnit();
		HashMap<Spinner, Consumer<Byte>> spinnerToUnitAttributes = new HashMap<>();
		spinnerToUnitAttributes.put(view.getInputMovement(), (value) -> unit.setMovement(value));
		spinnerToUnitAttributes.put(view.getInputToughness(), (value) -> unit.setToughness(value));
		spinnerToUnitAttributes.put(view.getInputHitPoints(), (value) -> unit.setHitPoints(value));
		spinnerToUnitAttributes.put(view.getInputLeadership(), (value) -> unit.setLeadership(value));
		spinnerToUnitAttributes.put(view.getInputObjectControl(), (value) -> unit.setObjectControl(value));
		
		spinnerToUnitAttributes.forEach((input, setter) -> input.addSelectionListener(Lambda.select(() -> {
			setter.accept((byte) input.getSelection());
		})));
	}
	
	private void _injectComboListeners() {
		HashMap<Combo, Consumer<Float>> combosToUnitAttributes = new HashMap<>();
		combosToUnitAttributes.put(view.getInputArmorSave(), (value) -> _getUnit().setArmorSave(value));
		combosToUnitAttributes.put(view.getInputFeelNoPain(), (value) -> _getUnit().setFeelNoPain(value));
		combosToUnitAttributes.put(view.getInputInvulnerableSave(), (value) -> _getUnit().setInvulnerableSave(value));
		
		combosToUnitAttributes.forEach((combo, setter) -> combo.addSelectionListener(Lambda.select(() -> {
			float probability = GuiFactory.mapComboSelectionToProbability(combo.getSelectionIndex());
			setter.accept(probability);
		})));
	}
	
	private void _injectUnitTypeComboListener() {
		view.getInputType().addSelectionListener(Lambda.select(() -> {
			int index = view.getInputType().getSelectionIndex();
			Unit.Type type = GuiFactory.mapUnitTypeComboSelectionToEnum(index);
			_getUnit().setType(type);
		}));
	}
	
	private void _injectCheckboxesListeners() {
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
			btn.addSelectionListener(Lambda.select(() -> _toggle(btn, rule)));
		});
	}
	
	private void _toggle(Button btn, SpecialRuleUnit rule) {
		Unit selectedUnit = _getUnit();
		boolean selected = btn.getSelection();
		
		if(selected) {
			selectedUnit.add(rule);
		} else {
			selectedUnit.remove(rule);
		}
	}
	
	private int _getIndex() {
		return view.getSelectionList().getSelectionIndex();
	}
	
	private Unit _getUnit() {
		return unitList.getUnits().get(_getIndex());
	}
}

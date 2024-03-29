package unit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

import arch.Controller;
import arch.Model;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import lombok.Getter;
import lombok.val;
import utils.GuiFactory;
import utils.Lambda;

public class UnitController implements Controller {
	
	private final UnitView view;
	private final UnitRepository repository;
	private UnitList list;
	
	public UnitController(UnitView view, UnitRepository unitRepository) {
		this.view = view;
		this.repository = unitRepository;
	}

	@Override
	public void loadModels() {
		list = (UnitList) repository.load();
	}
	
	@Override
	public void initView() {
		view.draw();
		view.drawList(list);
		
		if(list.getUnits().isEmpty()) {
			_freezeForm(true);
		}
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
			view.drawEditor(list.getUnits().get(_getIndex()));
		}));
	}
	
	private void _injectNameInputListener() {
		view.getInputName().addModifyListener(Lambda.modify(()->{
			Unit selectedUnit = list.getUnits().get(_getIndex());
			String newName = view.getInputName().getText();
			
			selectedUnit.setName(newName);
			list.getUnits().set(_getIndex(), selectedUnit);
			view.getSelectionList().setItem(_getIndex(), newName);
		}));
	}
	
	private void _injectAddListener() {
		view.getAddButton().addSelectionListener(Lambda.select(()->{
			if(list.getUnits().isEmpty()) {
				_freezeForm(false);
			}
			
			String nameOfNewUnit = view.getI18n().get("unit.UnitView.newUnit");
			view.getSelectionList().add(nameOfNewUnit);
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
			
			Unit unit = Unit.builder()
					.name(nameOfNewUnit)
					.build();
			
			list.getUnits().add(unit);
			view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		}));
	}
	
	private void _injectRemoveListener() {
		view.getDeleteButton().addSelectionListener(Lambda.select(()->{
			if(list.getUnits().isEmpty()) {
				return;
			}
			
			int currentUnit = _getIndex();
			int unitBeforeDeletedUnit = currentUnit - 1;
			
			list.getUnits().remove(currentUnit);
			view.drawList(list);
			view.getSelectionList().select(unitBeforeDeletedUnit);
			
			if(list.getUnits().isEmpty()) {
				_freezeForm(true);
			}
		}));
	}
	
	private void _injectNumberInputListeners() {
		HashMap<Spinner, Consumer<Byte>> spinnerToUnitAttributes = new HashMap<>();
		spinnerToUnitAttributes.put(view.getInputMovement(), (value) -> _getUnit().setMovement(value));
		spinnerToUnitAttributes.put(view.getInputToughness(), (value) -> _getUnit().setToughness(value));
		spinnerToUnitAttributes.put(view.getInputHitPoints(), (value) -> _getUnit().setHitPoints(value));
		spinnerToUnitAttributes.put(view.getInputLeadership(), (value) -> _getUnit().setLeadership(value));
		spinnerToUnitAttributes.put(view.getInputObjectControl(), (value) -> _getUnit().setObjectControl(value));
		
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
		return list.getUnits().get(_getIndex());
	}
	
	private void _freezeForm(boolean freeze) {
		view.getInputName().setEnabled(!freeze);
		view.getInputMovement().setEnabled(!freeze);
		view.getInputToughness().setEnabled(!freeze);
		view.getInputArmorSave().setEnabled(!freeze);
		view.getInputHitPoints().setEnabled(!freeze);
		view.getInputLeadership().setEnabled(!freeze);
		view.getInputObjectControl().setEnabled(!freeze);
		view.getInputInvulnerableSave().setEnabled(!freeze);
		view.getInputFeelNoPain().setEnabled(!freeze);
		view.getInputType().setEnabled(!freeze);
		view.getCheckBoxAddOneToHit().setEnabled(!freeze);
		view.getCheckBoxLethalHits().setEnabled(!freeze);
		view.getCheckBoxRerollOnesToHit().setEnabled(!freeze);
		view.getCheckBoxRerollHitRoll().setEnabled(!freeze);
		view.getCheckBoxAddOneToWound().setEnabled(!freeze);
		view.getCheckBoxRerollOnesToWound().setEnabled(!freeze);
		view.getCheckBoxRerollWound().setEnabled(!freeze);
		view.getCheckBoxIgnoreCover().setEnabled(!freeze);
	}
}

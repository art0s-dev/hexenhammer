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
import core.Weapon;
import lombok.Setter;
import utils.GuiFactory;
import utils.Lambda;
import weapon.WeaponController;

public class UnitController implements Controller {
	
	private final UnitView view;
	private final UnitRepository unitRepository;
	private UnitList unitList;
	
	@Setter private WeaponController weaponController;
	
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
		
		if(unitList.getUnits().isEmpty()) {
			_freezeForm(true);
		}
		
		freezeWeaponChamber();
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
		_injectAddEquipmentListener();
	}
	
	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
	}
	
	/**
	 * This Method is generally used to enable/freeze the weapon chamber
	 * this is called either through this class or through the weapon controller
	 * after updating the weapon
	 */
	public void freezeWeaponChamber() {
		boolean weaponControllerWasNotInjected = weaponController == null;
		if(weaponControllerWasNotInjected) {
			view.getAllWeaponsList().setEnabled(false);
			view.getWeaponQuantityInput().setEnabled(false);
			view.getEquipButton().setEnabled(false);
			view.getUnequipButton().setEnabled(false);
			view.getEquipmentList().setEnabled(false);
		} else {
			boolean weaponListIsNotEmpty = !weaponController.getWeaponList()
					.getWeapons().isEmpty();
			boolean unitListIsNotEmpty = !unitList.getUnits().isEmpty();
			boolean equimpemtListIsNotEmpty = view.getEquipmentList().getItemCount() > 0;
					
			view.getAllWeaponsList().setEnabled(weaponListIsNotEmpty && unitListIsNotEmpty);
			view.getWeaponQuantityInput().setEnabled(weaponListIsNotEmpty && unitListIsNotEmpty);
			view.getEquipButton().setEnabled(weaponListIsNotEmpty && unitListIsNotEmpty);
			view.getEquipmentList().setEnabled(weaponListIsNotEmpty && unitListIsNotEmpty);
			
			view.getUnequipButton().setEnabled(weaponListIsNotEmpty 
					&& unitListIsNotEmpty 
					&& equimpemtListIsNotEmpty);
		}
	}
	
	/**
	 * This method shall be used by the weapon controller to update
	 * the weaponList
	 */
	public void updateWeaponry() {
		boolean weaponControllerHasNotBeenInjected = weaponController == null;
		if(weaponControllerHasNotBeenInjected) {
			return;
		}
		
		view.getAllWeaponsList().removeAll();
		weaponController.getWeaponList().getWeapons()
		.forEach(weapon -> view.getAllWeaponsList().add(weapon.getName()));
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
			if(unitList.getUnits().isEmpty()) {
				_freezeForm(false);
			}
			
			String nameOfNewUnit = view.getI18n().get("unit.UnitView.newUnit");
			view.getSelectionList().add(nameOfNewUnit);
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
			
			Unit unit = Unit.builder()
					.name(nameOfNewUnit)
					.build();
			
			unitList.getUnits().add(unit);
			view.getSelectionList().notifyListeners(SWT.Selection, new Event());
			freezeWeaponChamber();
		}));
	}
	
	private void _injectRemoveListener() {
		view.getDeleteButton().addSelectionListener(Lambda.select(()->{
			if(unitList.getUnits().isEmpty()) {
				return;
			}
			
			int currentUnit = _getIndex();
			int unitBeforeDeletedUnit = currentUnit - 1;
			
			unitList.getUnits().remove(currentUnit);
			view.drawList(unitList);
			view.getSelectionList().select(unitBeforeDeletedUnit);
			
			if(unitList.getUnits().isEmpty()) {
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
	
	private void _injectAddEquipmentListener() {
		view.getEquipButton().addSelectionListener(Lambda.select(() -> {
			int selectionAllWeaponsList = view.getAllWeaponsList().getSelectionIndex();
			boolean userHasNotSelectedAnything = selectionAllWeaponsList == -1;
			if(userHasNotSelectedAnything) {
				return;
			}
			
			byte quantity = (byte) view.getWeaponQuantityInput().getSelection();
			if(quantity == 0) {
				return;
			}
			
			Unit unit = _getUnit();
			Weapon weapon = weaponController.getWeaponList()
					.getWeapons()
					.get(selectionAllWeaponsList);
			
			unit.equip(quantity, weapon);
			unitList.getUnits().set(_getIndex(), unit);
			view.getEquipmentList().add(quantity + "x " + weapon.getName());
			freezeWeaponChamber();
		}));
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
		freezeWeaponChamber();
	}


	
}

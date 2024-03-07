package unit;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

import arch.BaseView;
import arch.Model;
import arch.ModelList;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import lombok.Getter;
import lombok.val;
import utils.ButtonFactory;
import utils.I18n;

public final class UnitView extends BaseView {
	
	@Getter private Button checkBoxAddOneToHit;
	@Getter private Button checkBoxLethalHits;
	@Getter private Button checkBoxRerollOnesToHit;
	@Getter private Button checkBoxRerollHitRoll;
	@Getter private Button checkBoxAddOneToWound;
	@Getter private Button checkBoxRerollOnesToWound;
	@Getter private Button checkBoxRerollWound;
	@Getter private Button checkBoxIgnoreCover;
	
	public UnitView(Composite parent, I18n i18n) {
		super(parent, i18n);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeCheckBoxes();
		translate();
	}
	
	@Override
	public void translate() {
		super.translate();
		
		checkBoxAddOneToHit.setText(i18n.get("unit.UnitView.editor.checkBoxAddOneToHit"));
		checkBoxLethalHits.setText(i18n.get("unit.UnitView.editor.checkBoxLethalHits"));
		checkBoxRerollOnesToHit.setText(i18n.get("unit.UnitView.editor.checkRerollOnesToHit"));
		checkBoxRerollHitRoll.setText(i18n.get("unit.UnitView.editor.checkBoxRerollHitRoll"));
		
		checkBoxAddOneToWound.setText(i18n.get("unit.UnitView.editor.checkBoxAddOneToWoundRoll"));
		checkBoxRerollOnesToWound.setText(i18n.get("unit.UnitView.editor.checkBoxRerollOnesToWound"));
		checkBoxRerollWound.setText(i18n.get("unit.UnitView.editor.checkBoxRerollWoundRoll"));
		checkBoxIgnoreCover.setText(i18n.get("unit.UnitView.editor.checkBoxIgnoreCover"));
	}
	
	@Override
	public void drawList(ModelList modelList) { 
		UnitList unitList = (UnitList) modelList;
		selectionList.removeAll();
		
		val unitListIsEmpty = modelList == null;
		if(unitListIsEmpty) {
			return;
		}
		
		for(Unit unit: unitList.getUnits()) {
			val unitHasNoName = unit.getName() == null;
			selectionList.add(unitHasNoName ? "" : unit.getName());
		}
		
		selectionList.setSelection(0);
	}
	
	@Override 
	public void drawEditor(Model model) { 
		Unit unit = (Unit) model; 
		val formIsInitialized = model == null;
		
		if(formIsInitialized) {
			return;
		}
		
		val unitHasNoName = unit.getName() == null;
		inputName.setText(unitHasNoName ? "" : unit.getName());
		
		checkBoxAddOneToHit.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_HIT));
		checkBoxLethalHits.setSelection(unit.has(SpecialRuleUnit.LETHAL_HITS));
		checkBoxRerollOnesToHit.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_HIT));
		checkBoxRerollHitRoll.setSelection(unit.has(SpecialRuleUnit.REROLL_HIT_ROLL));
		
		checkBoxAddOneToWound.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_WOUND));
		checkBoxRerollOnesToWound.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND));
		checkBoxRerollWound.setSelection(unit.has(SpecialRuleUnit.REROLL_WOUND_ROLL));
		checkBoxIgnoreCover.setSelection(unit.has(SpecialRuleUnit.IGNORE_COVER));
	}
	
	private void _initializeCheckBoxes() {
		ButtonFactory buttonFactory = new ButtonFactory(entityEditorGroup);
		checkBoxAddOneToHit = buttonFactory.createCheckBox();
		checkBoxLethalHits = buttonFactory.createCheckBox();
		checkBoxRerollOnesToHit = buttonFactory.createCheckBox();
		checkBoxRerollHitRoll = buttonFactory.createCheckBox();
		
		checkBoxAddOneToWound = buttonFactory.createCheckBox();
		checkBoxRerollOnesToWound = buttonFactory.createCheckBox();
		checkBoxRerollWound = buttonFactory.createCheckBox();
		checkBoxIgnoreCover = buttonFactory.createCheckBox();
	}
	
	@Override
	protected String defineAddButtonToolTip() { 
		return "unit.UnitView.listView.addButtonToolTip";
	}
	
	@Override
	protected String defineDeleteButtonToolTip() {
		return "unit.UnitView.listView.deleteButtonToolTip";
	}
	
	@Override
	protected String defineListViewLabel() {
		return "unit.UnitView.listView.label";
	}
	
	@Override
	protected String defineEditorGroupName() {
		return "unit.UnitView.editor.groupName";
	}
}

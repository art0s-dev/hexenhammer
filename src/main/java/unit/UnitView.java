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

public final class UnitView extends BaseView {
	
	//Language Strings
	protected final static String ADD = "Klick here, to add an entry to the chosen tab list";
	protected final static String DELETE = "Klick here, to delete the selected entry from the chosen tab";
	protected final static String TAB_NAME = "Units";
	protected final static String LIST_VIEW = "Your units";
	protected final static String GROUP_NAME = "Special Rules";
	protected final static String UNIT_NAME = "Display name";
	private final static String ADD_ONE_TO_HIT = "Add one to hit";
	private final static String HAS_LETHAL_HITS = "Has Lethal hits";
	private final static String REROLL_ONES_TO_HIT = "Reroll ones to hit";
	private final static String REROLL_HIT_ROLL = "Reroll hit roll";
	private final static String ADD_ONE_TO_WOUND = "Add one to wound";
	private final static String REROLL_ONES_TO_WOUND = "Reroll ones to wound";
	private final static String REROLL_WOUND_ROLL = "Reroll wound roll";
	private final static String IGNORE_COVER = "Ignore cover";
	
	//"Puppet strings" for the controller
	@Getter private Button checkBoxAddOneToHit;
	@Getter private Button checkBoxLethalHits;
	@Getter private Button checkBoxRerollOnesToHit;
	@Getter private Button checkBoxRerollHitRoll;
	@Getter private Button checkBoxAddOneToWound;
	@Getter private Button checkBoxRerollOnesToWound;
	@Getter private Button checkBoxRerollWound;
	@Getter private Button checkBoxIgnoreCover;
	
	public UnitView(Composite parent) {
		super(parent);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeCheckBoxes();
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
		nameInput.setText(unitHasNoName ? "" : unit.getName());
		
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
		checkBoxAddOneToHit = buttonFactory.createCheckBox(ADD_ONE_TO_HIT);
		checkBoxLethalHits = buttonFactory.createCheckBox(HAS_LETHAL_HITS);
		checkBoxRerollOnesToHit = buttonFactory.createCheckBox(REROLL_ONES_TO_HIT);
		checkBoxRerollHitRoll = buttonFactory.createCheckBox(REROLL_HIT_ROLL);
		
		checkBoxAddOneToWound = buttonFactory.createCheckBox(ADD_ONE_TO_WOUND);
		checkBoxRerollOnesToWound = buttonFactory.createCheckBox(REROLL_ONES_TO_WOUND);
		checkBoxRerollWound = buttonFactory.createCheckBox(REROLL_WOUND_ROLL);
		checkBoxIgnoreCover = buttonFactory.createCheckBox(IGNORE_COVER);
	}
}

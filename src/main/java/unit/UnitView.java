package unit;

import java.util.function.Function;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Spinner;

import arch.BaseView;
import arch.Model;
import arch.ModelList;
import core.Unit;
import core.Unit.Equipment;
import core.Unit.SpecialRuleUnit;
import lombok.Getter;
import lombok.val;
import utils.GuiFactory;
import utils.I18n;
import utils.Theme;
	
public final class UnitView extends BaseView {

	//Profile
	@Getter private Button checkBoxUseAsEnemy;
	@Getter private Spinner inputMovement;
	@Getter private Spinner inputToughness;
	@Getter private Combo inputArmorSave;
	@Getter private Spinner inputHitPoints;
	@Getter private Spinner inputLeadership;
	@Getter private Spinner inputObjectControl;
	@Getter private Combo inputInvulnerableSave;
	@Getter private Combo inputFeelNoPain;
	@Getter private Combo inputType;
	
	//Special Rules
	@Getter private Button checkBoxAddOneToHit;
	@Getter private Button checkBoxLethalHits;
	@Getter private Button checkBoxRerollOnesToHit;
	@Getter private Button checkBoxRerollHitRoll;
	@Getter private Button checkBoxAddOneToWound;
	@Getter private Button checkBoxRerollOnesToWound;
	@Getter private Button checkBoxRerollWound;
	@Getter private Button checkBoxIgnoreCover;
	
	//Equipment
	@Getter private List equipmentList;
	@Getter private Button unequipButton;
	@Getter private Button equipButton;
	@Getter private Spinner weaponQuantityInput;
	@Getter private List allWeaponsList;
	
	//Labels
	private Group unitSpecialRules;
	private Label inputLabelMovement;
	private Label inputLabelToughness;
	private Label inputLabelWounds;
	private Label inputLabelLeadership;
	private Label inputLabelObjectControl;
	private Label inputLabelInvulnerableSave;
	private Label inputLabelFeelNoPain;
	private Label inputLabelArmorSave;
	private Label inputLabelType;
	private Group weaponEquipmentGroup;

	public UnitView(Composite parent, I18n i18n) {
		super(parent, i18n);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeProfileInputs();
		_initializeUnitTypeCombo();
		_initializeCheckBoxes();
		_initializeEquipmentForm();
		translate();
	}

	
	
	@Override
	public void translate() {
		super.translate();
		String prefix = "unit.UnitView.editor.";
		_translateInputFields(prefix);
		_translateCheckboxLabels(prefix);
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
		
		_drawInputValues(unit);
		_drawComboValues(unit);
		_drawCheckboxValues(unit);
		_drawEquipment(unit);
	}

	private void _drawEquipment(Unit unit) {
		getEquipmentList().removeAll();
		for(Equipment equipment: unit.getWeapons()) {
			getEquipmentList().add(equipment.quantity +"x " + equipment.weapon.getName());
		}
	}

	private void _translateCheckboxLabels(String prefix) {
		unitSpecialRules.setText(i18n.get(prefix + "unitSpecialRulesGroup"));
		checkBoxAddOneToHit.setText(i18n.get(prefix + "checkBoxAddOneToHit"));
		checkBoxLethalHits.setText(i18n.get(prefix + "checkBoxLethalHits"));
		checkBoxRerollOnesToHit.setText(i18n.get(prefix + "checkRerollOnesToHit"));
		checkBoxRerollHitRoll.setText(i18n.get(prefix + "checkBoxRerollHitRoll"));
		checkBoxAddOneToWound.setText(i18n.get(prefix + "checkBoxAddOneToWoundRoll"));
		checkBoxRerollOnesToWound.setText(i18n.get(prefix + "checkBoxRerollOnesToWound"));
		checkBoxRerollWound.setText(i18n.get(prefix + "checkBoxRerollWoundRoll"));
		checkBoxIgnoreCover.setText(i18n.get(prefix + "checkBoxIgnoreCover"));
		checkBoxUseAsEnemy.setText(i18n.get(prefix + "checkBoxUseAsEnemy"));
	}

	private void _translateInputFields(String prefix) {
		inputLabelMovement.setText(i18n.get(prefix + "labelMovement"));
		inputLabelToughness.setText(i18n.get(prefix + "labelToughness"));
		inputLabelWounds.setText(i18n.get(prefix + "labelWounds"));
		inputLabelLeadership.setText(i18n.get(prefix + "labelLeadership"));
		inputLabelObjectControl.setText(i18n.get(prefix + "labelObjectControl"));
		inputLabelInvulnerableSave.setText(i18n.get(prefix + "labelInvulnerableSave"));
		inputLabelFeelNoPain.setText(i18n.get(prefix + "labelFeelNoPain"));
		inputLabelArmorSave.setText(i18n.get(prefix + "labelArmorSave"));
		inputLabelType.setText(i18n.get(prefix + "labelUnitType"));
		GuiFactory.UNIT_TYPES.forEach((key, value) -> inputType.add(i18n.get(value)));
		inputType.select(0);
		equipButton.setText(i18n.get(prefix + "equipButton"));
		unequipButton.setText(i18n.get(prefix + "unequipButton"));
		weaponEquipmentGroup.setText(i18n.get(prefix + "weaponEquipmentGroup"));
	}

	private void _drawInputValues(Unit unit) {
		val unitHasNoName = unit.getName() == null;
		inputName.setText(unitHasNoName ? "" : unit.getName());
		inputMovement.setSelection(unit.getMovement());
		inputToughness.setSelection(unit.getToughness());
		inputHitPoints.setSelection(unit.getHitPoints());
		inputLeadership.setSelection(unit.getLeadership());
		inputObjectControl.setSelection(unit.getObjectControl());
	}
	
	private void _drawComboValues(Unit unit) {
		Function<Float, Integer> map = (probability) -> GuiFactory.mapProbabilityToComboSelection(probability);
		inputArmorSave.select(map.apply(unit.getArmorSave()));
		inputFeelNoPain.select(map.apply(unit.getFeelNoPain()));
		inputInvulnerableSave.select(map.apply(unit.getInvulnerableSave()));
		inputType.select(GuiFactory.mapTypeEnumToComboSelection(unit.getType()));
	}
	
	private void _drawCheckboxValues(Unit unit) {
		checkBoxAddOneToHit.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_HIT));
		checkBoxLethalHits.setSelection(unit.has(SpecialRuleUnit.LETHAL_HITS));
		checkBoxRerollOnesToHit.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_HIT));
		checkBoxRerollHitRoll.setSelection(unit.has(SpecialRuleUnit.REROLL_HIT_ROLL));
		checkBoxAddOneToWound.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_WOUND));
		checkBoxRerollOnesToWound.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND));
		checkBoxRerollWound.setSelection(unit.has(SpecialRuleUnit.REROLL_WOUND_ROLL));
		checkBoxIgnoreCover.setSelection(unit.has(SpecialRuleUnit.IGNORE_COVER));
		checkBoxUseAsEnemy.setSelection(unit.isUseAsEnemy());
	}
	
	private void _initializeProfileInputs() {
		GuiFactory factory = new GuiFactory(entityEditorGroup);
		checkBoxUseAsEnemy = factory.createCheckBox();
		factory.createLabel();
		factory.createLabel();
		factory.createLabel();
		
		inputLabelMovement = factory.createLabel();
		inputMovement = factory.createNumberInput();
		inputLabelToughness = factory.createLabel();
		inputToughness =  factory.createNumberInput();
		inputLabelArmorSave = factory.createLabel();
		inputArmorSave = factory.createProbabilityCombo();
		inputLabelWounds = factory.createLabel();
		inputHitPoints = factory.createNumberInput();
		inputLabelLeadership = factory.createLabel();
		inputLeadership = factory.createNumberInput();
		inputLabelObjectControl = factory.createLabel();
		inputObjectControl = factory.createNumberInput();
		inputLabelInvulnerableSave = factory.createLabel();
		inputInvulnerableSave = factory.createProbabilityCombo();
		inputLabelFeelNoPain = factory.createLabel();
		inputFeelNoPain = factory.createProbabilityCombo();
	}

	private void _initializeUnitTypeCombo() {
		inputLabelType = new Label(entityEditorGroup, SWT.NONE);
		inputType = new Combo(entityEditorGroup, SWT.NONE);
		GridData comboGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		comboGridData.verticalIndent = 15;
		inputType.setLayoutData(comboGridData);

		
		Label placeholder = new Label(entityEditorGroup, SWT.NONE);
		GridData placeholderGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		placeholderGridData.horizontalSpan = Theme.DEFAULT_VERTICAL_INDENT_COMBO;
		placeholder.setLayoutData(placeholderGridData);
	}
	
	private void _initializeCheckBoxes() {
		unitSpecialRules = new Group(entityEditorGroup, SWT.NONE);
		unitSpecialRules.setLayoutData(Theme.FULL_WIDTH_GROUP);
		unitSpecialRules.setLayout(new GridLayout(2, true));

		GuiFactory factory = new GuiFactory(unitSpecialRules);
		checkBoxAddOneToHit = factory.createCheckBox();
		checkBoxLethalHits = factory.createCheckBox();
		checkBoxRerollOnesToHit = factory.createCheckBox();
		checkBoxRerollHitRoll = factory.createCheckBox();
		
		checkBoxAddOneToWound = factory.createCheckBox();
		checkBoxRerollOnesToWound = factory.createCheckBox();
		checkBoxRerollWound = factory.createCheckBox();
		checkBoxIgnoreCover = factory.createCheckBox();
	}
	
	private void _initializeEquipmentForm() {
		weaponEquipmentGroup = new Group(entityEditorGroup, SWT.NONE);
		weaponEquipmentGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));
		weaponEquipmentGroup.setLayout(new GridLayout(3, true));
		allWeaponsList = new List(weaponEquipmentGroup, SWT.NONE);
		allWeaponsList.setLayoutData(Theme.GRID_FILL);
		
		Composite modificationComposite = new Composite(weaponEquipmentGroup, SWT.NONE);
		modificationComposite.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));
		modificationComposite.setLayout(new GridLayout(1, true));
		
		GuiFactory factory = new GuiFactory(modificationComposite);
		weaponQuantityInput = factory.createNumberInput();
		weaponQuantityInput.setLayoutData(Theme.GRID_CENTER);
		equipButton = factory.createTextButton();
		equipButton.setLayoutData(Theme.GRID_CENTER);
		unequipButton = factory.createTextButton();
		unequipButton.setLayoutData(Theme.GRID_CENTER);
		
		equipmentList = new List(weaponEquipmentGroup, SWT.NONE);
		equipmentList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
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

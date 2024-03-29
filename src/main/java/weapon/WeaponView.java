package weapon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

import arch.BaseView;
import arch.Model;
import arch.ModelList;
import core.Probability;
import core.Unit.Type;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.AntiType;
import core.Weapon.Range;
import core.Weapon.SpecialRuleWeapon;
import lombok.Getter;
import lombok.val;
import utils.GuiFactory;
import utils.I18n;
import utils.Theme;

public class WeaponView extends BaseView {

	//Inputs
	@Getter private Spinner inputAttacks;
	@Getter private Combo inputToHit;
	@Getter private Spinner inputStrenght;
	@Getter private Spinner inputArmorPenetration;
	@Getter private Spinner inputDamage;
	@Getter private Spinner inputSustainedHits;
	@Getter private Spinner inputMelter;
	@Getter private Button checkBoxTorrent;
	@Getter private Button checkBoxHeavyAndStationary;
	@Getter private Button weaponRangeShooting;
	@Getter private Button weaponRangeMeelee;
	@Getter private Button radioAttackInputFixedNumber;
	@Getter private Button radioAttackInputDice;
	@Getter private Spinner inputAttackInputFixedNumber;
	@Getter private Spinner inputAttackInputDice;
	@Getter private Combo inputAttackInputDiceChooser;
	@Getter private Button radioDamageInputFixedNumber;
	@Getter private Button radioDamageInputDice;
	@Getter private Spinner inputDamageInputFixedNumber;
	@Getter private Spinner inputDamageInputDice;
	@Getter private Combo inputDamageInputDiceChooser;
	@Getter private Combo antiTypeUnitTypeCombo;
	@Getter private Combo antiTypeProbabilityCombo;
	
	//Labels
	private Group weaponRange;
	private Label labelToHit;
	private Label labelStrength;
	private Label labelArmorPenetration;
	private Label labelSustainedHits;
	private Label labelMelter;
	private Group weaponSpecialRules;
	private Group attacksGroup;
	private Group damageGroup;
	private Group antiTypeGroup;
	
	public WeaponView(Composite parent, I18n i18n) {
		super(parent, i18n);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeWeaponRangeSwitch();
		_initializeAttackInputs();
		_initializeDamageInputs();
		_initializeInputFields();
		_initializeAntiType();
		_initializeCheckboxes();
		translate();
	}
	
	@Override
	public void translate() {
		super.translate();
		String prefix = "weapon.WeaponView.editor.";
		_translateRangeSwitcher(prefix);
		_translateInputFields(prefix);
		_translateSpecialRules(prefix);
		GuiFactory.UNIT_TYPES.forEach((key, value) -> antiTypeUnitTypeCombo.add(i18n.get(value)));
	}
	
	@Override
	public void drawList(ModelList modelList) {
		WeaponList weaponList = (WeaponList) modelList;
		selectionList.removeAll();
		
		val weaponListIsEmpty = modelList == null;
		if(weaponListIsEmpty) {
			return;
		}
		
		for(Weapon weapon: weaponList.getWeapons()) {
			val weaponHasNoName = weapon.getName() == null;
			selectionList.add(weaponHasNoName ? "" : weapon.getName());
		}
		
		selectionList.setSelection(0);
	}
	
	@Override
	public void drawEditor(Model model) {
		Weapon weapon = (Weapon) model;
		
		val formIsInitialized = model == null;
		if(formIsInitialized) {
			return;
		}
		
		_drawWeaponRange(weapon);
		_drawAttackInputValues(weapon);
		_drawDamageInputValues(weapon);
		_enableAttackInputFields(weapon);
		_enableDamageInputFields(weapon);
		_drawAntiType(weapon);
		_drawInputValues(weapon);
	}

	private void _drawWeaponRange(Weapon weapon) {
		boolean rangeNotSet = weapon.getRange() == null;
		if(rangeNotSet) {
			weaponRangeShooting.setSelection(true);
			weaponRangeMeelee.setSelection(false);
			return;
		} 
		
		Range range = weapon.getRange();
		weaponRangeShooting.setSelection(range.equals(Weapon.Range.SHOOTING));
		weaponRangeMeelee.setSelection(range.equals(Weapon.Range.MELEE));
	}

	private void _drawAttackInputValues(Weapon weapon) {
		boolean attackInputWasNotEntered = weapon.getAttackInput().isEmpty();
		if(attackInputWasNotEntered) {
			inputAttackInputFixedNumber.setSelection(0);
			return;
		} 
		
		UserNumberInput attackInput = weapon.getAttackInput().orElseThrow();
		inputAttackInputDice.setSelection(attackInput.getDiceQuantity());
		inputAttackInputDiceChooser.select(GuiFactory.mapDiceToComboSelection(attackInput.getDice()));
		inputAttackInputFixedNumber.setSelection(attackInput.getFixedNumber());
	}
	
	private void _drawDamageInputValues(Weapon weapon) {
		boolean damageInputWasNotEntered = weapon.getDamageInput().isEmpty();
		if(damageInputWasNotEntered) {
			inputDamageInputFixedNumber.setSelection(0);
			return;
		} 
		
		UserNumberInput damageInput = weapon.getDamageInput().orElseThrow();
		inputDamageInputFixedNumber.setSelection(damageInput.getFixedNumber());
		inputDamageInputDiceChooser.select(GuiFactory.mapDiceToComboSelection(damageInput.getDice()));
		inputDamageInputDice.setSelection(damageInput.getDiceQuantity());
	}
	
	private void _enableAttackInputFields(Weapon weapon) {
		boolean attackInputIsPresent = weapon.getAttackInput().isPresent();
		boolean useDice = false;
		if(attackInputIsPresent) {
			UserNumberInput attackInput = weapon.getAttackInput().orElseThrow();
			useDice = attackInput.isUseDice();
		}
		
		//Enable based on selection
		radioAttackInputFixedNumber.setSelection(!useDice);
		radioAttackInputDice.setSelection(useDice);
		inputAttackInputFixedNumber.setEnabled(!useDice);
		inputAttackInputDice.setEnabled(useDice);
		inputAttackInputDiceChooser.setEnabled(useDice);
	}
	
	private void _enableDamageInputFields(Weapon weapon) {
		boolean damageInputIsPresent = weapon.getDamageInput().isPresent();
		boolean useDice = false;
		if(damageInputIsPresent) {
			UserNumberInput damageInput = weapon.getDamageInput().orElseThrow();
			useDice = damageInput.isUseDice();
		}
		
		//Enable based on selection
		radioDamageInputFixedNumber.setSelection(!useDice);
		radioDamageInputDice.setSelection(useDice);
		inputDamageInputFixedNumber.setEnabled(!useDice);
		inputDamageInputDice.setEnabled(useDice);
		inputDamageInputDiceChooser.setEnabled(useDice);
	}
	
	private void _drawAntiType(Weapon weapon) {
		boolean noAntiTypeWasEntered = weapon.getAntiType().isEmpty();
		if(noAntiTypeWasEntered) {
			antiTypeProbabilityCombo.select(GuiFactory.mapProbabilityToComboSelection(Probability.NONE));
			antiTypeUnitTypeCombo.select(GuiFactory.mapTypeEnumToComboSelection(Type.INFANTRY));
			return;
		} 
		
		AntiType antiType = weapon.getAntiType().orElseThrow();
		antiTypeProbabilityCombo.select(GuiFactory.mapProbabilityToComboSelection(antiType.probability()));
		antiTypeUnitTypeCombo.select(GuiFactory.mapTypeEnumToComboSelection(antiType.type()));
	}
	
	private void _drawInputValues(Weapon weapon) {
		val weaponHasNoName = weapon.getName() == null;
		inputName.setText(weaponHasNoName ? "" : weapon.getName());
		inputStrenght.setSelection(weapon.getStrength());
		inputArmorPenetration.setSelection(weapon.getArmorPenetration());
		inputToHit.select(GuiFactory.mapProbabilityToComboSelection(weapon.getToHit()));
		inputMelter.setSelection(weapon.getMelter());
		inputSustainedHits.setSelection(weapon.getSustainedHits());
		checkBoxTorrent.setSelection(weapon.has(SpecialRuleWeapon.TORRENT));
		checkBoxHeavyAndStationary
		.setSelection(weapon.has(SpecialRuleWeapon.HEAVY_AND_UNIT_REMAINED_STATIONARY));
	}

	private void _initializeWeaponRangeSwitch() {
		weaponRange = new Group(entityEditorGroup, SWT.NONE);
		weaponRange.setLayout(new GridLayout(2, true));
		weaponRange.setLayoutData(Theme.FULL_WIDTH_GROUP);
		GuiFactory factory = new GuiFactory(weaponRange);
		weaponRangeShooting = factory.createRadioButton();
		weaponRangeMeelee = factory.createRadioButton();
	}
	
	private void _initializeAttackInputs() {
		GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		attacksGroup = new Group(entityEditorGroup, SWT.NONE);
		attacksGroup.setLayoutData(groupGridData);
		attacksGroup.setLayout(new GridLayout(5, true));
		GuiFactory factory = new GuiFactory(attacksGroup);
		radioAttackInputFixedNumber = factory.createRadioButton();
		inputAttackInputFixedNumber = factory.createNumberInput();
		radioAttackInputDice = factory.createRadioButton();
		inputAttackInputDice = factory.createNumberInput();
		inputAttackInputDiceChooser = factory.createDiceCombo();
	}
	
	private void _initializeDamageInputs() {
		GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		damageGroup = new Group(entityEditorGroup, SWT.NONE);
		damageGroup.setLayoutData(groupGridData);
		damageGroup.setLayout(new GridLayout(5, true));
		GuiFactory factory = new GuiFactory(damageGroup);
		radioDamageInputFixedNumber = factory.createRadioButton();
		inputDamageInputFixedNumber = factory.createNumberInput();
		radioDamageInputDice = factory.createRadioButton();
		inputDamageInputDice = factory.createNumberInput();
		inputDamageInputDiceChooser = factory.createDiceCombo();
	}
	
	private void _initializeInputFields() {
		GuiFactory factory = new GuiFactory(entityEditorGroup);
		labelToHit = factory.createLabel();
		inputToHit = factory.createProbabilityCombo();
		labelStrength = factory.createLabel();
		inputStrenght = factory.createNumberInput();
		labelArmorPenetration = factory.createLabel();
		inputArmorPenetration = factory.createNumberInput();
		labelSustainedHits = factory.createLabel();
		inputSustainedHits = factory.createNumberInput();
		labelMelter = factory.createLabel();
		inputMelter = factory.createNumberInput();
	}
	
	private void _initializeAntiType() {
		antiTypeGroup = new Group(entityEditorGroup, SWT.NONE);
		antiTypeGroup.setLayoutData(Theme.getFullWidthGroupWithOptimalComboHeight());
		
		antiTypeGroup.setLayout(new GridLayout(2, true));
		GuiFactory factory = new GuiFactory(antiTypeGroup);
		antiTypeUnitTypeCombo =  new Combo(antiTypeGroup, SWT.NONE);
		GridData comboGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		comboGridData.verticalIndent = 15;
		antiTypeUnitTypeCombo.setLayoutData(comboGridData);
		antiTypeProbabilityCombo = factory.createProbabilityCombo();
	}
	
	private void _initializeCheckboxes() {
		weaponSpecialRules = new Group(entityEditorGroup, SWT.NONE);
		weaponSpecialRules.setLayoutData(Theme.FULL_WIDTH_GROUP);
		weaponSpecialRules.setLayout(new GridLayout(2, true));

		GuiFactory factory = new GuiFactory(weaponSpecialRules);
		checkBoxTorrent = factory.createCheckBox();
		checkBoxHeavyAndStationary = factory.createCheckBox();
	}
	
	private void _translateSpecialRules(String prefix) {
		antiTypeGroup.setText(i18n.get(prefix + "labelAntiType"));
		checkBoxTorrent.setText(i18n.get(prefix + "checkBoxTorrent"));
		checkBoxHeavyAndStationary.setText(i18n.get(prefix + "checkBoxHeavyAndStationary"));
		weaponSpecialRules.setText(i18n.get(prefix + "weaponSpecialRules"));
	}

	private void _translateInputFields(String prefix) {
		labelToHit.setText(i18n.get(prefix + "labelToHit"));
		labelStrength.setText(i18n.get(prefix + "labelStrength"));
		labelArmorPenetration.setText(i18n.get(prefix + "labelArmorPenetration"));
		labelSustainedHits.setText(i18n.get(prefix + "labelSustainedHits"));
		labelMelter.setText(i18n.get(prefix + "labelMelter"));
		inputSustainedHits.setToolTipText(i18n.get(prefix + "zeroTurnsOffExplanation"));
		inputMelter.setToolTipText(i18n.get(prefix + "zeroTurnsOffExplanation"));
		
		attacksGroup.setText(i18n.get(prefix + "labelAttacks"));
		radioAttackInputFixedNumber.setText(i18n.get(prefix + "fixed"));
		radioAttackInputDice.setText(i18n.get(prefix + "dice"));
		damageGroup.setText(i18n.get(prefix + "labelDamage"));
		radioDamageInputFixedNumber.setText(i18n.get(prefix + "fixed"));
		radioDamageInputDice.setText(i18n.get(prefix + "dice"));
	}

	private void _translateRangeSwitcher(String prefix) {
		weaponRange.setText(i18n.get(prefix + "weaponRange"));
		weaponRangeMeelee.setText(i18n.get(prefix + "weaponRangeMelee"));
		weaponRangeShooting.setText(i18n.get(prefix + "weaponRangeShooting"));
	}
	
	@Override
	protected String defineAddButtonToolTip() { 
		return "weapon.WeaponView.listView.addButtonToolTip";
	}
	
	@Override
	protected String defineDeleteButtonToolTip() {
		return "weapon.WeaponView.listView.deleteButtonToolTip";
	}
	
	@Override
	protected String defineListViewLabel() {
		return "weapon.WeaponView.listView.label";
	}
	
	@Override
	protected String defineEditorGroupName() {
		return "weapon.WeaponView.editor.groupName";
	}

}

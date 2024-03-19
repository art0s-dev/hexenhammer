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
import core.Weapon;
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
	@Getter private Button radioAttackNumberInput;
	@Getter private Button radioAttackNumberDice;
	@Getter private Spinner inputAttackNumber;
	@Getter private Spinner inputAttackDiceNumber;
	@Getter private Combo inputAttackDiceChooser;
	@Getter private Button radioDamageInput;
	@Getter private Button radioDamageDice;
	@Getter private Spinner inputDamageNumber;
	@Getter private Spinner inputDamageDiceNumber;
	@Getter private Combo inputDamageDiceChooser;
	
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

	
	
	public WeaponView(Composite parent, I18n i18n) {
		super(parent, i18n);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeWeaponRangeSwitch();
		_initalizeAttackInputs();
		_initalizeDamageInputs();
		_initializeInputFields();
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
	}

	
	
	@Override
	public void drawList(ModelList modelList) {
		// TODO Auto-generated method stub 
	}
	
	@Override
	public void drawEditor(Model model) {
		Weapon weapon = (Weapon) model;
		
		val formIsInitialized = model == null;
		if(formIsInitialized) {
			return;
		}
		
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
	
	private void _initalizeAttackInputs() {
		GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		attacksGroup = new Group(entityEditorGroup, SWT.NONE);
		attacksGroup.setLayoutData(groupGridData);
		attacksGroup.setLayout(new GridLayout(5, true));
		GuiFactory factory = new GuiFactory(attacksGroup);
		radioAttackNumberInput = factory.createRadioButton();
		inputAttackNumber = factory.createNumberInput();
		radioAttackNumberDice = factory.createRadioButton();
		inputAttackDiceNumber = factory.createNumberInput();
		inputAttackDiceChooser = factory.createDiceCombo();
	}
	
	private void _initalizeDamageInputs() {
		GridData groupGridData = new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1);
		groupGridData.heightHint = 60;
		damageGroup = new Group(entityEditorGroup, SWT.NONE);
		damageGroup.setLayoutData(groupGridData);
		damageGroup.setLayout(new GridLayout(5, true));
		GuiFactory factory = new GuiFactory(damageGroup);
		radioDamageInput = factory.createRadioButton();
		inputDamageNumber = factory.createNumberInput();
		radioDamageDice = factory.createRadioButton();
		inputDamageDiceNumber = factory.createNumberInput();
		inputDamageDiceChooser = factory.createDiceCombo();
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
	
	private void _initializeCheckboxes() {
		weaponSpecialRules = new Group(entityEditorGroup, SWT.NONE);
		weaponSpecialRules.setLayoutData(Theme.FULL_WIDTH_GROUP);
		weaponSpecialRules.setLayout(new GridLayout(2, true));

		GuiFactory factory = new GuiFactory(weaponSpecialRules);
		checkBoxTorrent = factory.createCheckBox();
		checkBoxHeavyAndStationary = factory.createCheckBox();
	}
	
	private void _translateSpecialRules(String prefix) {
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
		radioAttackNumberInput.setText(i18n.get(prefix + "fixed"));
		radioAttackNumberDice.setText(i18n.get(prefix + "dice"));
		damageGroup.setText(i18n.get(prefix + "labelDamage"));
		radioDamageInput.setText(i18n.get(prefix + "fixed"));
		radioDamageDice.setText(i18n.get(prefix + "fixed"));
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

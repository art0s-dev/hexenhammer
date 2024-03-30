package weapon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.function.Consumer;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

import arch.Controller;
import arch.Model;
import core.Probability;
import core.Unit;
import core.Probability.Dice;
import core.Unit.SpecialRuleUnit;
import core.Unit.Type;
import core.Weapon;
import core.Weapon.AntiType;
import core.Weapon.Range;
import core.Weapon.SpecialRuleWeapon;
import utils.GuiFactory;
import utils.Lambda;

public class WeaponController implements Controller {

	private final WeaponView view;
	private final WeaponRepository repository;
	private WeaponList list;
	
	public WeaponController(WeaponView view, WeaponRepository repository) {
		this.view = view;
		this.repository = repository;
	}

	@Override
	public void loadModels() {
		list = (WeaponList) repository.load();
	}

	@Override
	public void initView() {
		view.draw();
		view.drawList(list);
		
		if(list.getWeapons().isEmpty()) {
			_freezeForm(true);
		}
	}

	@Override
	public void injectListener() {
		_injectAddListener();
		_injectDeleteListener();
		_injectSelectionListListener();
		_injectWeaponRangeListener();
		_injectAttackInputSwitchListener();
		_injectAttackInputListener();
		_injectDamageInputSwitchListener();
		_injectDamageInputListener();
		_injectToHitComboListener();
		_injectNumberInputListeners();
		_injectAntiTypeProbabilityListener();
		_injectAntiTypeUnitTypeListener();
		_injectCheckboxesListeners();
	}
	
	private void _injectAddListener() {
		view.getAddButton().addSelectionListener(Lambda.select(() -> {
			if(list.getWeapons().isEmpty()) {
				_freezeForm(false);
			}
			
			String nameOfWeapon = view.getI18n().get("weapon.WeaponView.newWeapon");
			view.getSelectionList().add(nameOfWeapon);
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
			
			Weapon weapon = Weapon.builder()
					.name(nameOfWeapon)
					.build();
			
			list.getWeapons().add(weapon);
			view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		}));
	}
	
	private void _injectDeleteListener() {
		view.getDeleteButton().addSelectionListener(Lambda.select(() -> {
			if(list.getWeapons().isEmpty()) {
				return;
			}
			
			int currentWeapon = _getIndex();
			int weaponBeforeDeletedWeapon = currentWeapon - 1;
			
			list.getWeapons().remove(currentWeapon);
			view.drawList(list);
			view.getSelectionList().select(weaponBeforeDeletedWeapon);
			
			if(list.getWeapons().isEmpty()) {
				_freezeForm(true);
			}
		}));
	}
	
	private void _injectSelectionListListener() {
		view.getSelectionList().addSelectionListener(Lambda.select(() -> {
			view.drawEditor(list.getWeapons().get(_getIndex()));
		}));
	}
	
	private void _injectWeaponRangeListener() {
		view.getWeaponRangeShooting().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			weapon.setRange(Range.SHOOTING);
			list.getWeapons().set(_getIndex(), weapon);
			view.getWeaponRangeMeelee().setSelection(false);
			view.getWeaponRangeShooting().setSelection(true);
		}));
		
		view.getWeaponRangeMeelee().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			weapon.setRange(Range.MELEE);
			list.getWeapons().set(_getIndex(), weapon);
			view.getWeaponRangeMeelee().setSelection(true);
			view.getWeaponRangeShooting().setSelection(false);
		}));
	}
	
	private void _injectAttackInputSwitchListener() {
		view.getRadioAttackInputDice().addSelectionListener(Lambda.select(() -> {
			view.getInputAttackInputDice().setEnabled(true);
			view.getInputAttackInputDiceChooser().setEnabled(true);
			view.getInputAttackInputFixedNumber().setEnabled(false);
			view.getRadioAttackInputFixedNumber().setSelection(false);
			Weapon weapon = _getWeapon();
			weapon.getAttackInput().orElseThrow().setUseDice(true);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getRadioAttackInputFixedNumber().addSelectionListener(Lambda.select(() -> {
			view.getInputAttackInputDice().setEnabled(false);
			view.getInputAttackInputDiceChooser().setEnabled(false);
			view.getInputAttackInputFixedNumber().setEnabled(true);
			view.getRadioAttackInputFixedNumber().setSelection(true);
			Weapon weapon = _getWeapon();
			weapon.getAttackInput().orElseThrow().setUseDice(false);
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectAttackInputListener() {
		view.getInputAttackInputFixedNumber().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			byte attacks = (byte) view.getInputAttackInputFixedNumber().getSelection();
			weapon.getAttackInput().orElseThrow().setFixedNumber(attacks);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getInputAttackInputDice().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			byte diceQuantity = (byte) view.getInputAttackInputDice().getSelection();
			weapon.getAttackInput().orElseThrow().setDiceQuantity(diceQuantity);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getInputAttackInputDiceChooser().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			int index = view.getInputAttackInputDiceChooser().getSelectionIndex();
			Dice dice = GuiFactory.mapComboSelectionToDice(index);
			weapon.getAttackInput().orElseThrow().setDice(dice);
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectDamageInputSwitchListener() {
		view.getRadioDamageInputDice().addSelectionListener(Lambda.select(() -> {
			view.getInputDamageInputDice().setEnabled(true);
			view.getInputDamageInputDiceChooser().setEnabled(true);
			view.getInputDamageInputFixedNumber().setEnabled(false);
			view.getRadioDamageInputFixedNumber().setSelection(false);
			Weapon weapon = _getWeapon();
			weapon.getDamageInput().orElseThrow().setUseDice(true);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getRadioDamageInputFixedNumber().addSelectionListener(Lambda.select(() -> {
			view.getInputDamageInputDice().setEnabled(false);
			view.getInputDamageInputDiceChooser().setEnabled(false);
			view.getInputDamageInputFixedNumber().setEnabled(true);
			view.getRadioDamageInputFixedNumber().setSelection(true);
			Weapon weapon = _getWeapon();
			weapon.getDamageInput().orElseThrow().setUseDice(false);
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectDamageInputListener() {
		view.getInputDamageInputFixedNumber().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			byte attacks = (byte) view.getInputDamageInputFixedNumber().getSelection();
			weapon.getDamageInput().orElseThrow().setFixedNumber(attacks);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getInputDamageInputDice().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			byte diceQuantity = (byte) view.getInputDamageInputDice().getSelection();
			weapon.getDamageInput().orElseThrow().setDiceQuantity(diceQuantity);
			list.getWeapons().set(_getIndex(), weapon);
		}));
		
		view.getInputDamageInputDiceChooser().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			int index = view.getInputDamageInputDiceChooser().getSelectionIndex();
			Dice dice = GuiFactory.mapComboSelectionToDice(index);
			weapon.getDamageInput().orElseThrow().setDice(dice);
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectToHitComboListener() {
		view.getInputToHit().addSelectionListener(Lambda.select(() -> {
			Weapon weapon = _getWeapon();
			int index = view.getInputToHit().getSelectionIndex();
			weapon.setToHit(GuiFactory.mapComboSelectionToProbability(index));
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectNumberInputListeners() {
		HashMap<Spinner, Consumer<Byte>> spinnerToWeaponAttributes = new HashMap<>();
		spinnerToWeaponAttributes.put(view.getInputStrenght(), (value) -> _getWeapon().setStrength(value));
		spinnerToWeaponAttributes.put(view.getInputArmorPenetration(), (value) -> _getWeapon().setArmorPenetration(value));
		spinnerToWeaponAttributes.put(view.getInputMelter(), (value) -> _getWeapon().setMelter(value));
		spinnerToWeaponAttributes.put(view.getInputSustainedHits(), (value) -> _getWeapon().setSustainedHits(value));
		
		spinnerToWeaponAttributes.forEach((input, setter) -> input.addSelectionListener(Lambda.select(() -> {
			setter.accept((byte) input.getSelection());
		})));
	}
	
	private void _injectAntiTypeUnitTypeListener() {
		view.getAntiTypeUnitTypeCombo().addSelectionListener(Lambda.select(() -> {
			int index = view.getAntiTypeUnitTypeCombo().getSelectionIndex();
			Type type = GuiFactory.mapUnitTypeComboSelectionToEnum(index);
			Weapon weapon = _getWeapon();
			
			float probability = Probability.NONE;
			if(weapon.getAntiType().isPresent()) {
				probability = weapon.getAntiType().orElseThrow().probability();
			}
			
			weapon.setAntiType(Optional.of(new AntiType(type, probability)));
			list.getWeapons().set(_getIndex(), weapon);	
		}));
	}

	private void _injectAntiTypeProbabilityListener() {
		view.getAntiTypeProbabilityCombo().addSelectionListener(Lambda.select(() -> {
			int index = view.getAntiTypeProbabilityCombo().getSelectionIndex();
			float selectedProbability = GuiFactory.mapComboSelectionToProbability(index);
			boolean probabilityIsNone = selectedProbability == Probability.NONE;
			Weapon weapon = _getWeapon();
			
			if(probabilityIsNone) {
				weapon.setAntiType(Optional.empty());
			} else {
				Type type = Type.INFANTRY;
				if(weapon.getAntiType().isPresent()) {
					type = weapon.getAntiType().orElseThrow().type();
				}
				
				weapon.setAntiType(Optional.of(new AntiType(type, selectedProbability)));
			}
			
			list.getWeapons().set(_getIndex(), weapon);
		}));
	}
	
	private void _injectCheckboxesListeners() {
		HashMap<Button, SpecialRuleWeapon> checkboxes = new HashMap<>();
		checkboxes.put(view.getCheckBoxTorrent(), SpecialRuleWeapon.TORRENT);
		checkboxes.put(view.getCheckBoxHeavyAndStationary(),
				SpecialRuleWeapon.HEAVY_AND_UNIT_REMAINED_STATIONARY);
		checkboxes.forEach((btn, rule) -> {
			btn.addSelectionListener(Lambda.select(() -> _toggle(btn, rule)));
		});
	}
	
	private void _toggle(Button btn, SpecialRuleWeapon rule) {
		Weapon weapon = _getWeapon();
		boolean selected = btn.getSelection();
		
		if(selected) {
			weapon.add(rule);
		} else {
			weapon.remove(rule);
		}
	}

	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
	}
	
	private int _getIndex() {
		return view.getSelectionList().getSelectionIndex();
	}
	
	private Weapon _getWeapon() {
		return list.getWeapons().get(_getIndex());
	}
	
	private void _freezeForm(boolean freeze) {
		view.getInputName().setEnabled(!freeze);
		view.getInputToHit().setEnabled(!freeze);
		view.getInputStrenght().setEnabled(!freeze);
		view.getInputArmorPenetration().setEnabled(!freeze);
		view.getInputSustainedHits().setEnabled(!freeze);
		view.getInputMelter().setEnabled(!freeze);
		view.getCheckBoxTorrent().setEnabled(!freeze);
		view.getCheckBoxHeavyAndStationary().setEnabled(!freeze);
		view.getWeaponRangeShooting().setEnabled(!freeze);
		view.getWeaponRangeMeelee().setEnabled(!freeze);
		view.getRadioAttackInputFixedNumber().setEnabled(!freeze);
		view.getRadioAttackInputDice().setEnabled(!freeze);
		view.getInputAttackInputFixedNumber().setEnabled(!freeze);
		view.getInputAttackInputDice().setEnabled(!freeze);
		view.getInputAttackInputDiceChooser().setEnabled(!freeze);
		view.getRadioDamageInputFixedNumber().setEnabled(!freeze);
		view.getRadioDamageInputDice().setEnabled(!freeze);
		view.getInputDamageInputFixedNumber().setEnabled(!freeze);
		view.getInputDamageInputDice().setEnabled(!freeze);
		view.getInputDamageInputDiceChooser().setEnabled(!freeze);
		view.getAntiTypeUnitTypeCombo().setEnabled(!freeze);
		view.getAntiTypeProbabilityCombo().setEnabled(!freeze);
	}

}

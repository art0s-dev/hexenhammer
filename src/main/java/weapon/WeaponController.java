package weapon;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Spinner;

import arch.Controller;
import arch.Model;
import core.Probability.Dice;
import core.Weapon;
import core.Weapon.Range;
import lombok.Getter;
import utils.GuiFactory;
import utils.I18n;
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
		_injectSelectionListListener();
		_injectWeaponRangeListener();
		_injectAttackInputSwitchListener();
		_injectAttackInputListener();
		
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

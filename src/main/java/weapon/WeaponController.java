package weapon;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;

import arch.Controller;
import arch.Model;
import core.Unit;
import core.Weapon;
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
		view.drawEditor(list.getWeapons().get(0));
		view.drawList(list);
	}

	@Override
	public void injectListener() {
		_injectAddListener();
	}

	private void _injectAddListener() {
		view.getAddButton().addSelectionListener(Lambda.select(() -> {
			String nameOfWeapon = "New Weapon";
			view.getSelectionList().add(nameOfWeapon);
			view.getSelectionList().setSelection(view.getSelectionList().getItemCount() - 1);
			
			Weapon weapon = Weapon.builder()
					.name(nameOfWeapon)
					.build();
			
			list.getWeapons().add(weapon);
			view.getSelectionList().notifyListeners(SWT.Selection, new Event());
		}));
	}
	
	

	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub

	}

}

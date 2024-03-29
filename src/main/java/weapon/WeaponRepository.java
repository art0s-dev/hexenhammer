package weapon;

import java.util.ArrayList;

import arch.ModelList;
import arch.Repository;

public class WeaponRepository implements Repository {

	@Override
	public ModelList load() {
		return new WeaponList(new ArrayList<>());
	}

}

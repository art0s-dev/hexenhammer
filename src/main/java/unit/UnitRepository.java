package unit;

import java.util.ArrayList;

import arch.ModelList;
import arch.Repository;

public class UnitRepository implements Repository {

	@Override
	public ModelList load() {
		return new UnitList(new ArrayList<>());
	}

}

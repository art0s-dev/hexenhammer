package unit;

import java.util.ArrayList;

import arch.ModelList;
import core.Unit;

public class UnitList extends ModelList {
	private final ArrayList<Unit> units;
	
	public UnitList(ArrayList<Unit> units) {
		this.units = units;
	}
	
	public ArrayList<Unit> getUnits(){
		return units;
	}
}

package unit;

import java.util.ArrayList;

import arch.ModelList;
import core.Unit;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class UnitList extends ModelList {
	@Getter final ArrayList<Unit> units;
}

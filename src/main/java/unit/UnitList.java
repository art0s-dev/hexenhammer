package unit;

import java.util.ArrayList;

import arch.ModelList;
import core.Unit;
import lombok.Data;


@Data
public class UnitList extends ModelList {
	final ArrayList<Unit> units;
}

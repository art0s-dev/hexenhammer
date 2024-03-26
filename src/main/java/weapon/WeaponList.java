package weapon;

import java.util.ArrayList;

import arch.ModelList;
import core.Weapon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class WeaponList extends ModelList {
	@Getter final ArrayList<Weapon> weapons;
}

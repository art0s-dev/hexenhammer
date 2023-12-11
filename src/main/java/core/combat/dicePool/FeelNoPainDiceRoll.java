package core.combat.dicePool;

import core.Enemy;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;
import lombok.val;

public class FeelNoPainDiceRoll extends DiceRoll {

	public FeelNoPainDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules combatRules) {
		super(unit, weapon, enemy, combatRules);
	}

	public DicePool roll(DicePool dicePool) {
		val total = dicePool.total();
		val damageAfterFeelNoPain = total - (total * enemy.getFeelNoPain()); 
		return new DicePool(total, damageAfterFeelNoPain);
	}

}

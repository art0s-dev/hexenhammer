package core.combat.dicePool;

import core.Enemy;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;
import lombok.val;

/**
 * Migitates damage after saving throws were made
 * and the damage multiplier has been applied
 * @see SavingThrowDiceRoll
 */
public class FeelNoPainDiceRoll extends DiceRoll {

	public FeelNoPainDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		super(unit, weapon, enemy, rules);
	}

	public DicePool roll(DicePool dicePool) {
		val total = dicePool.total();
		val damageAfterFeelNoPain = total - (total * enemy.getFeelNoPain()); 
		return new DicePool(total, damageAfterFeelNoPain);
	}

}

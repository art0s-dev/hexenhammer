package core.combat;

import core.CombatRules;
import core.Enemy;
import core.Unit;
import core.Weapon;
import lombok.val;

/**
 * Migitates damage after saving throws were made
 * and the damage multiplier has been applied
 * @see SavingThrowDiceRoll
 */
public final class FeelNoPainDiceRoll extends DiceRoll {

	public FeelNoPainDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		super(unit, weapon, enemy, rules);
	}

	public DicePool roll(DicePool dicePool) {
		val total = dicePool.getResult();
		val damageAfterFeelNoPain = total - (total * enemy.getFeelNoPain()); 
		return new DicePool(total, damageAfterFeelNoPain);
	}

}

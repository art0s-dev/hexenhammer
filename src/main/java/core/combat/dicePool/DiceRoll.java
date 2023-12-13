package core.combat.dicePool;

import core.Enemy;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;

/**
 * The Dice Roll is an abstraction which applies the combat rules 
 * of Warhammer 40k to a dice pool in order to achieve a result.
 */
public abstract class DiceRoll implements IDiceRoll {
	
	protected final Unit unit;
	protected final Weapon weapon;
	protected final Enemy enemy;
	protected final CombatRules combatRules;
	
	public DiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules combatRules) {
		this.unit = unit;
		this.weapon = weapon;
		this.enemy = enemy;
		this.combatRules = combatRules;
	}
	
	public abstract DicePool roll(DicePool dicePool);
}

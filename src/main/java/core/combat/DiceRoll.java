package core.combat;

import core.Enemy;
import core.Unit;
import core.Weapon;

/**
 * The Dice Roll is an abstraction which applies the combat rules 
 * of Warhammer 40k to a dice pool in order to achieve a result.
 */
public abstract class DiceRoll {
	
	protected final Unit unit;
	protected final Weapon weapon;
	protected final Enemy enemy;
	protected final CombatRules rules;
	
	public DiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		this.unit = unit;
		this.weapon = weapon;
		this.enemy = enemy;
		this.rules = rules;
	}
	
	/**
	 * Applies the implemented Functionality to the dice pool 
	 * in order to hit, wound etc...
	 */
	public abstract DicePool roll(DicePool dicePool);
}

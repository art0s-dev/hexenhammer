package core.combat;

import core.Unit;
import core.Weapon;
import lombok.AllArgsConstructor;

/**
 * The Dice Roll is an abstraction which applies the combat rules 
 * of Warhammer 40k to a dice pool in order to achieve a result.
 */

@AllArgsConstructor
public abstract class DiceRoll {
	
	protected final Unit unit;
	protected final Weapon weapon;
	protected final Unit enemy;
	protected final CombatRules rules;
	
	/**
	 * Applies the implemented Functionality to the dice pool 
	 * in order to hit, wound etc...
	 */
	public abstract DicePool roll(DicePool dicePool);
}

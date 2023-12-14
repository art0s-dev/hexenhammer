package core.combat;

import core.CombatRules;
import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

/**
 * Takes hits from a weapon and wounds the target
 */
public final class WoundDiceRoll extends DiceRoll {

	public WoundDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		super(unit, weapon, enemy, rules);
	}

	public DicePool roll(DicePool dicePool) {
		val total  = dicePool.result();
		val originalProbability = _compare(weapon.getStrength(), enemy.getToughness());
		val probabilityToWound = _modifyProbability(originalProbability);
		
		val wounds = total * probabilityToWound;
		val fails = rules.rerollOnesToWound() ? total * Probability.SIX_UP : total - wounds;
		val firstRoll = new DicePool(total, wounds);
		
		val reroll = rules.rerollWoundRoll() || rules.rerollOnesToWound();
		if(reroll) {
			val rerolledWounds = fails * probabilityToWound;
			return new DicePool(total, wounds + rerolledWounds);
		}
		
		return firstRoll;
	}
	
	/**
	 * Takes a probability like THREE_UP and upgrades or downgrades the result
	 * @implNote TWO_UP is highest, SIX_UP is lowest
	 */
	private float _modifyProbability(float originalProbability) {
		val modifyRoll = rules.addOneToWoundRoll() || rules.subtractOneFromWoundRoll();
		val bothModifiersApply = rules.addOneToWoundRoll() && rules.subtractOneFromWoundRoll();
		
		if(bothModifiersApply) {
			return originalProbability;
		}
		
		val modifier = rules.addOneToWoundRoll() ? '+' : '-' ;
		return modifyRoll ? Probability.modifyRoll(originalProbability, modifier)
				: originalProbability;
	}
	
	/**
	 * Determines the probability for a weapon to wound a target
	 */
	private float _compare(byte strength, byte toughness) {	
		if(strength >= toughness * 2) { 
			return Probability.TWO_UP; 
		}
		
		if(strength > toughness) { 
			return Probability.THREE_UP; 
		}
		
		if(strength == toughness){ 
			return Probability.FOUR_UP; 
		}
		
		if(strength * 2 <= toughness ) {
			return Probability.SIX_UP;
		}
		
		return Probability.FIVE_UP;
	}

}

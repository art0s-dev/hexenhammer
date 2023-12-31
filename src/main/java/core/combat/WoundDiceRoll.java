package core.combat;

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
		val total  = dicePool.getResult();
		val originalProbability = _compare(weapon.getStrength(), enemy.getToughness());
		val modifiedProbability = _modifyProbability(originalProbability);
		val probabilityToWound = rules.antiTypeWeapon() ? _antiType(modifiedProbability) : modifiedProbability;
		
		val wounds = total * probabilityToWound;
		val fails = rules.rerollOnesToWound() ? total * Probability.SIX_UP : total - wounds;
		val devastatingWounds = rules.devastatingWounds() ? total * Probability.SIX_UP : 0;
		val firstRoll = new WoundDicePool(total, wounds, devastatingWounds);
		
		val reroll = rules.rerollWoundRoll() || rules.rerollOnesToWound();
		if(reroll) {
			val rerolledWounds = fails * probabilityToWound;
			val allWounds = wounds + rerolledWounds;
			
			val rerolledDevastatingWounds = fails * Probability.SIX_UP;
			val allDevastatingWounds = rules.devastatingWounds() ? devastatingWounds + rerolledDevastatingWounds: 0;
			return new WoundDicePool(total, allWounds, allDevastatingWounds);
		}
		
		return firstRoll;
	} 
	
	/**
	 * Determines the wound probability of a specifice type of enemy
	 */
	private float _antiType(float originalProbability) {
		val noAntiType = !rules.antiTypeWeapon();
		if(noAntiType) {
			return originalProbability;
		}
		
		val antiType = weapon.getAntiType().orElseThrow();
		val enemyIsNotSameType = !antiType.type().equals(enemy.getType());
		if(enemyIsNotSameType) {
			return  originalProbability;
		}
		
		return antiType.probability();
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
		
		val modifier = rules.addOneToWoundRoll() 
				? Probability.Modifier.PLUS_ONE 
				: Probability.Modifier.MINUS_ONE;
		
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

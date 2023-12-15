package core.combat;

import core.CombatRules;
import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

/**
 * Takes the stats of a weapon and fires it at a given target
 */
public final class HitDiceRoll extends DiceRoll {

	public HitDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		super(unit, weapon, enemy, rules);
	}

	public DicePool roll(DicePool dicePool) {
		val total = dicePool.getTotal();
		val probabilityToHit = _modifyProbability(weapon.getToHit());
		val hits = total * probabilityToHit;
		val misses = rules.rerollOnesToHit() ? total * Probability.SIX_UP : total - hits;
		val lethalHits = rules.lethalHits() ? total * Probability.SIX_UP : 0f;
		
		val firstRoll = new HitDicePool(total, hits, lethalHits, 0f);
		
		val reroll = rules.rerollHitRoll() || rules.rerollOnesToHit();
		if(reroll) {
			val rerolledHits = misses * weapon.getToHit();
			val rerolledLethalHits = rules.lethalHits() ? (misses * Probability.SIX_UP) + lethalHits : 0f;
			return new HitDicePool(total, hits + rerolledHits, rerolledLethalHits, 0f);
		}
		
		return firstRoll; 
	}
	
	/**
	 * Takes a probability like THREE_UP and upgrades or downgrades the result
	 * @implNote TWO_UP is highest, SIX_UP is lowest
	 */
	private float _modifyProbability(float originalProbability) {
		val modifyRoll = rules.addOneToHitRoll() || rules.subtractOneFromHitRoll();
		val bothModifiersApply = rules.addOneToHitRoll() && rules.subtractOneFromHitRoll();
		
		if(bothModifiersApply) {
			return originalProbability;
		}
		
		val modifier = rules.addOneToHitRoll() ? '+' : '-' ;
		return modifyRoll ? Probability.modifyRoll(originalProbability, modifier)
				: originalProbability;
	}
	
	

}

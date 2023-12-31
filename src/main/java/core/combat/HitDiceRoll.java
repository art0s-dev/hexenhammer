package core.combat;

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
		
		val torrent = rules.torrent();
		if(torrent) {
			return new HitDicePool(total, total, 0f);
		}
		
		val probabilityToHit = _modifyProbability(weapon.getToHit());
		val hits = total * probabilityToHit;
		val misses = rules.rerollOnesToHit() ? total * Probability.SIX_UP : total - hits;
		
		val lethalHits = rules.lethalHits() ? total * Probability.SIX_UP : 0f;
		val sustainedHits = (total * Probability.SIX_UP) * weapon.getSustainedHits();
		
		val cumulatedHits = rules.sustainedHits() ? hits + sustainedHits : hits;
		val firstRoll = new HitDicePool(total, cumulatedHits, lethalHits);
		
		val reroll = rules.rerollHitRoll() || rules.rerollOnesToHit();
		if(reroll) {
			val rerolledHits = misses * weapon.getToHit();
			
			val rerolledLethalHits = rules.lethalHits() ? (misses * Probability.SIX_UP) + lethalHits : 0f;
			val rerolledSustainedHits = (misses * Probability.SIX_UP) * weapon.getSustainedHits();
			
			val cumulatedRerolledHits = hits + rerolledHits + sustainedHits + rerolledSustainedHits;
			return new HitDicePool(total, cumulatedRerolledHits, rerolledLethalHits);
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
		
		val modifier = rules.addOneToHitRoll() 
				? Probability.Modifier.PLUS_ONE 
				: Probability.Modifier.MINUS_ONE;
		
		return modifyRoll ? Probability.modifyRoll(originalProbability, modifier)
				: originalProbability;
	}
	
	

}

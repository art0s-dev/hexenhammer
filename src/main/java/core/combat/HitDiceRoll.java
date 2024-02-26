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
		
		val isTorrentWeapon = rules.torrent();
		if(isTorrentWeapon) {
			return new HitDicePool(total, total);
		}
		
		val firstRoll = _makeFirstRoll(total);		
		
		val dicePoolShallBeRerolled = rules.rerollHitRoll() || rules.rerollOnesToHit();
		if(dicePoolShallBeRerolled) {
			return _makeSecondRoll(total, firstRoll);
		}
		
		return firstRoll; 
	}

	/**
	 * Take the initial dice pool and roll the dice
	 */
	private HitDicePool _makeFirstRoll(float total) {
		val probabilityToHit = _modifyProbability(weapon.getToHit());
		val hits = total * probabilityToHit;
		val sixesInFirstRoll = total * Probability.SIX_UP;
		val misses = rules.rerollOnesToHit() ? sixesInFirstRoll : total - hits;
		
		val lethalHits = rules.lethalHits() ? sixesInFirstRoll : 0f;
		val sustainedHits = sixesInFirstRoll * weapon.getSustainedHits();
		
		val hitsAndSustainedHits = rules.sustainedHits() ? hits + sustainedHits : hits;
		
		return new HitDicePool(total, hitsAndSustainedHits)
				.withLethalHits(lethalHits)
				.withSustainedHits(sustainedHits)
				.withMisses(misses)
				.withHits(hits);
	}
	
	/**
	 * Do the reroll
	 */
	private HitDicePool _makeSecondRoll(float total, HitDicePool firstRoll) {
		val misses = firstRoll.getMisses();
		val rerolledHits = misses * weapon.getToHit();
		val sixesInSecondRoll = misses * Probability.SIX_UP;
		
		val rerolledLethalHits = rules.lethalHits() 
				? sixesInSecondRoll + firstRoll.getLethalHits() 
				: 0f;
		
		val rerolledSustainedHits = sixesInSecondRoll * weapon.getSustainedHits();
		
		val allHitsBothRerolledAndGenerated = 
				firstRoll.getHits() + 
				rerolledHits + 
				firstRoll.getSustainedHits() + 
				rerolledSustainedHits;
		
		return new HitDicePool(total, allHitsBothRerolledAndGenerated)
				.withLethalHits(rerolledLethalHits);
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

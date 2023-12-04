package core;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import core.Enemy.SpecialRuleProfile;
import core.Unit.SpecialRuleUnit;
import core.Weapon.AntiType;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;
import lombok.val;
import lombok.var;

/**
 * The Combat Class incoporates all fighting mechanics 
 * in the warhammer 40k rulebook. This class should only be called
 * via the abstractions of the unit class
 */
final class Combat {
	
	Combat(Unit unit, Enemy enemy){
		this.unit = unit;
		this.enemy = enemy;
	}
	
	/**
	 * Calculates the damage of all weapons a unit carries 
	 * from the same type against an enemy. The Incorporated
	 * Combat features are set through Unit, Weapon and enemy. 
	 * @see CombatFeatures 
	 */
	public float calculateDamage(Entry<Weapon, Byte> entry) {
		val weapon = entry.getKey();
		val quantity = entry.getValue();
		val rules = setRules(weapon);
		val attacks = weapon.getAttacks() * quantity;
		@var float chanceToHit = weapon.getToHit();
		//Modify hit rolls
		if(rules.addOneToHitRoll) {
			chanceToHit = Probability.modifyRoll(chanceToHit, '+');
		}
		if(rules.subtractOneFromHitRoll) {
			chanceToHit = Probability.modifyRoll(chanceToHit, '-');
		}
		
		//Calculate hits
		@var float hits = attacks  * chanceToHit;
		
		//Lethal hits bypass the wound roll
		//Subtract lethal hits from the hit pool 
		@var float lethalHits = 0f;
		@var float lethalHitsModifier = Probability.SIX_UP;
		
		if(rules.lethalHits) {
			if(rules.addOneToHitRoll) {
				lethalHitsModifier = Probability.FIVE_UP;
			}
			
			lethalHits = attacks * lethalHitsModifier;
			hits -= lethalHits;
		}
		
		//Do the rerolls for the hit roll
		@var boolean hitRollWasRerolled = false;
		val missedHits = attacks - hits;
		@var float hitRerollPool = 0;
		if(rules.rerollOnesToHit) {
			//Rolling ones has the same chance as rolling a Six
			hitRerollPool = missedHits * Probability.SIX_UP; 
			hitRollWasRerolled = true;
		}
		if(rules.rerollHitRoll) {
			hitRerollPool = missedHits;
			hitRollWasRerolled = true;
		}

		if(hitRollWasRerolled) {
			float rerolledLethalHits = 0;
			float rerolledHits = hitRerollPool * weapon.getToHit(); 
			
			if(rules.lethalHits) {
				rerolledLethalHits = hitRerollPool * lethalHitsModifier;
				rerolledHits -= rerolledLethalHits;
				lethalHits += rerolledLethalHits;
			} 

			hits += rerolledHits;
		}
		
		//Torrent weapons always hit
		if(rules.torrent) {
			hits = weapon.getAttacks() * quantity;
		}
		
		//calculate the wound roll   
		val strength = weapon.getStrength();
		val toughness = enemy.getToughness();
		@var float probabilityToWound = compare(strength, toughness);
		
		//Anti Type weapons
		if(rules.antiTypeWeapon) {
			val antiType = weapon.getAntiType().orElseThrow();
			val antiTypeIsProfileType = antiType.type() == enemy.getType();
			val antiTypeProbabilityIsHigherThanWoundRoll = antiType.probability() > probabilityToWound;
			
			if(antiTypeIsProfileType && antiTypeProbabilityIsHigherThanWoundRoll) {
				probabilityToWound = antiType.probability();
			}
		}
		
		//Modify wounds
		if(rules.addOneToWoundRoll) {
			probabilityToWound = Probability.modifyRoll(probabilityToWound, '+'); 
		}
		if(rules.subtractOneFromWoundRoll) {
			probabilityToWound = Probability.modifyRoll(probabilityToWound, '-');
		}
		
		//calculate wounds 
		@var float wounds = (hits * probabilityToWound) + lethalHits;
		
		//Reroll wounds
		if(rules.rerollOnesToWound) {
			wounds += ((hits - wounds) / 6) * probabilityToWound; 
		}
		if(rules.rerollWoundRoll) {
			wounds += (hits - wounds) * probabilityToWound; 
		}
		
		//determine Armour
		val armourSave = enemy.getArmorSave();
		@var byte modifiedArmourSave = (byte) (ARMOR_SAVES.get(armourSave) - weapon.getArmorPenetration());
		
		//Take cover!
		val weaponIsShooting = weapon.getPhase() == Phase.SHOOTING;
		val saveIsNotHighestPossible = modifiedArmourSave <= 5;
		if(weaponIsShooting && rules.enemyHasCover && !rules.ignoreCover && saveIsNotHighestPossible) {
			modifiedArmourSave++;
		}
		
		//Is there an invul save?
		@var float probabilityToSave = modifiedArmourSave / 6f;
		if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
			probabilityToSave = enemy.getInvulnerableSave();
		} 
		
		//Get Missed Saves and determine damage
		val missedSaves = wounds - (wounds * probabilityToSave);
		@var float damageMultiplier = weapon.getDamage() + weapon.getMelta();
		if(damageMultiplier > enemy.getHitPoints()) {
			damageMultiplier = enemy.getHitPoints();
		}
		
		//assert damage
		val damagePotential = missedSaves * damageMultiplier;
		val woundsAfterFeelNoPain = damagePotential * enemy.getFeelNoPain();
		return damagePotential - woundsAfterFeelNoPain;
}
	
	/**
	 * Determines the probability for a weapon to wound a target
	 */
	private static float compare(byte strength, byte toughness) {
		if(strength >= toughness * 2) { return Probability.TWO_UP; }
		if(strength > toughness) { return Probability.THREE_UP;}
		if(strength == toughness) { return Probability.FOUR_UP;}
		if(strength < toughness) { return Probability.FIVE_UP; }
		return Probability.SIX_UP;
	}
	
	/**
	 * These are the Features that can occure during a combat sequence
	 * It combines all Special rules from the unit, the enemy and the weapon entity
	 * in order to provide a good readable interface
	 */
	private record CombatRules(
		boolean addOneToHitRoll,
		boolean subtractOneFromHitRoll,
		boolean lethalHits,
		boolean rerollOnesToHit,
		boolean rerollHitRoll,
		boolean torrent,
		boolean addOneToWoundRoll,
		boolean subtractOneFromWoundRoll,
		boolean rerollOnesToWound,
		boolean rerollWoundRoll,
		boolean antiTypeWeapon,
		boolean enemyHasCover,
		boolean ignoreCover
	) {
	};
	
	/**
	 * Creates the feature flags for the battle sequence
	 * this method gets called before the damage of a new weapon is calculated
	 */
	private CombatRules setRules(Weapon weapon) {
		return new CombatRules(
			unit.has(SpecialRuleUnit.ADD_ONE_TO_HIT),
			enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL),
			unit.has(SpecialRuleUnit.LETHAL_HITS) || weapon.has(SpecialRuleWeapon.LETHAL_HITS),
			unit.has(SpecialRuleUnit.REROLL_ONES_TO_HIT),
			unit.has(SpecialRuleUnit.REROLL_HIT_ROLL),
			weapon.has(SpecialRuleWeapon.TORRENT),
			unit.has(SpecialRuleUnit.ADD_ONE_TO_WOUND),
			enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL),
			unit.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND),
			unit.has(SpecialRuleUnit.REROLL_WOUND_ROLL) || weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL),
			weapon.getAntiType().isPresent(),
			enemy.has(SpecialRuleProfile.HAS_COVER),
			unit.has(SpecialRuleUnit.IGNORE_COVER)
		);
	}
	
	/**
	 * The armor save characteristica of an enemy unit tells us how
	 * tanky a unit is. This implementation subtracts the armor penetration value
	 * from the weapon from the given values, until there is no armor save left
	 */
	private static final HashMap<Float,Byte> ARMOR_SAVES = new HashMap<>();
	static {
		ARMOR_SAVES.put(Probability.SIX_UP, (byte)1);
		ARMOR_SAVES.put(Probability.FIVE_UP, (byte)2);
		ARMOR_SAVES.put(Probability.FOUR_UP, (byte)3);
		ARMOR_SAVES.put(Probability.THREE_UP, (byte)4);
		ARMOR_SAVES.put(Probability.TWO_UP, (byte)5);
		Collections.unmodifiableMap(ARMOR_SAVES);
	}
	
	private final Unit unit;
	private final Enemy enemy;
	
}

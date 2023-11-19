package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import core.Profile.SpecialRuleProfile;
import core.Weapon.AntiType;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;

/**
 * This is the cornerstone of the software
 * Each unit represents a bunch of weapons that will be rolled
 * against another unit. In Order to calculate the damage Outcome of a Unit
 * you will have to:
 * - create some weapons, a unit and a target profile
 * - equip() the weapons and maybe add() some special rules
 * - attack() the enemy profile  
 */
public class Unit {
	
	/**
	 * With this method we add, delete and edit the weapons of a unit. 
	 * @param Weapon - The weapon we want to edit
	 * @param quantity - The quantity the weapns shall recieve   
	 * @implNote Quantity negative or zero deletes the weapon 
	 */
	public void equip(int quantity, Weapon weapon) {
		
		if(!weapons.containsKey(weapon)) {
			weapons.put(weapon, quantity);
			return;
		}
		
		var quantityWillBeNegative =  weapons.get(weapon) + quantity <= 0;
		if(quantityWillBeNegative || quantity == 0) {
			weapons.remove(weapon);
			return;
		}
		
		int oldQuantity = weapons.get(weapon);
		weapons.put(weapon, oldQuantity + quantity );
	}
	
	/**
	 * The SpecialRuleUnit builds an API for editing special cases in our unit
	 * these rules apply to the battle sequence in global. There is also
	 * a SpecialRuleWeapon which will be applied before the global rule if it is better 
	 */
	public enum SpecialRuleUnit{
		REROLL_ONES_TO_HIT,
		REROLL_HIT_ROLL,
		REROLL_ONES_TO_WOUND,
		REROLL_WOUND_ROLL,
		ADD_ONE_TO_HIT,
		ADD_ONE_TO_WOUND,
		IGNORE_COVER,
		LETHAL_HITS
	}
	public void add(SpecialRuleUnit specialRule) {
		this.specialRules.add(specialRule);
	}
	public void remove(SpecialRuleUnit specialRule) {
		this.specialRules.remove(specialRule);
	}

	/**
	 * The attack method is an implementation of the combat sequence of the 
	 * warhammer 40k rulebook it should cover the shooting phase,  
	 * aswell as the fight phase and all the generic special rules 
	 * 
	 * @see Design docs - core
	 * @apiNote Uses a Filter for which weapons shall be used in the calculation
	 * the default is set to both but with the setPhase Method it can be changed
	 * @param Profile - The Profile the Unit shall attack  
	 * @return Int - The damage done to the profile unit  
	 */
	public double attack(Profile enemy) {
		
		//Filters all Weapons that shall be used for a specific phase 
		Map<Weapon, Integer> filteredWeapons = weapons;
		if(phase != Phase.BOTH) {
			filteredWeapons = weapons.entrySet().parallelStream()
					.filter(entry -> entry.getKey().getPhase() == phase)
					.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
		}
		
		//Iterates through all weapons and calculates damage for each weapon  
		double damage = 0;
		for (Entry<Weapon, Integer> set : filteredWeapons.entrySet()) {
			Weapon weapon = set.getKey();
			
			//calculate the hits
			int quantity = set.getValue();
			double attacks = weapon.getAttacks() * quantity;
			double chanceToHit = weapon.getToHit();
			
			//Modify hit rolls
			if(this.has(SpecialRuleUnit.ADD_ONE_TO_HIT)) {
				chanceToHit = Probability.modifyRoll(chanceToHit, '+');
			}
			if(enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL)) {
				chanceToHit = Probability.modifyRoll(chanceToHit, '-');
			}
			
			//Do the rerolls 
			double hits = attacks  * chanceToHit;
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT)) {
				hits += ((attacks - hits) / 6) * weapon.getToHit(); 
			}
			if(this.has(SpecialRuleUnit.REROLL_HIT_ROLL)) {
				hits += (attacks - hits) * weapon.getToHit(); 
			}
			if(weapon.has(SpecialRuleWeapon.TORRENT)) {
				hits = weapon.getAttacks() * quantity;
			}
			
			
			//calculate the wound roll  
			int strength = weapon.getStrength();
			int toughness = enemy.getToughness();
			double probabilityToWound = Probability.SIX_UP;
			if(strength >= toughness * 2) { probabilityToWound = Probability.TWO_UP; }
			if(strength > toughness) { probabilityToWound = Probability.THREE_UP;}
			if(strength == toughness) { probabilityToWound = Probability.FOUR_UP;}
			if(strength < toughness) { probabilityToWound = Probability.FIVE_UP; }
			
			//Anti Type weapons
			boolean antiTypeIsSet = weapon.getAntiType().isPresent();
			if(antiTypeIsSet) {
				AntiType antiType = weapon.getAntiType().orElseThrow();
				boolean antiTypeIsProfileType = antiType.type() == enemy.getType();
				boolean antiTypeProbabilityIsHigherThanWoundRoll = antiType.probability() > probabilityToWound;
				
				if(antiTypeIsProfileType && antiTypeProbabilityIsHigherThanWoundRoll) {
					probabilityToWound = antiType.probability();
				}
			}
			
			//Modify wounds
			if(this.has(SpecialRuleUnit.ADD_ONE_TO_WOUND)) {
				probabilityToWound = Probability.modifyRoll(probabilityToWound, '+'); 
			}
			if(enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL)) {
				probabilityToWound = Probability.modifyRoll(probabilityToWound, '-');
			}
			
		
			//Subtract lethal hits from the hit pool
			double lethalHits = 0.00;
			boolean lethalHitsAreActive = this.has(SpecialRuleUnit.LETHAL_HITS) 
					|| weapon.has(SpecialRuleWeapon.LETHAL_HITS);
			if(lethalHitsAreActive) {
				double lethalHitsModifier = 1;
				if(this.has(SpecialRuleUnit.ADD_ONE_TO_HIT)) {
					lethalHitsModifier = 2;
				}
				
				lethalHits = (hits / 6) * lethalHitsModifier;
				hits -= lethalHits;
			}
			
			//calculate wounds 
			double wounds = hits * probabilityToWound;
			
			//Reroll wounds
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND)) {
				wounds += ((hits - wounds) / 6) * probabilityToWound; 
			}
			boolean rerollWoundRoll = this.has(SpecialRuleUnit.REROLL_WOUND_ROLL) 
					|| weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL);
			if(rerollWoundRoll) {
				wounds += (hits - wounds) * probabilityToWound; 
			}
			
			//Add lethal hits right back to the final wound pool
			if(lethalHitsAreActive) {
				wounds += lethalHits;
			}
			
			//determine Armour
			double armourSave = enemy.getArmorSave();
			HashMap<Double,Integer> symbolicArmourSave = new HashMap<>();
			symbolicArmourSave.put(Probability.SIX_UP, 1);
			symbolicArmourSave.put(Probability.FIVE_UP, 2);
			symbolicArmourSave.put(Probability.FOUR_UP, 3);
			symbolicArmourSave.put(Probability.THREE_UP, 4);
			symbolicArmourSave.put(Probability.TWO_UP, 5);
			int modifiedArmourSave = symbolicArmourSave.get(armourSave) - weapon.getArmorPenetration();
			
			//Take cover!
			boolean weaponIsShooting = weapon.getPhase() == Phase.SHOOTING;
			boolean enemyHasCover = enemy.has(SpecialRuleProfile.HAS_COVER);
			boolean unitDoesNotIgnoreCover = !this.has(SpecialRuleUnit.IGNORE_COVER);
			boolean saveIsNotHighestPossible = modifiedArmourSave <= 5;
			if(weaponIsShooting && enemyHasCover && unitDoesNotIgnoreCover && saveIsNotHighestPossible) {
				modifiedArmourSave++;
			}
			
			//Is there an invul save?
			double probabilityToSave = modifiedArmourSave / 6.00;
			if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
				probabilityToSave = enemy.getInvulnerableSave();
			} 
			
			//Get Missed Saves and determine damage
			double missedSaves = wounds - (wounds * probabilityToSave);
			double damageMultiplier = weapon.getDamage() + weapon.getMelta();
			if(damageMultiplier > enemy.getHitPoints()) {
				damageMultiplier = enemy.getHitPoints();
			}
			
			//assert damage
			double damagePotential = missedSaves * damageMultiplier;
			double woundsAfterFeelNoPain = damagePotential * enemy.getFeelNoPain();
			double damageDone = damagePotential - woundsAfterFeelNoPain;
			damage += damageDone;
		}
		
		return damage;
	} 
	
	/**
	 * Sets the Filter which weapons shall be used for the attack sequence
	 * @implNote The default is set to both phases
	 * @param phase
	 */
	public void setPhase(Phase phase) {
		this.phase = phase;
	}
	private Phase phase = Phase.BOTH;
	
	/**
	 * This is the register of weapons a unit has.
	 * This register can be edited via the equip method. 
	 * It contains the signature of the object as a key 
	 * and a quantity as the value. 
	 */
	private HashMap<Weapon, Integer> weapons = new HashMap<>();
	
	/**
	 * The Set of special rules each unit has.
	 * @see SpecialRuleUnit
	 */
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<>();
	
	private boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}

}

package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import core.Profile.SpecialRuleProfile;
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
		ADD_ONE_TO_HIT
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
	 * @see https://www.warhammer-community.com/2023/06/02/download-the-new-warhammer-40000-rules-for-free-right-here/ 
	 * @param Profile - The Profile the Unit shall attack  
	 * @return Int - The damage done to the profile unit 
	 */
	public int attack(Profile enemy) {
		int damage = 0;
		
		//Iterates through all weapons and calculates damage for each weapon
		for (Entry<Weapon, Integer> set : weapons.entrySet()) {
			Weapon weapon = set.getKey();
			
			//calculate the hits
			int quantity = set.getValue();
			double attacks = weapon.getAttacks() * quantity;
			double chanceToHit = weapon.getToHit();
			if(this.has(SpecialRuleUnit.ADD_ONE_TO_HIT)) {
				chanceToHit = Probability.modifyRoll(chanceToHit, '+');
			}
			if(enemy.has(SpecialRuleProfile.SUBSTRACT_ONE_FROM_HIT_ROLL)) {
				chanceToHit = Probability.modifyRoll(chanceToHit, '-');
			}
			
			double hits = (double) attacks  * chanceToHit;
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
			double wounds = hits * probabilityToWound;
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND)) {
				wounds += ((hits - wounds) / 6) * probabilityToWound; 
			}
			boolean rerollWoundRoll = this.has(SpecialRuleUnit.REROLL_WOUND_ROLL) 
					|| weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL);
			if(rerollWoundRoll) {
				wounds += (hits - wounds) * probabilityToWound; 
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
			double probabilityToSave = modifiedArmourSave / 6.00;
			if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
				probabilityToSave = enemy.getInvulnerableSave();
			} 
			
			//Calculate damage 
			//We don't assign assign 0,45 missed saves to a model in multiwound combat. 
			int missedSaves = (int) Math.floor(wounds - (wounds * probabilityToSave));
			double damageMultiplier = weapon.getDamage();
			if(weapon.getDamage() > enemy.getHitPoints()) {
				damageMultiplier = enemy.getHitPoints();
			}
			
			double damagePotential = missedSaves * damageMultiplier;
			double woundsAfterFeelNoPain = damagePotential * enemy.getFeelNoPain();
			damage += damagePotential - woundsAfterFeelNoPain;
		}
		
		return damage;
	}
	
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

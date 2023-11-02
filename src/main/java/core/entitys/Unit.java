package core.entitys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

/**
 * This is the cornerstone of the software
 * Each unit represents a bunch of weapons that will be rolled
 * against another unit   
 */
public class Unit {
	
	/**
	 * With this method we add, delete and edit the weapons of a unit.
	 * @param Weapon - The weapon we want to edit
	 * @param quantity - The quantity the weapns shall recieve  
	 * @apiNote Quantity negative or zero deletes the weapon
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
	 * The SpecialRuleUnit builds an api for editing special cases in our unit
	 * these rules apply to the battle sequence in global. There is also
	 * a SpecialRuleWeapon which will be applied before the global rule if it is better 
	 */
	public enum SpecialRuleUnit{
		REROLL_ONES_TO_HIT
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
		for (Entry<Weapon, Integer> set : weapons.entrySet()) {
			Weapon weapon = set.getKey();
			
			//calculate the hits
			double attacks = weapon.getAttacks() * set.getValue();
			double hits = (double) attacks  * weapon.getToHit();
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT)) {
				hits += ((attacks - hits) / 6) * weapon.getToHit(); 
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
			
			//determine armour
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
			
			double woundsAfterFeelNoPain = (missedSaves * damageMultiplier) * enemy.getFeelNoPain();
			damage += (missedSaves * damageMultiplier) - woundsAfterFeelNoPain;
		}
		
		return damage;
	}
	
	private HashMap<Weapon, Integer> weapons = new HashMap<>();
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<>();
	private boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}

}

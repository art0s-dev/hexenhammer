package core.entitys;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import core.entitys.Weapon.SpecialRuleWeapon;

/**
 * This is the cornerstone of the software
 * Each unit represents a bunch of weapons that will be rolled
 * against another unit 
 */
public class Unit {
	
	private int index = 0;
	public record Equipment(Weapon weapon, int quantity) {}; 
	private HashMap<Integer, Equipment> weapons = 
			new HashMap<Integer, Equipment>();
	
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<SpecialRuleUnit>();
	public enum SpecialRuleUnit{
		REROLL_ONES_TO_HIT
	}
	
	/**
	 * The attack method is an implementation of the combat sequence of the 
	 * warhammer 40k rulebookit should cover the shooting phase, 
	 * aswell as the fight phase and all the generic special rules
	 * https://www.warhammer-community.com/2023/06/02/download-the-new-warhammer-40000-rules-for-free-right-here/
	 */
	public double attack(Profile enemy) {
		double damage = 0;
		
		for (Map.Entry<Integer, Equipment> equipment : weapons.entrySet()) {
			var weapon = equipment.getValue().weapon();
			var quantity = equipment.getValue().quantity();
			
			//Calculate the hits 
			var numberOfShots = weapon.getAttacks() * quantity;
			var hits = (double) numberOfShots  * weapon.getToHit();
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT)) {
				hits += ((numberOfShots - hits) / 6) * weapon.getToHit(); 
			}
			
			//Calculate the wounds   
			int strength = weapon.getStrength();
			int toughness = enemy.getToughness();
			double probabilityToWound = Probability.SIX_UP;
			if(strength >= toughness * 2) { probabilityToWound = Probability.TWO_UP; }
			if(strength > toughness) { probabilityToWound = Probability.THREE_UP;}
			if(strength == toughness) { probabilityToWound = Probability.FOUR_UP;}
			if(strength < toughness) { probabilityToWound = Probability.FIVE_UP; }
			var wounds = hits * probabilityToWound;
			
			//Calculate the saves  
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
			
			//calculate Damage
			double missedSaves = Math.floor(wounds - (wounds * probabilityToSave));
			double damageMultiplier = weapon.getDamage() > enemy.getHitPoints() ?
					enemy.getHitPoints() : weapon.getDamage();
			double woundsAfterFeelNoPain = (missedSaves * damageMultiplier) * enemy.getFeelNoPain();
			damage += (missedSaves * damageMultiplier) - woundsAfterFeelNoPain;
		}
		
		return damage;
	}

	public int add(Equipment equipment) {
		int savedIndex = index;
		weapons.put(index, equipment);
		index++;
		return savedIndex;
	}
	public void remove(int key) {
		weapons.remove(key);
	}
	
	public void add(SpecialRuleUnit specialRule) {
		this.specialRules.add(specialRule);
	}
	public void remove(SpecialRuleUnit specialRule) {
		this.specialRules.remove(specialRule);
	}
	private boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}
	
}

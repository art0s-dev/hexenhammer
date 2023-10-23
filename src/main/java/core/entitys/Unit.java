package core.entitys;

import java.util.HashMap;
import java.util.Map;

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

	public double attack(Profile enemy) {
		double damage = 0;
		for (Map.Entry<Integer, Equipment> equipment : weapons.entrySet()) {
			Weapon weapon = equipment.getValue().weapon();
			int quantity = equipment.getValue().quantity();
			
			//calculate to hit
			double attacks = (weapon.getAttacks() * quantity);
			double hits = attacks * weapon.getToHit();
			
			//calculate to wound 
			int strength = weapon.getStrength();
			int toughness = enemy.getToughness();
			//default case is strength is lower than toughness 
			double probabilityToWound = Probability.FIVE_UP;
			if(strength >= toughness * 2) {
				probabilityToWound = Probability.TWO_UP;
			} else if (strength > toughness) {
				probabilityToWound = Probability.THREE_UP;
			} else if(strength == toughness) {
				probabilityToWound = Probability.FOUR_UP;
			} else if(strength < toughness * 2) {
				probabilityToWound = Probability.SIX_UP;
			}
			double savesToBeMade = hits * probabilityToWound;
			
			//calculate save
			//Calculate the armour penetration
			double armourSave = enemy.getArmorSave();
			int symbolicArmourSave = 0;
			if(armourSave == Probability.SIX_UP) {
				symbolicArmourSave = 1;
			} else if(armourSave == Probability.FIVE_UP) {
				symbolicArmourSave = 2;
			} else if(armourSave == Probability.FOUR_UP) {
				symbolicArmourSave = 3;
			} else if(armourSave == Probability.THREE_UP) {
				symbolicArmourSave = 4;
			} else if(armourSave == Probability.TWO_UP) {
				symbolicArmourSave = 5;
			}
			int modifiedArmourSave = symbolicArmourSave - weapon.getArmorPenetration();
			double probabilityToSave = modifiedArmourSave / 6.00;
			if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
				probabilityToSave = enemy.getInvulnerableSave();
			} 
			
			//Calculate Damage and add FNP
			double damageBeforeFeelNoPain = savesToBeMade * probabilityToSave * weapon.getDamage();
			double damageAfterFeelNoPain = damageBeforeFeelNoPain * enemy.getFeelNoPain();
			damage += damageBeforeFeelNoPain - damageAfterFeelNoPain;
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
	
}

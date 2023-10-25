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
			var weapon = equipment.getValue().weapon();
			var quantity = equipment.getValue().quantity();
			
			//Calculate the hits
			var hits = (double) weapon.getAttacks() * quantity * weapon.getToHit();
			
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
			double missedSaves = wounds * probabilityToSave;
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
	
}

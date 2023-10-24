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
		return BattleSequence.resolve(weapons, enemy);
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
	
	private class BattleSequence{
		public Profile enemy;
		public Weapon weapon;
		public int quantity;
		public BattleSequence() {}
		
		/**
		 * resolve
		 * this method shall resolve the combat round 
		 * only this method shall be used to set the fields from above
		 */
		public static double resolve(HashMap<Integer, Equipment> weapons, Profile enemy) {
			double damage = 0;
			var battleSequence = new Unit().new BattleSequence();
			battleSequence.enemy = enemy;
			
			for (Map.Entry<Integer, Equipment> equipment : weapons.entrySet()) {
				battleSequence.weapon = equipment.getValue().weapon();
				battleSequence.quantity = equipment.getValue().quantity();
				
				var hits = battleSequence.calculateHits();
				var wounds = battleSequence.calculateWounds(hits);
				var save = battleSequence.calculateSave();
				damage += battleSequence.calculateDamage(wounds, save);
			}
			
			return damage;
		}
		
		private double calculateHits() {
			//TODO: Implement Rerolling the Hit roll
			//TODO: Implement Sustained Hits
			//TODO: Implement 6es Autowound
			
			var attacks = (double) weapon.getAttacks() * quantity;
			return attacks * weapon.getToHit();
		}
		
		private double calculateWounds(double hits) {
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
			
			//TODO:Implement Reroll to Wound
			//TODO: Implement Devastating Wounds
			//TODO: Implement ANTI
			
			return hits * probabilityToWound;
		}
		
		private double calculateSave() {
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
			return probabilityToSave;
		}
		
		private double calculateDamage(double wounds, double save) {
			//Calculate Damage and add FNP
			//TODO: implement wound capping and mortal wound jumping
			//Also wenn ein schwerer Bolter z.B eine Wunde verursacht, das Modell aber nur einen Lebenspunkt hat,
			//(der schwere Bolter aber 3 Schadenspunkte macht), soll nur so viel schaden an einem
			//Modell gemacht werden, wie das Modell Lebenspunkte hat. 
			//Kann ich das kalkulieren ohne eine Schleife oder muss ich das mit schleife machen??
			double damageBeforeFeelNoPain = wounds * save * weapon.getDamage();
			double damageAfterFeelNoPain = damageBeforeFeelNoPain * enemy.getFeelNoPain();
			return damageBeforeFeelNoPain - damageAfterFeelNoPain;
		}
	}
	
}

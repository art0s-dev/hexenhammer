package core.entitys;

import java.util.HashMap;
import java.util.HashSet;

/**
 * This is the cornerstone of the software
 * Each unit represents a bunch of weapons that will be rolled
 * against another unit   
 */
public class Unit {
	
	/**
	 * With this method we add, delete and edit the weapons of a unit.
	 * When the weapon is already there it depends on the quantity if it is deleted
	 * For example if we say bolter subtract 5 and there are only 4 bolters, they get deleted
	 * if we set bolters to 0 we will also delete the entry 
	 * When the quantity is positive then the entry will be set to the given value
	 * if there is no enty, we add it 
	 */
	public void change(Weapon weapon, int quantity) {
		var weaponDoesNotExist = !weapons.contains(weapon);
		if(weaponDoesNotExist) {
			weapons.add(weapon);
			quantities.put(weapon, quantity);
			return;
		}
		
		var quantityWillBeNegative =  quantities.get(weapon) + quantity <= 0;
		if(quantityWillBeNegative || quantity == 0) {
			weapons.remove(weapon);
			quantities.remove(weapon);
			return;
		}
		
		quantities.put(weapon, quantity);
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
	 * warhammer 40k rulebookit should cover the shooting phase,  
	 * aswell as the fight phase and all the generic special rules
	 * https://www.warhammer-community.com/2023/06/02/download-the-new-warhammer-40000-rules-for-free-right-here/ 
	 */
	public double attack(Profile enemy) {
		double damage = 0;
		
		for (Weapon weapon : weapons) {
			/**
			 * calculate the hits
			 * we determine the number of attacks the unit has and let them roll to hit
			 * here we can apply modifiers like sustained hits
			 * and after that we apply rerolls 
			 */
			double attacks = weapon.getAttacks() * quantities.get(weapon);
			double hits = (double) attacks  * weapon.getToHit();
			if(this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT)) {
				hits += ((attacks - hits) / 6) * weapon.getToHit(); 
			}
			
			/**
			 * calculate the wounds
			 * in this section we compare the attacking weapon and the defending
			 * profile against each other to determine a probability to wound the target. 
			 * The default case is the worst case (so it only can get better)
			 */
			int strength = weapon.getStrength();
			int toughness = enemy.getToughness();
			double probabilityToWound = Probability.SIX_UP;
			if(strength >= toughness * 2) { probabilityToWound = Probability.TWO_UP; }
			if(strength > toughness) { probabilityToWound = Probability.THREE_UP;}
			if(strength == toughness) { probabilityToWound = Probability.FOUR_UP;}
			if(strength < toughness) { probabilityToWound = Probability.FIVE_UP; }
			double wounds = hits * probabilityToWound;
			
			/**
			 * calculate the saves
			 * here we calculate how high the chances are for a unit to tank the hit
			 * we have 2 different mechanics: we tank with the armor or we
			 * dodge the attack. The armor save can be modified via armor penetration
			 * the invulnerable(dodge)-save cannot be modified. If its higher, we 
			 * take the dodge save. For determining the probability to tank a hit we assign
			 * an int value and subtract the armor pen.
			 */
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
			
			/**
			 * calculate damage
			 * during this step we determine how the damage is done to a model.
			 * we substract the tanked or dodged wounds from the wound pool 
			 * and apply the damage for each wound. Each wound can be assigned to a new
			 * model in the unit. that means if a weapon has damage 3 and a model
			 * has only 2 hp, the weapon can only deal 2 hp damage.
			 * We round the number of wounds down to have an integer of damage because 
			 * there are no 12.56555 hitpoints on a unit.
			 * Also we apply further wound migitation mechanics like feel no pain
			 */
			double missedSaves = Math.floor(wounds - (wounds * probabilityToSave));
			double damageMultiplier = weapon.getDamage();
			if(weapon.getDamage() > enemy.getHitPoints()) {
				damageMultiplier = enemy.getHitPoints();
			}
			double woundsAfterFeelNoPain = (missedSaves * damageMultiplier) * enemy.getFeelNoPain();
			damage += (missedSaves * damageMultiplier) - woundsAfterFeelNoPain;
		}
		
		return damage;
	}
	
	private HashSet<Weapon> weapons = new HashSet<Weapon>();
	private HashMap<Weapon, Integer> quantities = new HashMap<>();
	private boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<SpecialRuleUnit>();

}

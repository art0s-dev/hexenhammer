package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import core.Enemy.SpecialRuleProfile;
import core.Weapon.AntiType;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;
import lombok.val;

/**
 * This is the cornerstone of the software
 * Each unit represents a bunch of weapons that will be rolled
 * against another unit. In Order to calculate the damage Outcome of a Unit
 * you will have to:
 * - create some weapons, a unit and a target profile
 * - equip() the weapons and maybe add() some special rules
 * - attack() the enemy profile  
 */
public final class Unit {
	
	/**
	 * With this method we add, delete and edit the weapons of a unit. 
	 * @param Weapon - The weapon we want to edit 
	 * @param quantity - The quantity the weapns shall recieve   
	 * @implNote Quantity negative or zero deletes the weapon 
	 */
	public void equip(byte quantity, Weapon weapon) {
		
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
		weapons.put(weapon, (byte) (oldQuantity + quantity) );
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
	
	public float attack(Enemy enemy) {
		val filteredWeapons = filter(weapons, phase);
		val unitParams = setUnitParameters();
		val enemyParamaters = setEnemyParameters();
		return filteredWeapons 
				.map(stream -> calculateDamage(unitParams, enemy, stream))
				.reduce(0f, (sum, damage) -> sum + damage);
	}
	
	private record UnitParameters() {};
	private UnitParameters setUnitParameters() {
		return new UnitParameters();
	}
	
	private record EnemyParameters() {};
	private EnemyParameters setEnemyParameters() {
		return new EnemyParameters();
	}
	
	private Stream<Entry<Weapon, Byte>> filter(Map<Weapon, Byte> weapons, Phase phase) {
		val weaponStream = weapons.entrySet().parallelStream();
		val isForBothPhases = phase.equals(Phase.BOTH);
		return isForBothPhases ? weaponStream 
				: weaponStream.filter(entry -> entry.getKey().getPhase() == phase);
	} 
	
	private float calculateDamage(UnitParameters params, Enemy enemy, Entry<Weapon, Byte> stream) {
		Weapon weapon = stream.getKey();
		
		//calculate the hits
		byte quantity = stream.getValue();
		float attacks = weapon.getAttacks() * quantity;
		float chanceToHit = weapon.getToHit();
		
		//Modify hit rolls
		if(this.has(SpecialRuleUnit.ADD_ONE_TO_HIT)) {
			chanceToHit = Probability.modifyRoll(chanceToHit, '+');
		}
		if(enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL)) {
			chanceToHit = Probability.modifyRoll(chanceToHit, '-');
		}
		
		//Calculate hits
		float hits = attacks  * chanceToHit;
		
		//Lethal hits bypass the wound roll
		//Subtract lethal hits from the hit pool 
		float lethalHits = 0f;
		boolean lethalHitsAreActive = this.has(SpecialRuleUnit.LETHAL_HITS) 
				|| weapon.has(SpecialRuleWeapon.LETHAL_HITS);
		boolean addOneToHit = this.has(SpecialRuleUnit.ADD_ONE_TO_HIT);
		float lethalHitsModifier = Probability.SIX_UP;
		
		if(lethalHitsAreActive) {
			if(addOneToHit) {
				lethalHitsModifier = Probability.FIVE_UP;
			}
			
			lethalHits = attacks * lethalHitsModifier;
			hits -= lethalHits;
		}
		
		//Do the rerolls for the hit roll
		boolean hitRollWasRerolled = false;
		float missedHits = attacks - hits;
		float hitRerollPool = 0;
		if(this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT)) {
			//Rolling ones has the same chance as rolling a Six
			hitRerollPool = missedHits * Probability.SIX_UP; 
			hitRollWasRerolled = true;
		}
		if(this.has(SpecialRuleUnit.REROLL_HIT_ROLL)) {
			hitRerollPool = missedHits;
			hitRollWasRerolled = true;
		}

		if(hitRollWasRerolled) {
			float rerolledLethalHits = 0;
			float rerolledHits = hitRerollPool * weapon.getToHit(); 
			
			if(lethalHitsAreActive) {
				rerolledLethalHits = hitRerollPool * lethalHitsModifier;
				rerolledHits -= rerolledLethalHits;
				lethalHits += rerolledLethalHits;
			} 

			hits += rerolledHits;
		}
		
		//Torrent weapons always hit
		if(weapon.has(SpecialRuleWeapon.TORRENT)) {
			hits = weapon.getAttacks() * quantity;
		}
		
		//calculate the wound roll   
		byte strength = weapon.getStrength();
		byte toughness = enemy.getToughness();
		float probabilityToWound = Probability.SIX_UP;
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
		
		//calculate wounds 
		float wounds = (hits * probabilityToWound) + lethalHits;
		
		//Reroll wounds
		if(this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND)) {
			wounds += ((hits - wounds) / 6) * probabilityToWound; 
		}
		boolean rerollWoundRoll = this.has(SpecialRuleUnit.REROLL_WOUND_ROLL) 
				|| weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL);
		if(rerollWoundRoll) {
			wounds += (hits - wounds) * probabilityToWound; 
		}
		
		//determine Armour
		float armourSave = enemy.getArmorSave();
		HashMap<Float,Byte> symbolicArmourSave = new HashMap<>();
		symbolicArmourSave.put(Probability.SIX_UP, (byte)1);
		symbolicArmourSave.put(Probability.FIVE_UP, (byte)2);
		symbolicArmourSave.put(Probability.FOUR_UP, (byte)3);
		symbolicArmourSave.put(Probability.THREE_UP, (byte)4);
		symbolicArmourSave.put(Probability.TWO_UP, (byte)5);
		byte modifiedArmourSave = (byte) (symbolicArmourSave.get(armourSave) - weapon.getArmorPenetration());
		
		//Take cover!
		boolean weaponIsShooting = weapon.getPhase() == Phase.SHOOTING;
		boolean enemyHasCover = enemy.has(SpecialRuleProfile.HAS_COVER);
		boolean unitDoesNotIgnoreCover = !this.has(SpecialRuleUnit.IGNORE_COVER);
		boolean saveIsNotHighestPossible = modifiedArmourSave <= 5;
		if(weaponIsShooting && enemyHasCover && unitDoesNotIgnoreCover && saveIsNotHighestPossible) {
			modifiedArmourSave++;
		}
		
		//Is there an invul save?
		float probabilityToSave = modifiedArmourSave / 6f;
		if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
			probabilityToSave = enemy.getInvulnerableSave();
		} 
		
		//Get Missed Saves and determine damage
		float missedSaves = wounds - (wounds * probabilityToSave);
		float damageMultiplier = weapon.getDamage() + weapon.getMelta();
		if(damageMultiplier > enemy.getHitPoints()) {
			damageMultiplier = enemy.getHitPoints();
		}
		
		//assert damage
		float damagePotential = missedSaves * damageMultiplier;
		float woundsAfterFeelNoPain = damagePotential * enemy.getFeelNoPain();
		return damagePotential - woundsAfterFeelNoPain;
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
	private HashMap<Weapon, Byte> weapons = new HashMap<>();
	
	/**
	 * The Set of special rules each unit has.
	 * @see SpecialRuleUnit
	 */
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<>();
	
	private boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}

}

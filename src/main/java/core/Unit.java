package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import core.Enemy.SpecialRuleProfile;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;
import core.combat.DicePool;
import core.combat.FeelNoPainDiceRoll;
import core.combat.HitDiceRoll;
import core.combat.SavingThrowDiceRoll;
import core.combat.WoundDiceRoll;
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
public class Unit {
	
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
	
	/**
	 * The attack interface for every unit calculates
	 * the total damage from every equipped and filtered weapon
	 * from that unit and uses the combat class for further calculations
	 * @see Combat
	 */
	public float attack(Enemy enemy) {
		val filteredWeapons = _filter(weapons, phase);
		return filteredWeapons
				.map(entry -> _dealDamage(entry, enemy))
				.reduce(0f, (sum, damage) -> sum + damage);
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
	 * Filters the weapons before attacking according to the phase
	 * that is set. So when the fighting phase is set it ensures, 
	 * that only close combat weapons are taken 
	 */
	private Stream<Entry<Weapon, Byte>> _filter
	(Map<Weapon, Byte> weapons, Phase phase) {
		val weaponStream = weapons.entrySet().parallelStream();
		val isForBothPhases = phase.equals(Phase.BOTH);
		return isForBothPhases ? weaponStream 
				: weaponStream.filter(entry -> entry.getKey().getPhase() == phase);
	} 
	
	/**
	 * The Set of special rules each unit has.
	 * @see SpecialRuleUnit
	 */
	private HashSet<SpecialRuleUnit> specialRules = new HashSet<>();
	
	public boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}
	
	private float _dealDamage(Entry<Weapon, Byte> entry, Enemy enemy) {
		val weapon = entry.getKey();
		val quantity = entry.getValue();
		val rules = _setRules(weapon, enemy);
		
		val hitRoll= new HitDiceRoll(this, weapon, enemy, rules);
		val woundRoll = new WoundDiceRoll(this, weapon, enemy, rules);
		val savingThrows = new SavingThrowDiceRoll(this, weapon, enemy, rules);
		val feelNoPainRoll = new FeelNoPainDiceRoll(this, weapon, enemy, rules);
		
		val shots = (float) weapon.getAttacks() * quantity;
		val hits = hitRoll.roll(new DicePool(shots, 0f));
		val wounds = woundRoll.roll(hits);
		val saves = savingThrows.roll(wounds);
		val damage = new DicePool(0f, weapon.getDamage() * saves.result());
		val feelNoPain = feelNoPainRoll.roll(damage);
		return feelNoPain.result();
	}
	
	
	/**
	 * Creates the feature flags for the battle sequence
	 * this method gets called before the damage of a new weapon is calculated
	 */
	private CombatRules _setRules(Weapon weapon, Enemy enemy) {
		return new CombatRules(
			this.has(SpecialRuleUnit.ADD_ONE_TO_HIT),
			enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL),
			this.has(SpecialRuleUnit.LETHAL_HITS) || weapon.has(SpecialRuleWeapon.LETHAL_HITS),
			this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT),
			this.has(SpecialRuleUnit.REROLL_HIT_ROLL),
			weapon.has(SpecialRuleWeapon.TORRENT),
			this.has(SpecialRuleUnit.ADD_ONE_TO_WOUND),
			enemy.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL),
			this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND),
			this.has(SpecialRuleUnit.REROLL_WOUND_ROLL) || weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL),
			weapon.getAntiType().isPresent(),
			enemy.has(SpecialRuleProfile.HAS_COVER),
			this.has(SpecialRuleUnit.IGNORE_COVER)
		);
	}

}

package core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.stream.Stream;

import arch.Model;
import core.Weapon.Range;
import core.Weapon.SpecialRuleWeapon;
import core.combat.CombatRules;
import core.combat.DicePool;
import core.combat.FeelNoPainDiceRoll;
import core.combat.HitDiceRoll;
import core.combat.SavingThrowDiceRoll;
import core.combat.WoundDicePool;
import core.combat.WoundDiceRoll;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
@Builder
public class Unit extends Model {
	@Getter @Setter @Builder.Default private String name = "New Unit";
	@Getter @Setter @Builder.Default private byte movement = 1;
	@Getter @Setter @Builder.Default private byte toughness = 1;
	@Getter @Setter @Builder.Default private float armorSave = Probability.NONE;
	@Getter @Setter @Builder.Default private byte hitPoints = 1;
	@Getter @Setter @Builder.Default private byte leadership = 1;
	@Getter @Setter @Builder.Default private byte objectControl = 1;
	@Getter @Setter @Builder.Default private float invulnerableSave = Probability.NONE;
	@Getter @Setter @Builder.Default private float feelNoPain = Probability.NONE;
	@Getter @Setter @Builder.Default private Type type = Type.INFANTRY;
	@Getter @Setter @Builder.Default private boolean useAsEnemy = false;
	
	/**
	 * This is the register of weapons a unit has.
	 * This register can be edited via the equip method. 
	 * It contains the signature of the object as a key 
	 * and a quantity as the value. 
	 */
	@Getter @Builder.Default private ArrayList<Equipment> weapons = new ArrayList();
	
	@AllArgsConstructor
	static public class Equipment{
		public Weapon weapon;
		public byte quantity;
	}
	
	public void equip(byte quantity, Weapon weapon) {
		weapons.add(new Equipment(weapon, quantity));
	}
	
	public void unequip(byte index, byte quantity) {
		int _index = (int) index;
		boolean noEquipmentFound = weapons.size() <= _index;
		
		if(noEquipmentFound) {
			return;
		}
		
		if(quantity == 0) {
			weapons.remove(_index);
			return;
		}
		
		Equipment equipment = weapons.get(_index);
		equipment.quantity = quantity;
	}
	
	/**
	 * The SpecialRuleUnit builds an API for editing special cases in our unit
	 * these rules apply to the battle sequence in global. There is also
	 * a SpecialRuleWeapon which will be applied before the global rule if it is better 
	 */
	public enum SpecialRuleUnit{
		//For Attack
		REROLL_ONES_TO_HIT,
		REROLL_HIT_ROLL,
		REROLL_ONES_TO_WOUND,
		REROLL_WOUND_ROLL,
		ADD_ONE_TO_HIT,
		ADD_ONE_TO_WOUND,
		IGNORE_COVER,
		LETHAL_HITS,
		//For defense
		SUBTRACT_ONE_FROM_HIT_ROLL, 
		SUBTRACT_ONE_FROM_WOUND_ROLL, 
		HAS_COVER
	}
	
	public void add(SpecialRuleUnit specialRule) {
		this.specialRules.add(specialRule);
	}
	
	public void remove(SpecialRuleUnit specialRule) {
		this.specialRules.remove(specialRule);
	}

	/**
	 * The unit type an enemy has. 
	 * It is used to determine wounds for anti weapon capabilities.
	 */
	public enum Type {
		INFANTRY, MONSTER, VEHICLE
	}
	
	/**
	 * Distinguishes the weapon between a combat and a shooting weapon 
	 */
	public enum Phase { SHOOTING, FIGHT, BOTH }
	
	/**
	 * Sets the Filter which weapons shall be used for the attack sequence
	 * @implNote The default is set to both phases
	 * @param phase
	 */
	public void usePhase(Phase phase) {
		this.phase = phase;
	}

	@Builder.Default Phase phase = Phase.BOTH;
	
	/**
	 * The attack interface for every unit calculates
	 * the total damage from every equipped and filtered weapon 
	 * from that unit and uses the combat class for further calculations
	 * @see Combat
	 */
	public float attack(Unit enemy) {
		val filteredWeapons = _filter(phase);
		return filteredWeapons
				.map(weaponAndQuantity -> _dealDamage(weaponAndQuantity, enemy))
				.reduce(0f, (sum, damage) -> sum + damage);
	}
	
	
	
	/**
	 * Filters the weapons before attacking according to the phase
	 * that is set. So when the fighting phase is set it ensures, 
	 * that only close combat weapons are taken 
	 */
	private Stream<Equipment> _filter(Phase phase) {
		val weaponStream = weapons.parallelStream();
		
		val isForBothPhases = phase.equals(Phase.BOTH);
		return isForBothPhases ? weaponStream 
				: weaponStream.filter(entry -> _filterWeaponRange(entry.weapon, phase));
	} 
	
	/**
	 * evaluates the range of a weapon and the picked phase
	 * so that only the user can "just use shooting weapons" and such 
	 */
	private static boolean _filterWeaponRange(Weapon weapon, Phase phase) {
		boolean shootingIsPickedAndRangeIsShooting = phase.equals(Phase.SHOOTING) 
				&& weapon.getRange().equals(Range.SHOOTING);
		if(shootingIsPickedAndRangeIsShooting) {
			return true;
		}
		
		boolean meeleIsPickedAndRangeIsMelee = phase.equals(Phase.FIGHT) 
				&& weapon.getRange().equals(Range.MELEE);

		if(meeleIsPickedAndRangeIsMelee) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * The Set of special rules each unit has.
	 * @see SpecialRuleUnit
	 */
	@Builder.Default private HashSet<SpecialRuleUnit> specialRules = new HashSet<>();
	
	public boolean has(SpecialRuleUnit specialRule) {
		return this.specialRules.contains(specialRule);
	}
	
	/**
	 * Calculates the damage of a single weapon against a given enemy
	 * @implNote shall utilize the combat package
	 */
	private float _dealDamage(Equipment weaponAndQuantity, Unit enemy) {
		val weapon = weaponAndQuantity.weapon;
		val quantity = weaponAndQuantity.quantity;
		val rules = _setRules(weapon, enemy);
		
		//Create all the Dice Rolls and give them the necessary informations
		val hitRoll = new HitDiceRoll(this, weapon, enemy, rules);
		val woundRoll = new WoundDiceRoll(this, weapon, enemy, rules);
		val savingThrows = new SavingThrowDiceRoll(this, weapon, enemy, rules);
		val feelNoPainRoll = new FeelNoPainDiceRoll(this, weapon, enemy, rules);
		
		//create a dice pool and pass it to the next step in the combat sequence
		val attacks = (float) weapon.getAttacks() * quantity;
		val hits = hitRoll.roll(new DicePool(attacks, 0f));
		WoundDicePool woundPool = (WoundDicePool) woundRoll.roll(hits);
		val saves = savingThrows.roll(woundPool);
		val wounds = saves.getResult() + woundPool.getDevastatingWounds();
		
		val damage = new DicePool(0f, weapon.getDamage() * wounds);
		val feelNoPain = feelNoPainRoll.roll(damage);
		
		return feelNoPain.getResult();
	}
	
	/**
	 * Creates the feature flags for the battle sequence
	 * this method gets called before the damage of a new weapon is calculated
	 */
	private CombatRules _setRules(Weapon weapon, Unit enemy) {
		return new CombatRules(
			this.has(SpecialRuleUnit.ADD_ONE_TO_HIT),
			enemy.has(SpecialRuleUnit.SUBTRACT_ONE_FROM_HIT_ROLL),
			this.has(SpecialRuleUnit.LETHAL_HITS),
			this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT),
			this.has(SpecialRuleUnit.REROLL_HIT_ROLL),
			weapon.has(SpecialRuleWeapon.TORRENT),
			this.has(SpecialRuleUnit.ADD_ONE_TO_WOUND),
			enemy.has(SpecialRuleUnit.SUBTRACT_ONE_FROM_WOUND_ROLL),
			this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND),
			this.has(SpecialRuleUnit.REROLL_WOUND_ROLL),
			weapon.getAntiType().isPresent(),
			enemy.has(SpecialRuleUnit.HAS_COVER),
			this.has(SpecialRuleUnit.IGNORE_COVER),
			weapon.getSustainedHits() > 0,
			weapon.getMelter() > 0,
			weapon.has(SpecialRuleWeapon.DEVASTATING_WOUNDS)
		);
	}

}

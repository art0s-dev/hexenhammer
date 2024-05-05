package core;

import static core.Probability.FIVE_UP;
import static core.Probability.FOUR_UP;
import static core.Probability.NONE;
import static core.Probability.SIX_UP;
import static core.Probability.THREE_UP;
import static core.Probability.TWO_UP;
import static core.Probability.modifyRoll;
import static core.Probability.Modifier.MINUS_ONE;
import static core.Probability.Modifier.PLUS_ONE;
import static core.Unit.Phase.BOTH;
import static core.Unit.SpecialRuleUnit.ADD_ONE_TO_HIT;
import static core.Unit.SpecialRuleUnit.ADD_ONE_TO_WOUND;
import static core.Unit.SpecialRuleUnit.HAS_COVER;
import static core.Unit.SpecialRuleUnit.IGNORE_COVER;
import static core.Unit.SpecialRuleUnit.LETHAL_HITS;
import static core.Unit.SpecialRuleUnit.REROLL_HIT_ROLL;
import static core.Unit.SpecialRuleUnit.REROLL_ONES_TO_HIT;
import static core.Unit.SpecialRuleUnit.REROLL_ONES_TO_WOUND;
import static core.Unit.SpecialRuleUnit.REROLL_WOUND_ROLL;
import static core.Unit.SpecialRuleUnit.SUBTRACT_ONE_FROM_HIT_ROLL;
import static core.Unit.SpecialRuleUnit.SUBTRACT_ONE_FROM_WOUND_ROLL;
import static core.Weapon.Range.SHOOTING;
import static core.Weapon.SpecialRuleWeapon.DEVASTATING_WOUNDS;
import static core.Weapon.SpecialRuleWeapon.TORRENT;
import static java.util.Collections.unmodifiableMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import arch.Model;
import core.Weapon.AntiType;
import core.Weapon.Range;
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
	@Getter @Setter @Builder.Default private int movement = 1;
	@Getter @Setter @Builder.Default private int toughness = 1;
	@Getter @Setter @Builder.Default private float armorSave = Probability.NONE;
	@Getter @Setter @Builder.Default private int hitPoints = 1;
	@Getter @Setter @Builder.Default private int leadership = 1;
	@Getter @Setter @Builder.Default private int objectControl = 1;
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
		public int quantity;
	}
	
	public void equip(int quantity, Weapon weapon) {
		weapons.add(new Equipment(weapon, quantity));
	}
	
	public void unequip(int index, int quantity) {
		boolean noEquipmentFound = weapons.size() <= index;
		
		if(noEquipmentFound) {
			return;
		}
		
		if(quantity == 0) {
			weapons.remove(index);
			return;
		}
		
		Equipment equipment = weapons.get(index);
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
	public List<CombatResult> attack(Unit enemy) {
		List<Equipment> equipments = _filter(phase).toList();
		List<CombatResult> results = new ArrayList<>();
		
		for(Equipment equipment: equipments) {
			Weapon weapon = equipment.weapon;
			int quantity = equipment.quantity;
			CombatRules rules = _setRules(weapon, enemy);
			float attacks = weapon.getAttacks() * quantity;
			float chanceToHit = weapon.getToHit();
			
			//Modify hit rolls
			if(rules.addOneToHitRoll()) {
				chanceToHit = modifyRoll(chanceToHit, PLUS_ONE);
			}
			if(rules.subtractOneFromHitRoll()) {
				chanceToHit = modifyRoll(chanceToHit, MINUS_ONE);
			}
			
			//Calculate hits
			float hits = attacks  * chanceToHit;
			
			//Lethal hits bypass the wound roll
			//Subtract lethal hits from the hit pool 
			float lethalHits = 0f;
			float lethalHitsModifier = SIX_UP;
			
			if(rules.lethalHits()) {
				if(rules.addOneToHitRoll()) {
					lethalHitsModifier = FIVE_UP;
				}
				
				lethalHits = attacks * lethalHitsModifier;
				hits -= lethalHits;
			}
			
			//Do the rerolls for the hit roll
			boolean hitRollWasRerolled = false;
			float missedHits = attacks - hits;
			float hitRerollPool = 0;
			if(rules.rerollOnesToHit()) {
				//Rolling ones has the same chance as rolling a Six
				hitRerollPool = missedHits * SIX_UP; 
				hitRollWasRerolled = true;
			}
			if(rules.rerollHitRoll()) {
				hitRerollPool = missedHits;
				hitRollWasRerolled = true;
			}

			if(hitRollWasRerolled) {
				float rerolledLethalHits = 0;
				float rerolledHits = hitRerollPool * weapon.getToHit(); 
				
				if(rules.lethalHits()) {
					rerolledLethalHits = hitRerollPool * lethalHitsModifier;
					rerolledHits -= rerolledLethalHits;
					lethalHits += rerolledLethalHits;
				} 

				hits += rerolledHits;
			}
			
			//Torrent weapons always hit
			if(rules.torrent()) {
				hits = weapon.getAttacks() * quantity;
			}
			
			//calculate the wound roll   
			int strength = weapon.getStrength();
			int toughness = enemy.getToughness();
			float probabilityToWound = compare(strength, toughness);
			
			//Anti Type weapons
			if(rules.antiTypeWeapon()) {
				AntiType antiType = weapon.getAntiType().orElseThrow();
				boolean antiTypeIsProfileType = antiType.type() == enemy.getType();
				boolean antiTypeProbabilityIsHigherThanWoundRoll = antiType.probability() > probabilityToWound;
				
				if(antiTypeIsProfileType && antiTypeProbabilityIsHigherThanWoundRoll) {
					probabilityToWound = antiType.probability();
				}
			}
			
			//Modify wounds
			if(rules.addOneToWoundRoll()) {
				probabilityToWound = modifyRoll(probabilityToWound, PLUS_ONE); 
			}
			if(rules.subtractOneFromWoundRoll()) {
				probabilityToWound = modifyRoll(probabilityToWound, MINUS_ONE);
			}
			
			//calculate wounds 
			float wounds = (hits * probabilityToWound) + lethalHits;
			
			//Devastating wounds bypass armour and invulnerable save
			//Subtract devastating wounds from the wound pool
//			boolean devastatingWoundsAreActive = weapon.has(SpecialRuleWeapon.DEVASTATING_WOUNDS);
//			float devastatingWounds = 0;
//			if(devastatingWoundsAreActive) {
//				devastatingWounds = (wounds / 6);
//				wounds -= devastatingWounds;
//			}
			
			//Reroll wounds
			if(rules.rerollOnesToWound()) {
				wounds += ((hits - wounds) / 6) * probabilityToWound; 
			}
			if(rules.rerollWoundRoll()) {
				wounds += (hits - wounds) * probabilityToWound; 
			}
			
			//determine Armour
			float armourSave = enemy.getArmorSave();
			int modifiedArmourSave = ARMOUR_SAVES.get(armourSave) - weapon.getArmorPenetration();
			
			//Take cover!
			boolean weaponIsShooting = weapon.getRange() == SHOOTING;
			boolean saveIsNotHighestPossible = modifiedArmourSave <= 5;
			if(weaponIsShooting && rules.enemyHasCover() && !rules.ignoreCover() && saveIsNotHighestPossible) {
				modifiedArmourSave++;
			}
			
			//Is there an invul save?
			float probabilityToSave = modifiedArmourSave / 6f;
			if(modifiedArmourSave <= 0 || enemy.getInvulnerableSave() > probabilityToSave) {
				probabilityToSave = enemy.getInvulnerableSave();
			} 
			
			//Get Missed Saves and determine damage
			float missedSaves = wounds - (wounds * probabilityToSave);
			float damageMultiplier = weapon.getDamage() + weapon.getMelter();
			if(damageMultiplier > enemy.getHitPoints()) {
				damageMultiplier = enemy.getHitPoints();
			}
			
			//assert damage
			//float damagePotential = (missedSaves + devastatingWounds) * damageMultiplier;
			float damagePotential = missedSaves * damageMultiplier;
			float woundsAfterFeelNoPain = damagePotential * enemy.getFeelNoPain();
			float damage = damagePotential - woundsAfterFeelNoPain;
			
			//Save everythig in an object for later usage
			CombatResult result = new CombatResult(
				weapon,
				quantity,
				attacks,
				chanceToHit,
				missedHits,
				lethalHitsModifier,
				missedHits,
				probabilityToWound,
				wounds,
				probabilityToSave,
				missedSaves,
				damageMultiplier,
				damagePotential,
				woundsAfterFeelNoPain,
				damage
			);

			results.add(result);
		}
		
		return results;
	}
	
	/**
	 * Determines a probability for a weapon to wound a target 
	 */
	private static float compare(int weaponsStrength, int enemyToughness) {
		float probabilityToWound = Probability.SIX_UP;
		if(weaponsStrength >= enemyToughness * 2) { probabilityToWound = TWO_UP; }
		if(weaponsStrength > enemyToughness) { probabilityToWound = THREE_UP;}
		if(weaponsStrength == enemyToughness) { probabilityToWound = FOUR_UP;}
		if(weaponsStrength < enemyToughness) { probabilityToWound = FIVE_UP; }
		return probabilityToWound;
	}
	
	/**
	 * Filters the weapons before attacking according to the phase
	 * that is set. So when the fighting phase is set it ensures, 
	 * that only close combat weapons are taken 
	 */
	private Stream<Equipment> _filter(Phase phase) {
		val weaponStream = weapons.parallelStream();
		Function<Equipment,Boolean> isFight = (equipment) -> phase.equals(Phase.FIGHT) 
				&& equipment.weapon.getRange().equals(Range.MELEE);
		
		if(phase.equals(BOTH)) {
			return weaponStream;
		} 

		return weaponStream.filter(equipment -> isFight.apply(equipment));
	} 
	
	/**
	 * The Armour save characteristica of an enemy unit tells us how
	 * tanky a unit is. This implementation subtracts the armour penetration value
	 * from the weapon from the given values, until there is no armpur save left
	 */
	private static final HashMap<Float,Integer> ARMOUR_SAVES = new HashMap<>();
	static {
		ARMOUR_SAVES.put(NONE, 0);
		ARMOUR_SAVES.put(SIX_UP, 1);
		ARMOUR_SAVES.put(FIVE_UP, 2);
		ARMOUR_SAVES.put(FOUR_UP, 3);
		ARMOUR_SAVES.put(THREE_UP, 4);
		ARMOUR_SAVES.put(TWO_UP, 5);
		unmodifiableMap(ARMOUR_SAVES);
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
	 * Creates the feature flags for the battle sequence
	 * this method gets called before the damage of a new weapon is calculated
	 */
	private CombatRules _setRules(Weapon weapon, Unit enemy) {
		return new CombatRules(
			this.has(ADD_ONE_TO_HIT),
			enemy.has(SUBTRACT_ONE_FROM_HIT_ROLL),
			this.has(LETHAL_HITS),
			this.has(REROLL_ONES_TO_HIT),
			this.has(REROLL_HIT_ROLL),
			weapon.has(TORRENT),
			this.has(ADD_ONE_TO_WOUND),
			enemy.has(SUBTRACT_ONE_FROM_WOUND_ROLL),
			this.has(REROLL_ONES_TO_WOUND),
			this.has(REROLL_WOUND_ROLL),
			weapon.getAntiType().isPresent(),
			enemy.has(HAS_COVER),
			this.has(IGNORE_COVER),
			weapon.getSustainedHits() > 0,
			weapon.getMelter() > 0,
			weapon.has(DEVASTATING_WOUNDS)
		);
	}

}

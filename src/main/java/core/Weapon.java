package core;

import java.util.HashSet;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

/**
 * The Weapons should just contain values
 * and be attached to units. This entity will be used for comparation
 * against the Profile entity.
 */
@Data @Generated @Builder
public class Weapon {
	//is double because we can use 2.5 as default for a d6
	private double attacks;
	//is double because we can directly take the hit chance from the profile
	private double toHit;
	private int strength;
	private int armorPenetration;
	private double damage;
	@Builder.Default private Phase phase = Phase.SHOOTING; 
	
	/**
	 * Distinguishes the weapon between a combat and a shooting weapon
	 */
	public enum Phase {
		SHOOTING,
		FIGHT
	}
	
	/**
	 * The Set of special rules each weapon can have
	 * it applies after the global unit special rules
	 */
	@Builder.Default
	private HashSet<SpecialRuleWeapon> specialRules = new HashSet<>();
	public enum SpecialRuleWeapon{
		TORRENT,
		HEAVY_AND_UNIT_REMAINED_STATIONARY,
		REROLL_WOUND_ROLL
	}
	
	public void add(SpecialRuleWeapon specialRule) {
		this.specialRules.add(specialRule);
	}
	
	public void remove(SpecialRuleWeapon specialRule) {
		this.remove(specialRule);
	}
	
	public boolean has(SpecialRuleWeapon specialRule) {
		return this.specialRules.contains(specialRule);
	}
	
	/**
	 * The Builder pattern that can be used to create Weapons
	 */
	public static class WeaponBuilder{
		public WeaponBuilder add(SpecialRuleWeapon specialRule) {
			var set = new HashSet<SpecialRuleWeapon>();
			set.add(specialRule);
			this.specialRules(set);
			return this;
		}
	}
}

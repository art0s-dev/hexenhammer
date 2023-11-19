package core;

import java.util.HashSet;
import java.util.Optional;

import core.Profile.Type;
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
	 * Describes the extra damage that is done 
	 * when this value is greater then 0
	 */
	@Builder.Default private int melta = 0;
	
	/**
	 * This is a tuple explains the specific efficiency for wound rolls
	 * against a certain profile type.
	 */
	public record AntiType(Type type, Double probability) {};
	@Builder.Default private Optional<AntiType> antiType = Optional.empty();
	
	/**
	 * Distinguishes the weapon between a combat and a shooting weapon 
	 */
	public enum Phase {
		SHOOTING,
		FIGHT,
		BOTH
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
		REROLL_WOUND_ROLL, 
		LETHAL_HITS
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
			set.addAll(this.build().getSpecialRules());
			this.specialRules(set);
			return this;
		}
		
		public WeaponBuilder setAntiType(Type type, double probability) {
			this.antiType(Optional.of(new AntiType(type, probability)));
			return this;
		}
	}
}

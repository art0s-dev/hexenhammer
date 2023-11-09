package core;

import java.util.HashSet;

import core.Weapon.SpecialRuleWeapon;
import core.Weapon.WeaponBuilder;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;

/**
 * This class just contains values for weapons
 * to compare against. The units that use a certain profile should be targets.
 */
@Data @Generated @Builder
public class Profile {
	private int toughness;
	private int wounds;
	private double armorSave;
	@Builder.Default private int hitPoints = 1;
	@Builder.Default private double invulnerableSave = Probability.NONE;
	@Builder.Default private double feelNoPain = Probability.NONE;
	
	/**
	 * The Set of special rules the profile can have
	 * this special rules do always apply
	 */
	@Builder.Default
	private HashSet<SpecialRuleProfile> specialRules = new HashSet<>();
	public enum SpecialRuleProfile{
		SUBSTRACT_ONE_FROM_HIT_ROLL
	}
	
	public void add(SpecialRuleProfile specialRule) {
		this.specialRules.add(specialRule);
	}
	
	public void remove(SpecialRuleProfile specialRule) {
		this.remove(specialRule);
	}
	
	public boolean has(SpecialRuleProfile specialRule) {
		return this.specialRules.contains(specialRule);
	}
	
	/**
	 * The Builder pattern that can be used to create Profiles
	 */
	public static class ProfileBuilder{
		public ProfileBuilder add(SpecialRuleProfile specialRule) {
			var set = new HashSet<SpecialRuleProfile>();
			set.add(specialRule);
			this.specialRules(set);
			return this;
		}
	}
}

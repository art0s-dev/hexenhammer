package core;

import java.util.HashSet;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

/**
 * This class just contains values for weapons to compare against. The units
 * that use a certain profile should be targets.
 */
@Data @Generated @Builder
public class Enemy {
	private byte toughness;
	private float armorSave;
	@Builder.Default private byte hitPoints = 1;
	@Builder.Default private float invulnerableSave = Probability.NONE;
	@Builder.Default private float feelNoPain = Probability.NONE;
	@Builder.Default private Type type = Type.INFANTRY;

	/**
	 * The Type a profile has. It is used to determine wounds for anti weapon
	 * capabilities.
	 */
	public enum Type {
		INFANTRY, MONSTER, VEHICLE
	}

	/**
	 * The Set of special rules the profile can have this special rules do always
	 * apply
	 */
	@Builder.Default
	private HashSet<SpecialRuleEnemy> specialRules = new HashSet<>();

	public enum SpecialRuleEnemy {
		SUBTRACT_ONE_FROM_HIT_ROLL, 
		SUBTRACT_ONE_FROM_WOUND_ROLL, 
		HAS_COVER
	}

	public void add(SpecialRuleEnemy specialRule) {
		this.specialRules.add(specialRule);
	}

	public void remove(SpecialRuleEnemy specialRule) {
		this.remove(specialRule);
	}

	public boolean has(SpecialRuleEnemy specialRule) {
		return this.specialRules.contains(specialRule);
	}

}

package core;

import java.util.HashSet;
import java.util.Optional;

import core.Unit.Type;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;
import lombok.Getter;

/**
 * The Weapons should just contain values
 * and be attached to units. This entity will be used for comparation 
 * against the Profile entity.
 */
@Data @Generated @Builder
public class Weapon {
	@Getter private String name;
	@Builder.Default private float attacks = 1;
	@Builder.Default private float toHit = Probability.SIX_UP;
	@Builder.Default private byte strength = 1;
	@Builder.Default private byte armorPenetration = 0;
	@Builder.Default private float damage = 1;
	@Builder.Default private byte sustainedHits = 0;
	@Builder.Default private Phase phase = Phase.SHOOTING; 
	
	/**
	 * Describes the extra damage that is done 
	 * when this value is greater then 0
	 */
	@Builder.Default private byte melter = 0; 
	
	/**
	 * This is a tuple explains the specific efficiency for wound rolls
	 * against a certain profile type.
	 */
	public record AntiType(Type type, Float probability) {}
	@Builder.Default private Optional<AntiType> antiType = Optional.empty();
	
	/**
	 * Distinguishes the weapon between a combat and a shooting weapon 
	 */
	public enum Phase { SHOOTING, FIGHT, BOTH }
	
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
		LETHAL_HITS,
		DEVASTATING_WOUNDS,
	}
	
	public void add(SpecialRuleWeapon specialRule) {
		this.specialRules.add(specialRule);
	}
	
	public void remove(SpecialRuleWeapon specialRule) {
		this.specialRules.remove(specialRule);
	}
	
	public boolean has(SpecialRuleWeapon specialRule) {
		return this.specialRules.contains(specialRule);
	}
}

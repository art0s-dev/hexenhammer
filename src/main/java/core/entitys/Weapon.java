package core.entitys;

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
	
	public enum Phase {
		SHOOTING,
		FIGHT
	}
}

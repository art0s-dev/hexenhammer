package unittests;

import core.Probability;
import core.Profile;
import core.Weapon;
import core.Weapon.SpecialRuleWeapon;

/**
 * The Weaponry just contains some static profiles
 * for the battle simulator to test
 */
public class Weaponry {
	
	public static Weapon flameThrower = Weapon.builder()
			.attacks(Probability.d6(1))
			.add(SpecialRuleWeapon.TORRENT)
			.strength(4)
			.armorPenetration(0)
			.damage(1)
			.build();
	
	public static Weapon bolter = Weapon.builder()
			.attacks(2)
			.toHit(Probability.THREE_UP)
			.strength(4)
			.armorPenetration(0)
			.damage(1)
			.build();
	
	public static Weapon heavyBolter = Weapon.builder()
			.attacks(3)
			.toHit(Probability.THREE_UP)
			.add(SpecialRuleWeapon.HEAVY_AND_UNIT_REMAINED_STATIONARY)
			.strength(5)
			.armorPenetration(2)
			.damage(2)
			.build();
	
	public static Profile guardsmen = Profile.builder()
			.toughness(3)
			.armorSave(Probability.FIVE_UP)
			.build();
	
	public static Profile eldarRangers = Profile.builder()
			.toughness(3)
			.armorSave(Probability.FIVE_UP)
			.invulnerableSave(Probability.FIVE_UP)
			.build();
	
	public static Profile otherSpaceMarines = Profile.builder()
			.toughness(4)
			.armorSave(Probability.THREE_UP)
			.hitPoints(2)
			.build();
	
	public static Profile abberants = Profile.builder()
			.toughness(6)
			.armorSave(Probability.FIVE_UP)
			.hitPoints(3)
			.feelNoPain(Probability.FOUR_UP)
			.build();
}

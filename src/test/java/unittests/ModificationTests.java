package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;

import core.Probability;
import core.Profile;
import core.Unit;
import core.Weapon;
import core.Weapon.SpecialRuleWeapon;

/**
 * We have a modification system in our battle calculator.
 * This modification system changes the situation probability of
 * our hit and wound rolls. This System should just modify +1 or -1
 * For instance if we have +1 to hit on a unit and -1 on a profile,
 * we expect no modifications and so on. In Order to Test this, 
 * we simulate a battle situation between a unit with many high volume
 * and damage shots against a profile with high tanking capabilities.
 * We do this, because from experience modifiers in extreme situations have
 * the most impact. We aggregate some Groups:
 */
class ModificationTests {
	
	static Unit scorpionTank = new Unit();
	static Weapon scorpionPulsar = Weapon.builder()
			.attacks(6)
			.toHit(Probability.THREE_UP)
			.strength(18)
			.armorPenetration(3)
			.damage(5)
			.add(SpecialRuleWeapon.REROLL_WOUND_ROLL)
			.build();
	
	Profile mortarion = Profile.builder()
			.toughness(12)
			.hitPoints(16)
			.armorSave(Probability.TWO_UP)
			.invulnerableSave(Probability.FOUR_UP)
			.feelNoPain(Probability.FIVE_UP)
			.build();
	
	@BeforeAll
	static void setup() {
		scorpionTank = new Unit();
		scorpionTank.equip(1, scorpionPulsar);
	}
	
	@Test
	void BaseCaseToAttackMortarion_WithoutModifiers() {
		double hits = 6 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		int missedSaves = (int) Math.floor(wounds * Probability.FOUR_UP);
		var damagePotential = missedSaves * scorpionPulsar.getDamage(); 
		var damageAfterFeelNoPain = damagePotential - (damagePotential * Probability.FIVE_UP);
		damageAfterFeelNoPain = Math.floor(damageAfterFeelNoPain);
		
		assertEquals(damageAfterFeelNoPain, scorpionTank.attack(mortarion));
	}

}

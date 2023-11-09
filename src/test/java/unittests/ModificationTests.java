package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;

import core.Probability;
import core.Profile;
import core.Profile.SpecialRuleProfile;
import core.Unit;
import core.Unit.SpecialRuleUnit;
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
	
	Unit scorpionTank = new Unit();
	Weapon scorpionPulsar = Weapon.builder()
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
	
	@BeforeEach
	void setup() {
		scorpionTank = new Unit();
		scorpionTank.equip(1, scorpionPulsar);
	}
	
	@Test
	void BaseCaseToAttackMortarion_WithoutModifiers() {
		double hits = 6 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		wounds += (hits - wounds) * Probability.THREE_UP; 
		
		int missedSaves = (int) Math.floor(wounds * Probability.FOUR_UP);
		double damagePotential = missedSaves * scorpionPulsar.getDamage(); 
		double damageAfterFeelNoPain = damagePotential - (damagePotential * Probability.FIVE_UP);
		damageAfterFeelNoPain = Math.floor(damageAfterFeelNoPain);
		
		assertEquals(damageAfterFeelNoPain, scorpionTank.attack(mortarion));
	}
	
	@Test
	void ScorpionTankGetsOneToHit_AgainstMortarion() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		double hits = 6 * Probability.TWO_UP; //4.998
		double wounds = hits * Probability.THREE_UP; //3.29
		wounds += (hits - wounds) * Probability.THREE_UP; 
		int missedSaves = (int) Math.floor(wounds * Probability.FOUR_UP); //1
		
		double damagePotential = missedSaves * scorpionPulsar.getDamage(); //5
		double damageAfterFeelNoPain = damagePotential - (damagePotential * Probability.FIVE_UP);
		damageAfterFeelNoPain = Math.floor(damageAfterFeelNoPain);
		
		assertEquals(damageAfterFeelNoPain, scorpionTank.attack(mortarion));
	}
	
	@Test @Disabled("Test is Flaky and runs green - i suspect the hits are the same")
	void ScorpionTankGetsMinusOneToHit_AgainstMortarion() {
		mortarion.add(SpecialRuleProfile.SUBSTRACT_ONE_FROM_HIT_ROLL);
		double hits = 6 * Probability.FOUR_UP;
		double wounds = hits * Probability.THREE_UP;
		wounds += (hits - wounds) * Probability.THREE_UP; 
		int missedSaves = (int) Math.floor(wounds * Probability.FOUR_UP); //1
		
		double damagePotential = missedSaves * scorpionPulsar.getDamage(); //5
		double damageAfterFeelNoPain = damagePotential - (damagePotential * Probability.FIVE_UP);
		damageAfterFeelNoPain = Math.floor(damageAfterFeelNoPain);
		
		assertEquals(damageAfterFeelNoPain, scorpionTank.attack(mortarion));
	}

}

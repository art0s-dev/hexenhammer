package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
	
	@BeforeEach
	void setup() {
		scorpionTank = new Unit();
		scorpionTank.equip(1, scorpionPulsar);
	}
	
	
	/**
	 * Base Case Test without modifiers 
	 */
	@Test
	void GivenIsAScorpionTank_WhenItAttacksMortarion_ThenTheCalculationIsCorrect() {		
		var damage = calculateScorpionDamage(Probability.THREE_UP,Probability.THREE_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	
	@Test
	void GivenScorpionTank_WhenPlusOneToHit_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		var damage = calculateScorpionDamage(Probability.TWO_UP,Probability.THREE_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	@Test
	void GivenScorpionTank_WhenSubtractToHit_ThenTheDamageIsLower() {
		mortarion.add(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL);
		var damage = calculateScorpionDamage(Probability.FOUR_UP,Probability.THREE_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	@Test 
	void GivenScorpionBothModifiers_WhenItAttacks_ThenDamageIsSameAsBaseCase() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		mortarion.add(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL);
		var damage = calculateScorpionDamage(Probability.THREE_UP,Probability.THREE_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	@Test
	void GivenScorpionTank_WhenPlusOneToWound_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		var damage = calculateScorpionDamage(Probability.THREE_UP,Probability.TWO_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	@Test
	void GivenScorpionTank_WhenSubtractToWound_ThenTheDamageIsLower() {
		mortarion.add(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL);
		var damage = calculateScorpionDamage(Probability.THREE_UP, Probability.FOUR_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	@Test
	void GivenScorpionTank_WhenBothModifiersApply_ThenTheDamageIsSameAsBaseCase() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		mortarion.add(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL);
		var damage = calculateScorpionDamage(Probability.THREE_UP,Probability.THREE_UP);
		assertEquals(damage, scorpionTank.attack(mortarion));
	}
	
	private double calculateScorpionDamage(double toHit, double toWound) {
		double hits = scorpionPulsar.getAttacks() * toHit;
		double wounds = hits * toWound;
		wounds += (hits - wounds) * toWound; 
		int missedSaves = (int) Math.floor(wounds * Probability.FOUR_UP);
		
		double damagePotential = missedSaves * scorpionPulsar.getDamage();
		double damageAfterFeelNoPain = damagePotential - (damagePotential * Probability.FIVE_UP);
		return damageAfterFeelNoPain = Math.floor(damageAfterFeelNoPain);
	}
	
	Unit scorpionTank = new Unit();
	
	/**
	 * We exaggerate The attacks of the weapon so we dont fail the tests in
	 * the math.floor() > 1 spectrum. 
	 */
	Weapon scorpionPulsar = Weapon.builder()
			.attacks(100)
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

}

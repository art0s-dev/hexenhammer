package core.unit;

import static core.Probability.modifyRoll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
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
 * - +1 / no mod / -1  |  for hit and wound rolls
 */
class ModificationTests {
	
	
	Weapon scorpionPulsar;
	Profile mortarion;
	Unit scorpionTank = new Unit();
	
	@BeforeEach
	void setup() {
		scorpionPulsar = mock(Weapon.class);
		when(scorpionPulsar.getAttacks()).thenReturn(5.00);
		when(scorpionPulsar.getToHit()).thenReturn(Probability.THREE_UP);
		when(scorpionPulsar.getStrength()).thenReturn(18);
		when(scorpionPulsar.getArmorPenetration()).thenReturn(5);
		when(scorpionPulsar.getDamage()).thenReturn(5.00);
		when(scorpionPulsar.has(SpecialRuleWeapon.REROLL_WOUND_ROLL)).thenReturn(true);
		scorpionTank.equip(1, scorpionPulsar);
		
		mortarion = mock(Profile.class);
		when(mortarion.getToughness()).thenReturn(12);
		when(mortarion.getHitPoints()).thenReturn(16);
		when(mortarion.getArmorSave()).thenReturn(Probability.TWO_UP);
		when(mortarion.getInvulnerableSave()).thenReturn(Probability.FOUR_UP);
		when(mortarion.getFeelNoPain()).thenReturn(Probability.FIVE_UP);
	}
	
	/**
	 * This Test covers the functionality of our roll modification
	 * The modification API allows just one modification (+1 or -1) at a time
	 */
	@Test @DisplayName("Mod test")
	void mofifyRollTest() {
		//Test the edge cases
		assertEquals(modifyRoll(7526.88, '+'), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88, '+'), Probability.SIX_UP);
		assertEquals(modifyRoll(7526.88, '-'), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(0, '+'), Probability.SIX_UP);
		assertEquals(modifyRoll(0, '?'), Probability.SIX_UP);
		assertEquals(modifyRoll(2, '?'), Probability.TWO_UP);
		
		//Test the main cases for positive modification
		assertEquals(modifyRoll(Probability.SIX_UP, '+'), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, '+'), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, '+'), Probability.THREE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, '+'), Probability.TWO_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, '+'), Probability.TWO_UP);
		
		//Test the main cases for negative modification
		assertEquals(modifyRoll(Probability.SIX_UP, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, '-'), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, '-'), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, '-'), Probability.THREE_UP);
	}
	
	@Test @DisplayName("Mod test / +1 hit ")
	void GivenScorpionTank_WhenPlusOneToHit_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		
		double expectedDamage = calculateScorpionDamage(Probability.TWO_UP,Probability.THREE_UP);
		double damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 hit ")
	void GivenScorpionTank_WhenSubtractToHit_ThenTheDamageIsLower() {
		when(mortarion.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		double expectedDamage = calculateScorpionDamage(Probability.FOUR_UP,Probability.THREE_UP);
		double damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / no mods ")
	void GivenScorpionBothModifiers_WhenItAttacks_ThenDamageIsSameAsBaseCase() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		when(mortarion.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		double expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.THREE_UP);
		double damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / +1 wound ") 
	void GivenScorpionTank_WhenPlusOneToWound_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		
		double expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.TWO_UP);
		double damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 wound ")
	void GivenScorpionTank_WhenSubtractToWound_ThenTheDamageIsLower() {
		when(mortarion.has(SpecialRuleProfile.SUBTRACT_ONE_FROM_WOUND_ROLL)).thenReturn(true);
		
		double expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.FOUR_UP);
		double damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	private double calculateScorpionDamage(double toHit, double toWound) {
		double hits = scorpionPulsar.getAttacks() * toHit;
		double wounds = hits * toWound;
		wounds += (hits - wounds) * toWound; 
		double missedSaves = wounds * Probability.FOUR_UP;
		
		double damagePotential = missedSaves * scorpionPulsar.getDamage();
		return damagePotential - (damagePotential * Probability.FIVE_UP);
	}
	
}

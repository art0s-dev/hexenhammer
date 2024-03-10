package integration.core;

import static core.Probability.modifyRoll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import core.Weapon;
import core.Weapon.SpecialRuleWeapon;
import lombok.val;

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
@TestMethodOrder(MethodOrderer.Random.class)
class ModificationTests {
	
	
	Weapon scorpionPulsar;
	Unit mortarion;
	Unit scorpionTank = Unit.builder().build();
	
	@BeforeEach
	void setup() {
		scorpionPulsar = mock(Weapon.class);
		when(scorpionPulsar.getAttacks()).thenReturn(5f);
		when(scorpionPulsar.getToHit()).thenReturn(Probability.THREE_UP);
		when(scorpionPulsar.getStrength()).thenReturn((byte)18);
		when(scorpionPulsar.getArmorPenetration()).thenReturn((byte)5);
		when(scorpionPulsar.getDamage()).thenReturn(5f);
		when(scorpionPulsar.has(SpecialRuleWeapon.REROLL_WOUND_ROLL)).thenReturn(true);
		scorpionTank.equip((byte)1, scorpionPulsar);
		
		mortarion = mock(Unit.class);
		when(mortarion.getToughness()).thenReturn((byte)12);
		when(mortarion.getHitPoints()).thenReturn((byte)16);
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
		val plusOne = Probability.Modifier.PLUS_ONE;
		val minusOne = Probability.Modifier.MINUS_ONE;
		
		//Test the edge cases
		assertEquals(modifyRoll(7526.88f, plusOne), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88f, plusOne), Probability.SIX_UP);
		assertEquals(modifyRoll(7526.88f, minusOne), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88f, minusOne), Probability.SIX_UP);
		assertEquals(modifyRoll(0, plusOne), Probability.SIX_UP);
		
		//Test the main cases for positive modification
		assertEquals(modifyRoll(Probability.SIX_UP, plusOne), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, plusOne), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, plusOne), Probability.THREE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, plusOne), Probability.TWO_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, plusOne), Probability.TWO_UP);
		
		//Test the main cases for negative modification
		assertEquals(modifyRoll(Probability.SIX_UP, minusOne), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, minusOne), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, minusOne), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, minusOne), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, minusOne), Probability.THREE_UP);
	}
	
	@Test @DisplayName("Mod test / +1 hit ")
	void GivenScorpionTank_WhenPlusOneToHit_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		
		float expectedDamage = calculateScorpionDamage(Probability.TWO_UP,Probability.THREE_UP);
		float damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 hit ")
	void GivenScorpionTank_WhenSubtractToHit_ThenTheDamageIsLower() {
		when(mortarion.has(SpecialRuleUnit.SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(Probability.FOUR_UP,Probability.THREE_UP);
		float damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / no mods ")
	void GivenScorpionBothModifiers_WhenItAttacks_ThenDamageIsSameAsBaseCase() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		when(mortarion.has(SpecialRuleUnit.SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.THREE_UP);
		float damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / +1 wound ") 
	void GivenScorpionTank_WhenPlusOneToWound_ThenTheDamageIsHigher() {
		scorpionTank.add(SpecialRuleUnit.ADD_ONE_TO_WOUND);
		
		float expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.TWO_UP);
		float damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 wound ")
	void GivenScorpionTank_WhenSubtractToWound_ThenTheDamageIsLower() {
		when(mortarion.has(SpecialRuleUnit.SUBTRACT_ONE_FROM_WOUND_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(Probability.THREE_UP,Probability.FOUR_UP);
		float damage = scorpionTank.attack(mortarion);
		
		assertEquals(expectedDamage, damage);
	}
	
	private float calculateScorpionDamage(float toHit, float toWound) {
		float hits = scorpionPulsar.getAttacks() * toHit;
		float wounds = hits * toWound;
		wounds += (hits - wounds) * toWound; 
		float missedSaves = wounds * Probability.FOUR_UP;
		
		float damagePotential = missedSaves * scorpionPulsar.getDamage();
		return damagePotential - (damagePotential * Probability.FIVE_UP);
	}
	
}

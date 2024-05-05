package integration.core;

import static core.CombatResult.getOverallDamage;
import static core.Probability.FIVE_UP;
import static core.Probability.FOUR_UP;
import static core.Probability.SIX_UP;
import static core.Probability.THREE_UP;
import static core.Probability.TWO_UP;
import static core.Probability.modifyRoll;
import static core.Probability.Modifier.MINUS_ONE;
import static core.Probability.Modifier.PLUS_ONE;
import static core.Unit.SpecialRuleUnit.ADD_ONE_TO_HIT;
import static core.Unit.SpecialRuleUnit.ADD_ONE_TO_WOUND;
import static core.Unit.SpecialRuleUnit.REROLL_WOUND_ROLL;
import static core.Unit.SpecialRuleUnit.SUBTRACT_ONE_FROM_HIT_ROLL;
import static core.Unit.SpecialRuleUnit.SUBTRACT_ONE_FROM_WOUND_ROLL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Unit;
import core.Weapon;
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
		when(scorpionPulsar.getToHit()).thenReturn(THREE_UP);
		when(scorpionPulsar.getStrength()).thenReturn(18);
		when(scorpionPulsar.getArmorPenetration()).thenReturn(5);
		when(scorpionPulsar.getDamage()).thenReturn(5f);
		scorpionTank.add(REROLL_WOUND_ROLL);
		scorpionTank.equip((byte)1, scorpionPulsar);
		
		mortarion = mock(Unit.class);
		when(mortarion.getToughness()).thenReturn(12);
		when(mortarion.getHitPoints()).thenReturn(16);
		when(mortarion.getArmorSave()).thenReturn(TWO_UP);
		when(mortarion.getInvulnerableSave()).thenReturn(FOUR_UP);
		when(mortarion.getFeelNoPain()).thenReturn(FIVE_UP);
	}
	
	/**
	 * This Test covers the functionality of our roll modification
	 * The modification API allows just one modification (+1 or -1) at a time
	 */
	@Test @DisplayName("Mod test")
	void mofifyRollTest() {
		val plusOne = PLUS_ONE;
		val minusOne = MINUS_ONE;
		
		//Test the edge cases
		assertEquals(modifyRoll(7526.88f, plusOne), TWO_UP);
		assertEquals(modifyRoll(-7526.88f, plusOne), SIX_UP);
		assertEquals(modifyRoll(7526.88f, minusOne), TWO_UP);
		assertEquals(modifyRoll(-7526.88f, minusOne), SIX_UP);
		assertEquals(modifyRoll(0, plusOne), SIX_UP);
		
		//Test the main cases for positive modification
		assertEquals(modifyRoll(SIX_UP, plusOne), FIVE_UP);
		assertEquals(modifyRoll(FIVE_UP, plusOne), FOUR_UP);
		assertEquals(modifyRoll(FOUR_UP, plusOne), THREE_UP);
		assertEquals(modifyRoll(THREE_UP, plusOne), TWO_UP);
		assertEquals(modifyRoll(TWO_UP, plusOne), TWO_UP);
		
		//Test the main cases for negative modification
		assertEquals(modifyRoll(SIX_UP, minusOne), SIX_UP);
		assertEquals(modifyRoll(FIVE_UP, minusOne), SIX_UP);
		assertEquals(modifyRoll(FOUR_UP, minusOne), FIVE_UP);
		assertEquals(modifyRoll(THREE_UP, minusOne), FOUR_UP);
		assertEquals(modifyRoll(TWO_UP, minusOne), THREE_UP);
	}
	
	@Test @DisplayName("Mod test / +1 hit ")
	void GivenScorpionTank_WhenPlusOneToHit_ThenTheDamageIsHigher() {
		scorpionTank.add(ADD_ONE_TO_HIT);
		
		float expectedDamage = calculateScorpionDamage(TWO_UP,THREE_UP);
		float damage = getOverallDamage(scorpionTank.attack(mortarion));
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 hit ")
	void GivenScorpionTank_WhenSubtractToHit_ThenTheDamageIsLower() {
		when(mortarion.has(SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(FOUR_UP,THREE_UP);
		float damage = getOverallDamage(scorpionTank.attack(mortarion));
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / no mods ")
	void GivenScorpionBothModifiers_WhenItAttacks_ThenDamageIsSameAsBaseCase() {
		scorpionTank.add(ADD_ONE_TO_HIT);
		when(mortarion.has(SUBTRACT_ONE_FROM_HIT_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(THREE_UP,THREE_UP);
		float damage = getOverallDamage(scorpionTank.attack(mortarion));
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / +1 wound ") 
	void GivenScorpionTank_WhenPlusOneToWound_ThenTheDamageIsHigher() {
		scorpionTank.add(ADD_ONE_TO_WOUND);
		
		float expectedDamage = calculateScorpionDamage(THREE_UP,TWO_UP);
		float damage = getOverallDamage(scorpionTank.attack(mortarion));
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Mod test / -1 wound ")
	void GivenScorpionTank_WhenSubtractToWound_ThenTheDamageIsLower() {
		when(mortarion.has(SUBTRACT_ONE_FROM_WOUND_ROLL)).thenReturn(true);
		
		float expectedDamage = calculateScorpionDamage(THREE_UP,FOUR_UP);
		float damage = getOverallDamage(scorpionTank.attack(mortarion));
		
		assertEquals(expectedDamage, damage);
	}
	
	private float calculateScorpionDamage(float toHit, float toWound) {
		float hits = scorpionPulsar.getAttacks() * toHit;
		float wounds = hits * toWound;
		wounds += (hits - wounds) * toWound; 
		float missedSaves = wounds * FOUR_UP;
		
		float damagePotential = missedSaves * scorpionPulsar.getDamage();
		return damagePotential - (damagePotential * FIVE_UP);
	}
	
}

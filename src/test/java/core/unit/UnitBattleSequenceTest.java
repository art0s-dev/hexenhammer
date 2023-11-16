/**
 * 
 */
package core.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Profile;
import core.Unit;
import core.Weapon;
import core.Weapon.SpecialRuleWeapon;
import lombok.val;


/**
 * These are our battle simulations to test the unit API.
 * The Test cases ensure that the attack sequence is ruled correctly
 */
@TestMethodOrder(MethodOrderer.Random.class)
class UnitBattleSequenceTest {
	
	/**
	 * This is the base test case for our attack api.
	 */
	@Test @DisplayName("Base Case")
	void GivenSpaceMarinesEquippedWithBolter_WhenAttackingGuardsmen_ThenDamageIsCalculatedCorrectly() {
		//Setup
		Weapon bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2.00);
		when(bolter.getStrength()).thenReturn(4);
		when(bolter.getArmorPenetration()).thenReturn(0);
		when(bolter.getDamage()).thenReturn(1.00);	
		when(bolter.getToHit()).thenReturn(Probability.THREE_UP);
		
		Profile guardsmen = mock(Profile.class);
		when(guardsmen.getToughness()).thenReturn(3);
		when(guardsmen.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn(1);
		
		Unit spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		int damage = spaceMarines.attack(guardsmen);
		
		//Expectation
		int attacks = 5 * 2;
		double hits = attacks * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		int expectedDamage = (int) Math.floor(wounds - (wounds * Probability.FIVE_UP));
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * Here we shall test, if the dodge save of our rangers will be taken
	 * instead of the armor save
	 */
	@Test @DisplayName("Invul Save")
	void GivenEldarRangerAsTarget_WhenArmorSaveIsLoweThanInvulSave_ThenTakeInvulSave() {
		Weapon heavyBolter = mock(Weapon.class);
		when(heavyBolter.getAttacks()).thenReturn(3.00);
		when(heavyBolter.getToHit()).thenReturn(Probability.THREE_UP);
		when(heavyBolter.getStrength()).thenReturn(5);
		when(heavyBolter.getArmorPenetration()).thenReturn(2);
		
		Profile eldarRangers = mock(Profile.class);
		when(eldarRangers.getToughness()).thenReturn(3);
		when(eldarRangers.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(eldarRangers.getInvulnerableSave()).thenReturn(Probability.FIVE_UP);
		
		Unit spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		double damage = spaceMarines.attack(eldarRangers);
		
		double hits = 12 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		int missedSaves = (int) Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		int expectedDamage = missedSaves * eldarRangers.getHitPoints();
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here We test the multi wounded Damage mechanic. The goal here is
	 * that exess damage after the unit has died cannot 
	 */
	
	
	
	
	
	
}

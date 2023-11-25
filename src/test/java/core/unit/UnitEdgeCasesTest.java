package core.unit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Profile;
import core.Unit;
import core.Weapon;
import core.Weapon.SpecialRuleWeapon;

/**
 * This Testclass incorporates all edge cases that come with combining the 
 * different feature described in the unit attack features test
 */
@TestMethodOrder(MethodOrderer.Random.class)
class UnitEdgeCasesTest {
	
	Weapon bolter;
	Weapon heavyBolter;
	Profile guardsmen;
	Profile aberrants;
	Profile lemanRussTank;
	
	@BeforeEach
	void setup() {
		bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2.00);
		when(bolter.getStrength()).thenReturn(4);
		when(bolter.getArmorPenetration()).thenReturn(0);
		when(bolter.getDamage()).thenReturn(1.00);	
		when(bolter.getToHit()).thenReturn(Probability.THREE_UP);
		
		guardsmen = mock(Profile.class);
		when(guardsmen.getToughness()).thenReturn(3);
		when(guardsmen.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn(1);
	}

	/**
	 * Let's test how we can combine lethal hits and sustained hits.
	 * We assume exploding 6es => more hits + autowounds
	 */
	@Test @DisplayName("Lethal hits & Sustained hits") @Disabled("Other tests needed to be finished before")
	void GivenSpaceMarines_WhenAddingLethalHitsAndSustainedHits_ThenCreateAdditionalHitsAndAutowounds() {
		//when(bolter.has(SpecialRuleWeapon.SUSTAINED_HITS)).thenReturn(true);
		when(bolter.has(SpecialRuleWeapon.LETHAL_HITS)).thenReturn(true);
		
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		double damage = spaceMarines.attack(guardsmen);
		
		//double hits = (quantity * bolter.getAttacks()) * Probability
	}

}

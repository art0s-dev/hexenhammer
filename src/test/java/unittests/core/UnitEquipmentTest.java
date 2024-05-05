package unittests.core;

import static core.CombatResult.getOverallDamage;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Unit;
import core.Weapon;

/**
 * These are the test cases for our equipment api
 */
@TestMethodOrder(MethodOrderer.Random.class)
class UnitEquipmentTest {
	
	Weapon bolter;
	Unit guardsmen;
	
	@BeforeEach
	void setup() {
		bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2f);
		when(bolter.getStrength()).thenReturn(4);
		when(bolter.getArmorPenetration()).thenReturn(0);
		when(bolter.getDamage()).thenReturn(1f);	
		when(bolter.getToHit()).thenReturn(Probability.THREE_UP);
		
		guardsmen = mock(Unit.class);
		when(guardsmen.getToughness()).thenReturn(3);
		when(guardsmen.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn(1);
	}

	@Test
	void testAddEquipment() {
		Unit unit = Unit.builder().build();
		float initialDamage = getOverallDamage(unit.attack(guardsmen));
		unit.equip(5, bolter);
		assertTrue(getOverallDamage(unit.attack(guardsmen)) > initialDamage);
	}
	
	@Test
	void testDecreaseEquipment() {
		Unit unit = Unit.builder().build();
		unit.equip(5, bolter);
		float initialDamage = getOverallDamage(unit.attack(guardsmen));
		unit.unequip(0, 4);
		assertTrue(getOverallDamage(unit.attack(guardsmen)) < initialDamage);
	}
	
	@Test
	void testDecreaseEquipmentThatIsNotThere() {
		Unit unit = Unit.builder().build();
		unit.equip(5, bolter);
		float initialDamage = getOverallDamage(unit.attack(guardsmen));
		unit.unequip(1, 4);
		assertTrue(getOverallDamage(unit.attack(guardsmen)) == initialDamage);
	}
	
	@Test
	void deleteEquipment() {
		Unit unit = Unit.builder().build();
		unit.equip(5, bolter);
		unit.unequip(0, 0);
		assertTrue(getOverallDamage(unit.attack(guardsmen)) == 0);
	}
	
	
	


}

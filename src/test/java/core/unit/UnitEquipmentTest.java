package core.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Profile;
import core.Unit;
import core.Weapon;

/**
 * These are the test cases for our equipment api
 */
@TestMethodOrder(MethodOrderer.Random.class)
class UnitEquipmentTest {
	
	Weapon bolter;
	Profile guardsmen;
	
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

	@Test @DisplayName("Add Equipment") 
	void GivenSpaceMarines_WhenEquippedWithBolter_ThenDamageIncreases(){	
		Unit unit = new Unit();
		unit.equip(5, bolter);
		
		assertTrue(unit.attack(guardsmen) > 0);
	}
	
	@Test @DisplayName("No Equipment")
	void GivenSpaceMarines_WhenNoEquipment_ThenThereIsNoDamage() {	
		Unit unit = new Unit();
		
		assertEquals(0.00, unit.attack(guardsmen));
	}
	
	@Test @DisplayName("Remove Equipment") 
	void GivenSpaceMarines_WhenEquipmentIsRemoves_ThenThereIsNoDamage(){	
		Unit unit = new Unit();
		unit.equip(5, bolter);
		unit.equip(-9999999, bolter);
		
		assertEquals(0.00, unit.attack(guardsmen));
	}
	
	@Test @DisplayName("Increase Equipment") 
	void GivenSpaceMarines_WhenEquipmentIsIncreased_ThenTheDamageIncreases(){	
		Unit unit = new Unit();
		unit.equip(5, bolter);
		double initialDamage = unit.attack(guardsmen);
		unit.equip(5, bolter);
		
		assertTrue(unit.attack(guardsmen) > initialDamage);
	}

}

package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.entitys.Probability;
import core.entitys.Profile;
import core.entitys.Unit;
import core.entitys.Unit.Equipment;
import core.entitys.Weapon;

class UnitTest {
	
	private Weapon bolter = Weapon.builder()
			.attacks(2)
			.toHit(Probability.THREE_UP)
			.strength(4)
			.armorPenetration(0)
			.damage(1)
			.build();
	
	private Profile guardsmen = Profile.builder()
			.toughness(3)
			.armorSave(Probability.FIVE_UP)
			.build();
	
	private Equipment testEquipment = new Equipment(bolter, 1);

	@Test @Disabled
	void WhenUnitWithNoWeaponAttacks_ItMakesNoDamage() {
		var unit = new Unit();
		double damage = unit.attack(guardsmen);
		assertEquals(damage, 0.00);
	}
	
	@Test
	void modelJoinsAUnit() {
		var unit = new Unit();
		unit.add(testEquipment);
		assertTrue(getWeapons(unit).size() > 0);
	} 
	
	@Test
	void modelLeavesAUnit() {
		var unit = new Unit();
		int key = unit.add(testEquipment);
		unit.remove(key);
		assertEquals(getWeapons(unit).size(), 0);
	}
	
	@Test
	void allModelsHaveLeftTheUnit_thenTheReferencesToTheModelsGetNull() {
		var unit = new Unit();
		int key1 = unit.add(testEquipment);
		unit.remove(key1);
		
		assertEquals(getWeapons(unit).get(key1), null);
	}
	
	@Test
	void someModelsJoin_someModelsLeave_KeysStayTheSame() {
		//This is for later usage in the gui so we can edit 
		//The unit profiles directly and operate on the
		//data structure more direct
		
		var unit = new Unit();
		//The Index starts couting from 0
		int key1 = unit.add(testEquipment);
		int key3 = unit.add(testEquipment);
		unit.remove(key1);
		unit.remove(key3);
		int key4 = unit.add(testEquipment);
		
		assertEquals(getWeapons(unit).get(0), null);
		assertEquals(2, key4);
	}
	
	@Test
	void whenUnitHasSomeWeapons_AndAttacksATarget_ItMakesDamage() {
		//So lets say we have a unit with a simple space marine 
		//This marine shall attack a guardsman
		var spaceMarines = new Unit();
		spaceMarines.add(new Equipment(bolter, 5));
		double damage = spaceMarines.attack(guardsmen);
		//10 Attacks with 3+ to hit, 3+ to wound and a 5up save
		double hits = 10 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds * Probability.FIVE_UP; 
		assertEquals(expectedDamage, damage);
	}
	
	private HashMap<Integer, Equipment> getWeapons(Unit unit) {
		try {
			var field = Unit.class.getDeclaredField("weapons");
	        field.setAccessible(true); 
	        var attributeValue = (HashMap<Integer, Equipment>) field.get(unit);
	        field.setAccessible(false);
	        return attributeValue;
		} catch (Exception e) {
			return null;
		}
    }
}

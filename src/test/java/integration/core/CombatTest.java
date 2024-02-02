package integration.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

class CombatTest {

	@Test
	void testFullCombatMechanic() {
		Weapon bolter = Weapon.builder()
				.toHit(Probability.THREE_UP)
				.attacks(2)
				.strength((byte)4)
				.build();
		
		Enemy guardsmen = Enemy.builder()
				.toughness((byte)3)
				.armorSave(Probability.FIVE_UP)
				.build();
		
		val total = (byte) 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(total, bolter);
		
		val hits = (bolter.getAttacks() * total) * bolter.getToHit();
		val wounds = hits * Probability.THREE_UP;
		val expectedDamage = wounds - (wounds * guardsmen.getArmorSave());
		
		assertEquals(expectedDamage, spaceMarines.attack(guardsmen));
	}

}

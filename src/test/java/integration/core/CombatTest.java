package integration.core;

import static core.Probability.FIVE_UP;
import static core.Probability.THREE_UP;
import static core.UserNumberInput.withNumber;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

class CombatTest {

	@Test
	void testFullCombatMechanic() {
		Weapon bolter = Weapon.builder()
				.toHit(THREE_UP)
				.attackInput(of(withNumber(2)))
				.damageInput(of(withNumber(1)))
				.strength(4)
				.build();
		
		Unit guardsmen = Unit.builder()
				.toughness((byte)3)
				.armorSave(FIVE_UP)
				.build();
		
		val total = (byte) 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(total, bolter);
		
		val hits = (bolter.getAttacks() * total) * bolter.getToHit();
		val wounds = hits * Probability.THREE_UP;
		val expectedDamage = wounds - (wounds * guardsmen.getArmorSave());
		
		assertEquals(expectedDamage, spaceMarines.attack(guardsmen));
	}

}

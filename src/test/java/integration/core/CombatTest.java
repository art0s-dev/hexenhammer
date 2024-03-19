package integration.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import core.Probability;
import core.Unit;
import core.UserNumberInput;
import core.Weapon;
import lombok.val;

class CombatTest {

	@Test
	void testFullCombatMechanic() {
		Weapon bolter = Weapon.builder()
				.toHit(Probability.THREE_UP)
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.strength((byte)4)
				.build();
		
		Unit guardsmen = Unit.builder()
				.toughness((byte)3)
				.armorSave(Probability.FIVE_UP)
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

package unittests.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Probability;
import core.Unit;
import core.Unit.Phase;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;

class WeaponDamagePhaseApiTest {
	
	Weapon bolter;
	Weapon chainsword;
	Unit enemyImperialGuard;
	
	@BeforeEach
	void setup() {
		bolter = Weapon.builder()
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.strength((byte)4)
				.armorPenetration((byte) 0)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.range(Range.SHOOTING)
				.build();
		
		chainsword = Weapon.builder()
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.strength((byte)4)
				.armorPenetration((byte) 0)
				.range(Range.MELEE)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.build();
		
		enemyImperialGuard = Unit.builder()
				.toughness((byte) 3)
				.hitPoints((byte) 1)
				.armorSave(Probability.FIVE_UP)
				.build();	
	}
	
	@Test
	void testOnlyShootingWeaponsEquipped_whenMeeleeIsPicked_thenNoDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip((byte)5, bolter);
		spaceMarines.usePhase(Phase.FIGHT);
		assertEquals(0f, spaceMarines.attack(enemyImperialGuard));
	}
	
	@Test
	void testOnlyMeeleWeaponsEquipped_whenShootingIsPicked_thenNoDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip((byte)5, chainsword);
		spaceMarines.usePhase(Phase.SHOOTING);
		assertEquals(0f, spaceMarines.attack(enemyImperialGuard));
	}
	
	@Test
	void testMeleeWeaponEquipped_whenMeleeIsSelectes_thenDealDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip((byte)5, chainsword);
		spaceMarines.usePhase(Phase.FIGHT);
		assertTrue(spaceMarines.attack(enemyImperialGuard) > 0f);
	}

}

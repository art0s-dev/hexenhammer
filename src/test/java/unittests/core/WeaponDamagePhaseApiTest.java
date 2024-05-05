package unittests.core;

import static core.CombatResult.getOverallDamage;
import static core.Probability.FIVE_UP;
import static core.Unit.Phase.FIGHT;
import static core.UserNumberInput.withNumber;
import static core.Weapon.Range.MELEE;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Unit;
import core.Unit.Phase;
import core.Weapon;
import core.Weapon.Range;

class WeaponDamagePhaseApiTest {
	
	Weapon bolter;
	Weapon chainsword;
	Unit enemyImperialGuard;
	
	@BeforeEach
	void setup() {
		bolter = Weapon.builder()
				.attackInput(of(withNumber( 2)))
				.strength(4)
				.armorPenetration(0)
				.damageInput(of(withNumber(1)))
				.range(Range.SHOOTING)
				.build();
		
		chainsword = Weapon.builder()
				.attackInput(of(withNumber(2)))
				.strength(4)
				.armorPenetration(0)
				.range(MELEE)
				.damageInput(of(withNumber(1)))
				.build();
		
		enemyImperialGuard = Unit.builder()
				.toughness(3)
				.hitPoints(1)
				.armorSave(FIVE_UP)
				.build();	
	}
	
	@Test
	void testOnlyShootingWeaponsEquipped_whenMeeleeIsPicked_thenNoDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip(5, bolter);
		spaceMarines.usePhase(FIGHT);
		assertEquals(0f, getOverallDamage(spaceMarines.attack(enemyImperialGuard)));
	}
	
	@Test
	void testOnlyMeeleWeaponsEquipped_whenShootingIsPicked_thenNoDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip(5, chainsword);
		spaceMarines.usePhase(Phase.SHOOTING);
		assertEquals(0f, getOverallDamage(spaceMarines.attack(enemyImperialGuard)));
	}
	
	@Test
	void testMeleeWeaponEquipped_whenMeleeIsSelectes_thenDealDamage() {
		Unit spaceMarines = Unit.builder()
				.build();
		
		spaceMarines.equip(5, chainsword);
		spaceMarines.usePhase(FIGHT);
		assertTrue(getOverallDamage(spaceMarines.attack(enemyImperialGuard)) > 0f);
	}

}

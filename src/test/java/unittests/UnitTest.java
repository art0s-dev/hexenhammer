package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.entitys.Probability;
import core.entitys.Profile;
import core.entitys.Unit;
import core.entitys.Unit.SpecialRuleUnit;
import core.entitys.Weapon;

class UnitTest {
	
	/**
	 * We test the equipment of the units,
	 * we arm a space marine unit with bolters and withdraw the bolters
	 */
	@Test
	void spaceMarinesWithoutWeapons_against_guardsmen() {
		var spaceMarines = new Unit();
		spaceMarines.equip(2, bolter);
		spaceMarines.equip(3, bolter);
		spaceMarines.equip(-5, bolter);
		spaceMarines.equip(1, bolter); //one bolter should't do much damage
		assertEquals(0, spaceMarines.attack(guardsmen));
	}
	
	/**
	 * This Test shall just test the simple combat mechanic
	 * of hit, wound and damage
	 */
	@Test
	void spaceMarines_against_guardsmen() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		double damage = spaceMarines.attack(guardsmen);
		//10 Attacks with 3+ to hit, 3+ to wound and a 5up save
		double hits = 10 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we shall test, if the dodge save of our rangers will be taken
	 * instead of the armor save
	 */
	@Test
	void spaceMarines_against_eldarRangers() {	
		var spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		double damage = spaceMarines.attack(eldarRangers);
		double hits = 12 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double missedSaves = Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		double expectedDamage = missedSaves * eldarRangers.getHitPoints();
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test the damage - we expect the maximum number 
	 * of damage to be equal to the number of hitpoints * missed Saves
	 */
	@Test
	void spaceMarines_against_other_spaceMarines() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		double damage = spaceMarines.attack(otherSpaceMarines);
		double hits = 12 * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double failedSaves = wounds - (wounds * Probability.FIVE_UP); 
		int hitPoints = otherSpaceMarines.getHitPoints();
		double expectedDamage = Math.floor(failedSaves) * hitPoints;
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * On this one we test the wound migitation of feel no pain.
	 * we want to halve the incoming damage
	 */
	@Test
	void spaceMarines_verus_abberants() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		double damage = spaceMarines.attack(abberants);
		double hits = 10 * Probability.THREE_UP;
		double wounds = hits * Probability.FIVE_UP;
		double missedSaves = Math.floor(wounds - (wounds * Probability.FIVE_UP));
		//Feel no pain wound migitation
		double expectedDamage = missedSaves * Probability.FOUR_UP;
		assertEquals(0, damage);
	}
	
	/**
	 * The Space marines have braught reinforcements!
	 * A special rule allows our models to reroll ones on the hit roll
	 */
	@Test
	void SpaceMarinesWithACaptian_AttackGuardsmen_RerollOnesToHit() {
		var spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_HIT);
		spaceMarines.equip(5, bolter);
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var missedHits = 10 - hits;
		//We reroll only ones
		hits += (missedHits / 6) * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var expectedDamage = Math.floor(wounds - (wounds * Probability.FIVE_UP));
		
		assertEquals(expectedDamage, damage);
	}
	
	Weapon bolter = Weapon.builder()
			.attacks(2)
			.toHit(Probability.THREE_UP)
			.strength(4)
			.armorPenetration(0)
			.damage(1)
			.build();
	
	Weapon heavyBolter = Weapon.builder()
			.attacks(3)
			.toHit(Probability.THREE_UP)
			.strength(5)
			.armorPenetration(2)
			.damage(2)
			.build();
	
	Profile guardsmen = Profile.builder()
			.toughness(3)
			.armorSave(Probability.FIVE_UP)
			.build();
	
	Profile eldarRangers = Profile.builder()
			.toughness(3)
			.armorSave(Probability.FIVE_UP)
			.invulnerableSave(Probability.FIVE_UP)
			.build();
	
	Profile otherSpaceMarines = Profile.builder()
			.toughness(4)
			.armorSave(Probability.THREE_UP)
			.hitPoints(2)
			.build();
	
	Profile abberants = Profile.builder()
			.toughness(6)
			.armorSave(Probability.FIVE_UP)
			.hitPoints(3)
			.feelNoPain(Probability.FOUR_UP)
			.build();
}

package unittests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static unittests.Weaponry.abberants;
import static unittests.Weaponry.bolter;
import static unittests.Weaponry.eldarRangers;
import static unittests.Weaponry.flameThrower;
import static unittests.Weaponry.guardsmen;
import static unittests.Weaponry.heavyBolter;
import static unittests.Weaponry.otherSpaceMarines;

import org.junit.jupiter.api.Test;

import core.Probability;
import core.Unit;
import core.Unit.SpecialRuleUnit;

/**
 * These are our battle simulations to test the unit API.
 * 
 */
class BattleSimulations {
	
	/**
	 * In our next case we bring something interesting intrducing the flameThrower
	 * the flamethrowers gain a quantity like w6 -> 2,5 and then autohit
	 */
	@Test
	void spaceMarines_withFlamethrowers_againstGuardsmen() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, flameThrower);
		var damage = spaceMarines.attack(guardsmen);
		var wounds = Probability.d6(4) * Probability.THREE_UP;
		double expectedDamage = Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In the next case we have brought fancy special rules with us
	 * that allow our space marines to reroll the whole wound roll.
	 */
	@Test
	void spaceMarines_withRerollWoundRoll_againstGuardsmen() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_WOUND_ROLL);
		var damage = spaceMarines.attack(guardsmen);
		var hits = 12 * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var rerolls = (hits - wounds) * Probability.THREE_UP;
		wounds += rerolls;
		
		double expectedDamage = Math.floor(wounds); 
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test
	void spaceMarines_withRerollOnesToWound_againstGuardsment() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_WOUND);
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var rerolls = ((hits - wounds) / 6) * Probability.THREE_UP;
		wounds += rerolls;
		
		double expectedDamage = Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this scenario we let our space marines use oath of the moment
	 * so they can fully reroll the hit roll
	 */
	@Test 
	void spaceMarines_withRerollHit_against_guardsmen() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_HIT_ROLL);
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var rerolls = (10 - hits) * Probability.THREE_UP;
		hits += rerolls;
		var wounds = hits * Probability.THREE_UP;
		double expectedDamage = Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		assertEquals(expectedDamage, damage);
	}
	
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
		double expectedDamage = Math.floor(missedSaves * Probability.FOUR_UP);
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * The Space marines have braught reinforcements!
	 * A special rule allows our models to reroll ones on the hit roll
	 */
	@Test
	void SpaceMarines_WithRerollOnesToHit_RerollOnesToHit() {
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

}

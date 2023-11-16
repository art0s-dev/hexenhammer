package core.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.Probability;
import core.Profile;
import core.Profile.SpecialRuleProfile;
import core.Profile.Type;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import core.Weapon;
import core.Weapon.Phase;

/**
 * These are our battle simulations to test the unit API.
 * The Test cases ensure that the attack sequence is ruled correctly
 */
class BattleSimulations {
	
	Weapon heavyBolter;
	Profile otherSpaceMarines;
	Weapon flameThrower;
	Profile guardsmen;
	Weapon bolter;
	Profile abberants;
	Profile lemanRussTank;
	
	/**
	 * Here we test the damage - we expect the maximum number 
	 * of damage to be equal to the number of hitpoints * missed Saves
	 */
	@Test @Disabled
	void GivenMultiWoundTarget_ThenDamageOfASingleShotDoesNotExceedHitPoints() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		
		var damage = spaceMarines.attack(otherSpaceMarines);
		var hits = 12 * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var failedSaves = wounds - (wounds * Probability.FIVE_UP); 
		int hitPoints = otherSpaceMarines.getHitPoints();
		int expectedDamage = (int) Math.floor(failedSaves) * hitPoints;
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In our next case we bring something interesting intrducing the flameThrower
	 * the flamethrowers gain a quantity like w6 -> 2,5 and then autohit
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithFlameThrowers_WhenAttack_ThenTorrentRuleIsCalculatedCorrect() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, flameThrower); //Torrent rule is in flame thrower
		
		var damage = spaceMarines.attack(guardsmen);
		var wounds = Probability.d6(4) * Probability.THREE_UP;
		int expectedDamage = (int)Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In the next case we have brought fancy special rules with us
	 * that allow our space marines to reroll the whole wound roll.
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithReRollWounds_WhenAttack_ThenCalculateRerollWoundsCorrect() {
		var spaceMarines = new Unit();
		spaceMarines.equip(4, heavyBolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_WOUND_ROLL);
		
		var damage = spaceMarines.attack(guardsmen);
		var hits = 12 * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var rerolls = (hits - wounds) * Probability.THREE_UP;
		wounds += rerolls;
		int expectedDamage = (int) Math.floor(wounds); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithRerollOnesToWound_WhenAttack_ThenCalculateRerollCorrect() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_WOUND);
		
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var missedWounds = hits - wounds;
		var rerollHits = (missedWounds / 6) * Probability.THREE_UP;
		wounds += rerollHits;
		int expectedDamage = (int)Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this scenario we let our space marines use oath of the moment
	 * so they can fully reroll the hit roll
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithRerollHitRoll_WhenAttack_ThenCalculateRerollCorrect() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_HIT_ROLL);
		
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var rerolls = (10 - hits) * Probability.THREE_UP;
		hits += rerolls;
		var wounds = hits * Probability.THREE_UP;
		int expectedDamage = (int)Math.floor(wounds - (wounds * Probability.FIVE_UP)); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * On this one we test the wound migitation of feel no pain.
	 * we want to halve the incoming damage
	 */
	@Test @Disabled
	void GivenAbberantsAsTarget_WhenAttack_TheMigigtateHalveOfTheWounds() {
		var spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		
		var damage = spaceMarines.attack(abberants);
		var hits = 10 * Probability.THREE_UP;
		var wounds = hits * Probability.FIVE_UP;
		var missedSaves = Math.floor(wounds - (wounds * Probability.FIVE_UP));
		//Feel no pain wound migitation
		int expectedDamage = (int)Math.floor(missedSaves * Probability.FOUR_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * The Space marines have braught reinforcements!
	 * A special rule allows our models to reroll ones on the hit roll
	 */
	@Test @Disabled
	void GivenSpaceMarines_WhenRerollOnesToHit_ThenCalculateTheDamageCorrect() {
		var spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_HIT);
		spaceMarines.equip(5, bolter);
		
		var damage = spaceMarines.attack(guardsmen);
		var hits = 10 * Probability.THREE_UP;
		var missedHits = 10 - hits;
		//We reroll only ones
		hits += (missedHits / 6) * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		int expectedDamage = (int)Math.floor(wounds - (wounds * Probability.FIVE_UP));
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test Anti-X weapons. They always wound a certain type of
	 * profile on the given probability.
	 */
	@Test @Disabled
	void GivenAntiWeapons_WhenAttack_ThenWoundProbabiltyIsHigherThanNaturalStrengthComparison() {
		Weapon haywireCannon = Weapon.builder()
				.attacks(2)
				.toHit(Probability.THREE_UP)
				.setAntiType(Type.VEHICLE, Probability.FOUR_UP)
				.strength(3)
				.armorPenetration(1)
				.damage(3)
				.build();
		
		var harlequinBiker = new Unit();
		//Normally those haywire cannons have devastating wounds
		//we don't add that here for now
		int quantity = 10;
		harlequinBiker.equip(quantity, haywireCannon);
		
		var damage = harlequinBiker.attack(lemanRussTank);
		var hits = (quantity * haywireCannon.getAttacks()) * haywireCannon.getToHit();
		//shall return an optional of a double a <enum, double)(probability)
		var wounds = hits * haywireCannon.getAntiType().orElseThrow().probability(); 
		int missedSaves = (int) Math.floor(wounds - (wounds * Probability.THREE_UP));	
		int expectedDamage = (int) Math.floor(missedSaves * haywireCannon.getDamage());
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * lets use some knifes! We test if our algo filters all weapns except combat weapons
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithKnifes_WhenAttack_OnlyCombarWeaponsGetCalculated() {
		Weapon combatKnife = Weapon.builder()
				.attacks(2)
				.toHit(Probability.THREE_UP)
				.strength(4)
				.armorPenetration(0)
				.damage(1)
				.phase(Phase.FIGHT)
				.build();
		
		var spaceMarines = new Unit();
		spaceMarines.setPhase(Phase.FIGHT);
		spaceMarines.equip(5, combatKnife);
		spaceMarines.equip(5, heavyBolter);
		
		var damage = spaceMarines.attack(guardsmen);
		var hits = 5 * (combatKnife.getAttacks())* Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		var expectedDamage = (int) Math.floor(wounds - (wounds * Probability.FIVE_UP));
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Let us introduce the Melter rule
	 * Melter are weapons that deal extra damage if the target is in melter range.
	 * the amount of extra damage is set by the user
	 */
	@Test @Disabled
	void GivenSpaceMarinesWithMelters_WhenAttack_TheyDealExtraDamage() {
		Weapon melter = Weapon.builder()
				.attacks(1)
				.toHit(Probability.THREE_UP)
				.strength(9)
				.armorPenetration(4)
				.damage(Probability.d6(1))
				.melta(3)
				.build();
	
		int quantity = 10;
		var spaceMarines = new Unit();
		spaceMarines.equip(quantity, melter);
		var damage = spaceMarines.attack(lemanRussTank);
		
		var hits = quantity * Probability.THREE_UP;
		var wounds = hits * Probability.FIVE_UP;
		int missedSaves = (int) Math.floor(wounds - (wounds * Probability.SIX_UP));
		var expectedDamage = (int) Math.floor(missedSaves * (melter.getDamage() + melter.getMelta()));
		
		assertTrue(expectedDamage > 0);
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Lets use some cover. We attack again with the space marines
	 * and the guardsmen get some cover
	 */
	@Test @Disabled
	void GivenSpaceMarines_WhenGuardsmenHaveCover_ThenSpaceMarinesDealLessDamage() {
		var spaceMarines = new Unit();
		int quantity = 10;
		spaceMarines.equip(quantity, bolter);
		guardsmen.add(SpecialRuleProfile.HAS_COVER);
		
		var damage = spaceMarines.attack(guardsmen);
		//10 Attacks with 3+ to hit, 3+ to wound and a 5up save
		var hits = (quantity * bolter.getAttacks()) * Probability.THREE_UP;
		var wounds = hits * Probability.THREE_UP;
		int expectedDamage = (int) Math.floor(wounds - (wounds * Probability.FOUR_UP)); 
		
		assertEquals(expectedDamage, damage);
	}
}

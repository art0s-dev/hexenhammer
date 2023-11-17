/**
 * 
 */
package core.unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Profile;
import core.Profile.SpecialRuleProfile;
import core.Profile.Type;
import core.Unit.SpecialRuleUnit;
import core.Unit;
import core.Weapon;
import core.Weapon.AntiType;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;


/**
 * These are our battle simulations to test the unit API.
 * The Test cases ensure that the attack sequence is ruled correctly
 */
@TestMethodOrder(MethodOrderer.Random.class)
class UnitBattleSequenceTest {
	
	Weapon bolter;
	Weapon heavyBolter;
	Profile guardsmen;
	Profile aberrants;
	Profile lemanRussTank;
	
	@BeforeEach
	void setup() {
		bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2.00);
		when(bolter.getStrength()).thenReturn(4);
		when(bolter.getArmorPenetration()).thenReturn(0);
		when(bolter.getDamage()).thenReturn(1.00);	
		when(bolter.getToHit()).thenReturn(Probability.THREE_UP);
		
		heavyBolter = mock(Weapon.class);
		when(heavyBolter.getAttacks()).thenReturn(3.00);
		when(heavyBolter.getToHit()).thenReturn(Probability.THREE_UP);
		when(heavyBolter.getStrength()).thenReturn(5);
		when(heavyBolter.getArmorPenetration()).thenReturn(2);
		
		guardsmen = mock(Profile.class);
		when(guardsmen.getToughness()).thenReturn(3);
		when(guardsmen.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn(1);
		
		aberrants = mock(Profile.class);
		when(aberrants.getToughness()).thenReturn(6);
		when(aberrants.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(aberrants.getFeelNoPain()).thenReturn(Probability.FOUR_UP);
		when(aberrants.getHitPoints()).thenReturn(3);
		
		lemanRussTank = mock(Profile.class);
		when(lemanRussTank.getToughness()).thenReturn(11);
		when(lemanRussTank.getArmorSave()).thenReturn(Probability.TWO_UP);
		when(lemanRussTank.getHitPoints()).thenReturn(13);
	}
	
	/**
	 * This is the base test case for our attack api.
	 */
	@Test @DisplayName("Base Mechanic - Base Case")
	void GivenSpaceMarinesEquippedWithBolter_WhenAttackingGuardsmen_ThenDamageIsCalculatedCorrect() {
		Unit spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		double damage = spaceMarines.attack(guardsmen);
		
		//Expectation
		int attacks = 5 * 2;
		double hits = attacks * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * Here we shall test, if the dodge save of our rangers will be taken
	 * instead of the armor save
	 */
	@Test @DisplayName("Base Mechanic - Invul Save")
	void GivenEldarRangerAsTarget_WhenArmorSaveIsLoweThanInvulSave_ThenTakeInvulSave() {

		Profile eldarRangers = mock(Profile.class);
		when(eldarRangers.getToughness()).thenReturn(3);
		when(eldarRangers.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(eldarRangers.getInvulnerableSave()).thenReturn(Probability.FIVE_UP);
		
		int quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, heavyBolter);
		double damage = spaceMarines.attack(eldarRangers);
		
		double hits = (quantity * heavyBolter.getAttacks()) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double missedSaves = wounds - (wounds * Probability.FIVE_UP); 
		double expectedDamage = missedSaves * eldarRangers.getHitPoints();
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test the feel no pain mechanic. FNP takes of the percentage of damage
	 * after the damage applies.
	 */
	@Test @DisplayName("Base Mechanic - Feel No Pain")
	void GivenAberrantsAsTarget_WhenEquippedWithHeavyBolter_ThenCalculateTheDamageCorrect() {
		
		int quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, heavyBolter);
		double damage = spaceMarines.attack(aberrants);
		
		double hits = (quantity * heavyBolter.getAttacks()) * Probability.THREE_UP;
		double wounds = hits * Probability.FIVE_UP;
		double damagePool = wounds * heavyBolter.getDamage();
		//This is the FNP Step
		double expectedDamage = damagePool - (damagePool * Probability.FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * lets use some knifes! We test if our algo filters all weapns except combat weapons
	 */
	@Test @DisplayName("Base Mechanic - Combat")
	void GivenSpaceMarinesWithKnifes_WhenAttack_OnlyCombarWeaponsGetCalculated() {
		
		Weapon combatKnife = mock(Weapon.class);
		when(combatKnife.getAttacks()).thenReturn(2.00);
		when(combatKnife.getToHit()).thenReturn(Probability.THREE_UP);
		when(combatKnife.getStrength()).thenReturn(4);
		when(combatKnife.getPhase()).thenReturn(Phase.FIGHT);
		when(combatKnife.getDamage()).thenReturn(1.00);
		
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.setPhase(Phase.FIGHT);
		spaceMarines.equip(quantity, combatKnife);
		spaceMarines.equip(quantity, heavyBolter);
		double damage = spaceMarines.attack(guardsmen);
		
		double hits = (quantity * combatKnife.getAttacks())* Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test the damage - we expect the maximum number 
	 * of damage to be equal to the number of hitpoints * missed Saves
	 * So if we divide by the number of missed saves we should get the number of hitpoints
	 */
	@Test @DisplayName("Base Mechanic - Mulit Wound")
	void GivenMultiWoundTarget_WhenAttack_ThenDamageIsMultipleOfHitpoints() {
		Weapon laserCannon = mock(Weapon.class);
		when(laserCannon.getAttacks()).thenReturn(1.00);
		when(laserCannon.getToHit()).thenReturn(Probability.THREE_UP);
		when(laserCannon.getStrength()).thenReturn(9);
		when(laserCannon.getArmorPenetration()).thenReturn(3);
		when(laserCannon.getDamage()).thenReturn(Probability.d6(1));
		
		//Disable FNP for Damage Test
		when(aberrants.getFeelNoPain()).thenReturn(0.00);
		
		int quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, laserCannon);
		double damage = spaceMarines.attack(aberrants);
		
		double hits = (quantity * laserCannon.getAttacks()) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		
		assertEquals(aberrants.getHitPoints(), damage / wounds);
	}
	
	/**
	 * The Space marines have braught reinforcements!
	 * A special rule allows our models to reroll ones on the hit roll
	 */
	@Test @DisplayName("Rerolls - Reroll ones on hit")
	void GivenSpaceMarines_WhenRerollOnesToHit_ThenCalculateTheDamageCorrect() {
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_HIT);
		spaceMarines.equip(quantity, bolter);
		double damage = spaceMarines.attack(guardsmen);
		
		double attacks = (quantity * bolter.getAttacks());
		double hits = (attacks) * Probability.THREE_UP;
		double missedHits = attacks - hits;
		//We reroll only ones
		hits += (missedHits / 6) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this scenario we let our space marines use oath of the moment
	 * so they can fully reroll the hit roll
	 */
	@Test @DisplayName("Rerolls - Reroll full hit")
	void GivenSpaceMarinesWithRerollHitRoll_WhenAttack_ThenCalculateRerollCorrect() {
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_HIT_ROLL);
		spaceMarines.equip(quantity, bolter);
		double damage = spaceMarines.attack(guardsmen);
		
		double attacks = (quantity * bolter.getAttacks());
		double hits = (attacks) * Probability.THREE_UP;
		double missedHits = attacks - hits;
		//We reroll only ones
		hits += (missedHits) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test @DisplayName("Rerolls - Reroll ones on wound")
	void GivenSpaceMarinesWithRerollOnesToWound_WhenAttack_ThenCalculateRerollCorrect() {
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_WOUND);
		double damage = spaceMarines.attack(guardsmen);
		
		double attacks = (quantity * bolter.getAttacks());
		double hits = (attacks) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double missedWounds = hits - wounds;
		wounds += (missedWounds / 6) * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test @DisplayName("Rerolls - Reroll full wound")
	void GivenSpaceMarinesWithRerollWounds_WhenAttack_ThenCalculateRerollCorrect() {
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(5, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_WOUND_ROLL);
		double damage = spaceMarines.attack(guardsmen);
		
		double attacks = (quantity * bolter.getAttacks());
		double hits = (attacks) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double missedWounds = hits - wounds;
		wounds += missedWounds * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Let us introduce the Melter rule
	 * Melter are weapons that deal extra damage if the target is in melter range.
	 * the amount of extra damage is set by the user
	 */
	@Test @DisplayName("Special Rules - Melta")
	void GivenSpaceMarinesWithMelters_WhenAttack_TheyDealExtraDamage() {	
		Weapon melter = mock(Weapon.class);
		when(melter.getAttacks()).thenReturn(1.00);
		when(melter.getToHit()).thenReturn(Probability.THREE_UP);
		when(melter.getStrength()).thenReturn(9);
		when(melter.getArmorPenetration()).thenReturn(4);
		when(melter.getDamage()).thenReturn(Probability.d6(1));
		when(melter.getMelta()).thenReturn(3);
		

		
		int quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, melter);
		double damage = spaceMarines.attack(lemanRussTank);
		
		double hits = (quantity * melter.getAttacks()) * Probability.THREE_UP;
		double wounds = hits * Probability.FIVE_UP;
		double missedSaves = wounds - (wounds * Probability.SIX_UP);
		double expectedDamage = missedSaves * (melter.getDamage() + melter.getMelta());
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Lets use some cover. We attack again with the space marines
	 * and the guardsmen get some cover
	 */
	@Test @DisplayName("Special Rules - Cover")
	void GivenSpaceMarines_WhenGuardsmenHaveCover_ThenSpaceMarinesDealLessDamage() {
		//Weapon has to be shooting weapon
		when(bolter.getPhase()).thenReturn(Phase.SHOOTING);
		
		//Profile has to be in cover
		when(guardsmen.has(SpecialRuleProfile.HAS_COVER)).thenReturn(true);
		
		int quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		double damage = spaceMarines.attack(guardsmen);
		
		double hits = (quantity * bolter.getAttacks()) * Probability.THREE_UP;
		double wounds = hits * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In our next case we bring something interesting intrducing the flameThrower
	 * the flamethrowers gain a quantity like w6 -> 2,5 and then autohit
	 */
	@Test @DisplayName("Special Rules - Torrent")
	void GivenSpaceMarinesWithFlameThrowers_WhenAttack_ThenTorrentRuleIsCalculatedCorrect() {
		
		Weapon flameThrower = mock(Weapon.class);
		when(flameThrower.getAttacks()).thenReturn(Probability.d6(1));
		when(flameThrower.getStrength()).thenReturn(4);
		when(flameThrower.getDamage()).thenReturn(1.00);
		when(flameThrower.has(SpecialRuleWeapon.TORRENT)).thenReturn(true);
		
		Unit spaceMarines = new Unit();
		spaceMarines.equip(4, flameThrower);
		double damage = spaceMarines.attack(guardsmen);
		double wounds = Probability.d6(4) * Probability.THREE_UP;
		double expectedDamage = wounds - (wounds * Probability.FIVE_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test Anti-X weapons. They always wound a certain type of
	 * profile on the given probability.
	 */
	@Test @DisplayName("Special Rules - Anti Type")
	void GivenAntiWeapons_WhenAttack_ThenWoundProbabiltyIsHigherThanNaturalStrengthComparison() {
		Weapon haywireCannon = mock(Weapon.class);
		when(haywireCannon.getAttacks()).thenReturn(2.00);
		when(haywireCannon.getStrength()).thenReturn(3);
		when(haywireCannon.getDamage()).thenReturn(3.00);
		Optional<AntiType> vehicles = Optional.of(new AntiType(Type.VEHICLE, Probability.FOUR_UP));
		when(haywireCannon.getAntiType()).thenReturn(vehicles);
		
		//Add vehicle type
		when(lemanRussTank.getType()).thenReturn(Type.VEHICLE);
		
		int quantity = 10;
		Unit harlequinBiker = new Unit();		
		harlequinBiker.equip(quantity, haywireCannon);
		double damage = harlequinBiker.attack(lemanRussTank);
		
		double hits = (quantity * haywireCannon.getAttacks()) * haywireCannon.getToHit();
		//shall return an optional of a double a <enum, double)(probability)
		double wounds = hits * haywireCannon.getAntiType().orElseThrow().probability(); 
		double missedSaves = wounds - (wounds * Probability.THREE_UP);	
		double expectedDamage = missedSaves * haywireCannon.getDamage();

		assertEquals(expectedDamage, damage);
	}
	
	
	
	
	
	
	
	
	
	
	
}

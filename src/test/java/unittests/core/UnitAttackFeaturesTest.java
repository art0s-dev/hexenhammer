package unittests.core;

import static core.CombatResult.getOverallDamage;
import static core.Probability.FIVE_UP;
import static core.Probability.FOUR_UP;
import static core.Probability.NONE;
import static core.Probability.SIX_UP;
import static core.Probability.THREE_UP;
import static core.Probability.TWO_UP;
import static core.Unit.Phase.FIGHT;
import static core.Unit.SpecialRuleUnit.ADD_ONE_TO_HIT;
import static core.Unit.SpecialRuleUnit.HAS_COVER;
import static core.Unit.SpecialRuleUnit.LETHAL_HITS;
import static core.Unit.SpecialRuleUnit.REROLL_WOUND_ROLL;
import static core.Weapon.Range.MELEE;
import static core.Weapon.Range.SHOOTING;
import static core.Weapon.SpecialRuleWeapon.DEVASTATING_WOUNDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Probability;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import core.Weapon;
import lombok.val;


/**
 * These are our battle simulations to test the unit API.
 * The Test cases ensure that the attack sequence is ruled correctly
 */
@TestMethodOrder(MethodOrderer.Random.class) 
class UnitAttackFeaturesTest {
	
	Weapon bolter;
	Weapon heavyBolter;
	Unit guardsmen;
	Unit aberrants;
	Unit lemanRussTank;
	
	@BeforeEach
	void setup() {
		bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2f);
		when(bolter.getStrength()).thenReturn(4);
		when(bolter.getArmorPenetration()).thenReturn(0);
		when(bolter.getDamage()).thenReturn(1f);	
		when(bolter.getRange()).thenReturn(SHOOTING);
		when(bolter.getToHit()).thenReturn(THREE_UP);
		
		heavyBolter = mock(Weapon.class);
		when(heavyBolter.getAttacks()).thenReturn(3f);
		when(heavyBolter.getToHit()).thenReturn(THREE_UP);
		when(heavyBolter.getStrength()).thenReturn(5);
		when(heavyBolter.getRange()).thenReturn(SHOOTING);
		when(heavyBolter.getArmorPenetration()).thenReturn(2);
		
		guardsmen = mock(Unit.class);
		when(guardsmen.getToughness()).thenReturn(3);
		when(guardsmen.getArmorSave()).thenReturn(FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn(1);
		
		aberrants = mock(Unit.class);
		when(aberrants.getToughness()).thenReturn(6);
		when(aberrants.getArmorSave()).thenReturn(FIVE_UP);
		when(aberrants.getFeelNoPain()).thenReturn(FOUR_UP);
		when(aberrants.getHitPoints()).thenReturn(3);
		
		lemanRussTank = mock(Unit.class);
		when(lemanRussTank.getToughness()).thenReturn(11);
		when(lemanRussTank.getArmorSave()).thenReturn(TWO_UP);
		when(lemanRussTank.getHitPoints()).thenReturn(13);
	}
	
	/**
	 * This is the base test case for our attack api.
	 */
	@Test @DisplayName("Base Mechanic - Base Case")
	void GivenSpaceMarinesEquippedWithBolter_WhenAttackingGuardsmen_ThenDamageIsCalculatedCorrect() {
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, bolter);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		//Expectation
		int attacks =  quantity * 2;
		float hits = attacks * THREE_UP;
		float wounds = hits * THREE_UP;
		float expectedDamage = wounds - (wounds * FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * Here we shall test, if the dodge save of our rangers will be taken
	 * instead of the armor save
	 */
	@Test @DisplayName("Base Mechanic - Invul Save")
	void GivenEldarRangerAsTarget_WhenArmorSaveIsLoweThanInvulSave_ThenTakeInvulSave() {

		Unit eldarRangers = mock(Unit.class);
		when(eldarRangers.getToughness()).thenReturn(3);
		when(eldarRangers.getArmorSave()).thenReturn(FIVE_UP);
		when(eldarRangers.getInvulnerableSave()).thenReturn(FIVE_UP);
		
		int quantity = 4;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, heavyBolter);
		float damage = getOverallDamage(spaceMarines.attack(eldarRangers));
		
		float hits = (quantity * heavyBolter.getAttacks()) * THREE_UP;
		float wounds = hits * THREE_UP;
		float missedSaves = wounds - (wounds * FIVE_UP); 
		float expectedDamage = missedSaves * eldarRangers.getHitPoints();
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test the feel no pain mechanic. FNP takes of the percentage of damage
	 * after the damage applies.
	 */
	@Test @DisplayName("Base Mechanic - Feel No Pain")
	void GivenAberrantsAsTarget_WhenEquippedWithHeavyBolter_ThenCalculateTheDamageCorrect() {
		
		byte quantity = 4;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, heavyBolter);
		float damage = getOverallDamage(spaceMarines.attack(aberrants));
		
		float hits = (quantity * heavyBolter.getAttacks()) * THREE_UP;
		float wounds = hits * FIVE_UP;
		float damagePool = wounds * heavyBolter.getDamage();
		//This is the FNP Step
		float expectedDamage = damagePool - (damagePool * FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * lets use some knifes! We test if our algo filters all weapns except combat weapons
	 */
	@Test @DisplayName("Base Mechanic - Combat")
	void GivenSpaceMarinesWithKnifes_WhenAttack_OnlyCombarWeaponsGetCalculated() {
		Weapon combatKnife = mock(Weapon.class);
		when(combatKnife.getAttacks()).thenReturn(2f);
		when(combatKnife.getToHit()).thenReturn(THREE_UP);
		when(combatKnife.getStrength()).thenReturn(4);
		when(combatKnife.getRange()).thenReturn(MELEE);
		when(combatKnife.getDamage()).thenReturn(1f);
		
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.usePhase(FIGHT);
		spaceMarines.equip(quantity, combatKnife);
		spaceMarines.equip(quantity, heavyBolter);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float hits = (quantity * combatKnife.getAttacks())* THREE_UP;
		float wounds = hits * THREE_UP;
		float expectedDamage = wounds - (wounds * FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this scenario we let our space marines use oath of the moment
	 * so they can fully reroll the hit roll
	 */
	@Test @DisplayName("Rerolls - Reroll full hit")
	void GivenSpaceMarinesWithRerollHitRoll_WhenAttack_ThenCalculateRerollCorrect() {
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.add(SpecialRuleUnit.REROLL_HIT_ROLL);
		spaceMarines.equip(quantity, bolter);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float attacks = (quantity * bolter.getAttacks());
		float hits = (attacks) * Probability.THREE_UP;
		float missedHits = attacks - hits;
		//We reroll only ones
		hits += (missedHits) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test @DisplayName("Rerolls - Reroll full wound")
	void GivenSpaceMarinesWithRerollWounds_WhenAttack_ThenCalculateRerollCorrect() {
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(REROLL_WOUND_ROLL);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float attacks = (quantity * bolter.getAttacks());
		float hits = (attacks) * THREE_UP;
		float wounds = hits * THREE_UP;
		float missedWounds = hits - wounds;
		wounds += missedWounds * THREE_UP;
		float expectedDamage = wounds - (wounds * FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Lets use some cover. We attack again with the space marines
	 * and the guardsmen get some cover
	 */
	@Test @DisplayName("Special Rules - Cover") 
	void GivenSpaceMarines_WhenGuardsmenHaveCover_ThenSpaceMarinesDealLessDamage() {
		when(bolter.getRange()).thenReturn(SHOOTING);
		when(guardsmen.has(HAS_COVER)).thenReturn(true);
		
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, bolter);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float hits = (quantity * bolter.getAttacks()) * THREE_UP;
		float wounds = hits * THREE_UP;
		float expectedDamage = wounds - (wounds * FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Devastating wounds here we go! Every 6 Rolled on the wound roll shall
	 * bypass the armour save.
	 */
	@Test @DisplayName("Special Rules - Devastating wounds") @Disabled("Gonna reimplement this later")
	void GivenSpaceMarines_WhenAddingDevastaingWounds_TheDamageIsCalculatedCorrect() {
		when(bolter.has(DEVASTATING_WOUNDS)).thenReturn(true);
		
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, bolter);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float hits = (quantity * bolter.getAttacks()) * THREE_UP;
		float wounds = hits * THREE_UP;
		float devastatingWounds = hits * SIX_UP;
		float expectedDamage = wounds - (wounds * FIVE_UP);
		expectedDamage += devastatingWounds;
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Now we come to the real warcrimes - we combine add 1 to hit roll and 
	 * the lethal hits. Means that the lethal hits trigger now on 5
	 */ 
	@Test @DisplayName("Special Rules - Lethal Hits on 5") @Disabled
	void GivenSpaceMarinesWithLethalHitsWeapons_WhenAddOneToHit_ThenMoreLethalHitsAreProduced() {
		int quantity = 5;
		Unit spaceMarines = Unit.builder().build();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(ADD_ONE_TO_HIT);
		when(spaceMarines.has(LETHAL_HITS)).thenReturn(true);
		float damage = getOverallDamage(spaceMarines.attack(guardsmen));
		
		float hitPool = quantity * bolter.getAttacks();
		float hits = hitPool * Probability.TWO_UP;
		float lethalHits = hitPool * Probability.FIVE_UP;
		//Lethal hits wander into the wound pool
		hits -= lethalHits;
		
		float wounds = (hits * Probability.THREE_UP) + lethalHits;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	@Test @DisplayName("Regression - Test no Armor Sa") 
	void testDamageAgainstUnitWithNoArmorSave() {
		val unit = Unit.builder().build();
		unit.equip(5, bolter);
		float initialDamage = getOverallDamage(unit.attack(guardsmen));
		assertTrue(initialDamage > 0);
		
		when(guardsmen.getArmorSave()).thenReturn(NONE);
		assertTrue(initialDamage < getOverallDamage(unit.attack(guardsmen)));
	}
	
}

package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Enemy;
import core.Enemy.SpecialRuleProfile;
import core.Enemy.Type;
import core.Probability;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import core.Weapon;
import core.Weapon.AntiType;
import core.Weapon.Phase;
import core.Weapon.SpecialRuleWeapon;


/**
 * These are our battle simulations to test the unit API.
 * The Test cases ensure that the attack sequence is ruled correctly
 */
@TestMethodOrder(MethodOrderer.Random.class) 
class UnitAttackFeaturesTest {
	
	Weapon bolter;
	Weapon heavyBolter;
	Enemy guardsmen;
	Enemy aberrants;
	Enemy lemanRussTank;
	
	@BeforeEach
	void setup() {
		bolter = mock(Weapon.class);
		when(bolter.getAttacks()).thenReturn(2f);
		when(bolter.getStrength()).thenReturn((byte)4);
		when(bolter.getArmorPenetration()).thenReturn((byte)0);
		when(bolter.getDamage()).thenReturn(1f);	
		when(bolter.getToHit()).thenReturn(Probability.THREE_UP);
		
		heavyBolter = mock(Weapon.class);
		when(heavyBolter.getAttacks()).thenReturn(3f);
		when(heavyBolter.getToHit()).thenReturn(Probability.THREE_UP);
		when(heavyBolter.getStrength()).thenReturn((byte)5);
		when(heavyBolter.getArmorPenetration()).thenReturn((byte)2);
		
		guardsmen = mock(Enemy.class);
		when(guardsmen.getToughness()).thenReturn((byte)3);
		when(guardsmen.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(guardsmen.getHitPoints()).thenReturn((byte)1);
		
		aberrants = mock(Enemy.class);
		when(aberrants.getToughness()).thenReturn((byte)6);
		when(aberrants.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(aberrants.getFeelNoPain()).thenReturn(Probability.FOUR_UP);
		when(aberrants.getHitPoints()).thenReturn((byte)3);
		
		lemanRussTank = mock(Enemy.class);
		when(lemanRussTank.getToughness()).thenReturn((byte)11);
		when(lemanRussTank.getArmorSave()).thenReturn(Probability.TWO_UP);
		when(lemanRussTank.getHitPoints()).thenReturn((byte)13);
	}
	
	/**
	 * This is the base test case for our attack api.
	 */
	@Test @DisplayName("Base Mechanic - Base Case")
	void GivenSpaceMarinesEquippedWithBolter_WhenAttackingGuardsmen_ThenDamageIsCalculatedCorrect() {
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		//Expectation
		byte attacks = (byte) (quantity * 2);
		float hits = attacks * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * Here we shall test, if the dodge save of our rangers will be taken
	 * instead of the armor save
	 */
	@Test @DisplayName("Base Mechanic - Invul Save")
	void GivenEldarRangerAsTarget_WhenArmorSaveIsLoweThanInvulSave_ThenTakeInvulSave() {

		Enemy eldarRangers = mock(Enemy.class);
		when(eldarRangers.getToughness()).thenReturn((byte)3);
		when(eldarRangers.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(eldarRangers.getInvulnerableSave()).thenReturn(Probability.FIVE_UP);
		
		byte quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, heavyBolter);
		float damage = spaceMarines.attack(eldarRangers);
		
		float hits = (quantity * heavyBolter.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float missedSaves = wounds - (wounds * Probability.FIVE_UP); 
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
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, heavyBolter);
		float damage = spaceMarines.attack(aberrants);
		
		float hits = (quantity * heavyBolter.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.FIVE_UP;
		float damagePool = wounds * heavyBolter.getDamage();
		//This is the FNP Step
		float expectedDamage = damagePool - (damagePool * Probability.FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * lets use some knifes! We test if our algo filters all weapns except combat weapons
	 */
	@Test @DisplayName("Base Mechanic - Combat")
	void GivenSpaceMarinesWithKnifes_WhenAttack_OnlyCombarWeaponsGetCalculated() {
		
		Weapon combatKnife = mock(Weapon.class);
		when(combatKnife.getAttacks()).thenReturn(2f);
		when(combatKnife.getToHit()).thenReturn(Probability.THREE_UP);
		when(combatKnife.getStrength()).thenReturn((byte)4);
		when(combatKnife.getPhase()).thenReturn(Phase.FIGHT);
		when(combatKnife.getDamage()).thenReturn(1f);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.setPhase(Phase.FIGHT);
		spaceMarines.equip(quantity, combatKnife);
		spaceMarines.equip(quantity, heavyBolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float hits = (quantity * combatKnife.getAttacks())* Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test the damage - we expect the maximum number 
	 * of damage to be equal to the number of hitpoints * missed Saves
	 * So if we divide by the number of missed saves we should get the number of hitpoints
	 */
	@Test @DisplayName("Base Mechanic - Mulit Wound") @Disabled
	void GivenMultiWoundTarget_WhenAttack_ThenDamageIsMultipleOfHitpoints() {
		Weapon laserCannon = mock(Weapon.class);
		when(laserCannon.getAttacks()).thenReturn(1f);
		when(laserCannon.getToHit()).thenReturn(Probability.THREE_UP);
		when(laserCannon.getStrength()).thenReturn((byte)9);
		when(laserCannon.getArmorPenetration()).thenReturn((byte)3);
		when(laserCannon.getDamage()).thenReturn(Probability.d6((byte)1));
		
		//Disable FNP for Damage Test
		when(aberrants.getFeelNoPain()).thenReturn(0f);
		
		byte quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, laserCannon);
		float damage = spaceMarines.attack(aberrants);
		
		float hits = (quantity * laserCannon.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		
		assertEquals(aberrants.getHitPoints(), damage / wounds);
	}
	
	/**
	 * The Space marines have braught reinforcements!
	 * A special rule allows our models to reroll ones on the hit roll
	 */
	@Test @DisplayName("Rerolls - Reroll ones on hit") @Disabled
	void GivenSpaceMarines_WhenRerollOnesToHit_ThenCalculateTheDamageCorrect() {
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_HIT);
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float attacks = (quantity * bolter.getAttacks());
		float hits = (attacks) * Probability.THREE_UP;
		float missedHits = attacks - hits;
		//We reroll only ones
		hits += (missedHits / 6) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this scenario we let our space marines use oath of the moment
	 * so they can fully reroll the hit roll
	 */
	@Test @DisplayName("Rerolls - Reroll full hit")
	void GivenSpaceMarinesWithRerollHitRoll_WhenAttack_ThenCalculateRerollCorrect() {
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.add(SpecialRuleUnit.REROLL_HIT_ROLL);
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
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
	@Test @DisplayName("Rerolls - Reroll ones on wound") @Disabled
	void GivenSpaceMarinesWithRerollOnesToWound_WhenAttack_ThenCalculateRerollCorrect() {
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_WOUND);
		float damage = spaceMarines.attack(guardsmen);
		
		float attacks = (quantity * bolter.getAttacks());
		float hits = (attacks) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float missedWounds = hits - wounds;
		wounds += (missedWounds / 6) * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In this case we let our space marines reroll ones to wound 
	 * (they've got a lieutenant or smth idk)
	 */
	@Test @DisplayName("Rerolls - Reroll full wound")
	void GivenSpaceMarinesWithRerollWounds_WhenAttack_ThenCalculateRerollCorrect() {
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_WOUND_ROLL);
		float damage = spaceMarines.attack(guardsmen);
		
		float attacks = (quantity * bolter.getAttacks());
		float hits = (attacks) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float missedWounds = hits - wounds;
		wounds += missedWounds * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Let us introduce the Melter rule
	 * Melter are weapons that deal extra damage if the target is in melter range.
	 * the amount of extra damage is set by the user
	 */
	@Test @DisplayName("Special Rules - Melta") @Disabled
	void GivenSpaceMarinesWithMelters_WhenAttack_TheyDealExtraDamage() {	
		Weapon melter = mock(Weapon.class);
		when(melter.getAttacks()).thenReturn(1f);
		when(melter.getToHit()).thenReturn(Probability.THREE_UP);
		when(melter.getStrength()).thenReturn((byte)9);
		when(melter.getArmorPenetration()).thenReturn((byte)4);
		when(melter.getDamage()).thenReturn(Probability.d6((byte)1));
		when(melter.getMelta()).thenReturn((byte)3);
		
		byte quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, melter);
		float damage = spaceMarines.attack(lemanRussTank);
		
		float hits = (quantity * melter.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.FIVE_UP;
		float missedSaves = wounds - (wounds * Probability.SIX_UP);
		float expectedDamage = missedSaves * (melter.getDamage() + melter.getMelta());
		
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
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float hits = (quantity * bolter.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FOUR_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * In our next case we bring something interesting intrducing the flameThrower
	 * the flamethrowers gain a quantity like w6 -> 2,5 and then autohit
	 */
	@Test @DisplayName("Special Rules - Torrent") @Disabled
	void GivenSpaceMarinesWithFlameThrowers_WhenAttack_ThenTorrentRuleIsCalculatedCorrect() {
		
		Weapon flameThrower = mock(Weapon.class);
		when(flameThrower.getAttacks()).thenReturn(Probability.d6((byte)1));
		when(flameThrower.getStrength()).thenReturn((byte)4);
		when(flameThrower.getDamage()).thenReturn(1f);
		when(flameThrower.has(SpecialRuleWeapon.TORRENT)).thenReturn(true);
		
		byte quantity = 4;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, flameThrower);
		float damage = spaceMarines.attack(guardsmen);
		float wounds = Probability.d6((byte)4) * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP); 
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Here we test Anti-X weapons. They always wound a certain type of
	 * profile on the given probability.
	 */
	@Test @DisplayName("Special Rules - Anti Type") @Disabled
	void GivenAntiWeapons_WhenAttack_ThenWoundProbabiltyIsHigherThanNaturalStrengthComparison() {
		Weapon haywireCannon = mock(Weapon.class);
		when(haywireCannon.getAttacks()).thenReturn(2f);
		when(haywireCannon.getStrength()).thenReturn((byte)3);
		when(haywireCannon.getDamage()).thenReturn(3f);
		Optional<AntiType> vehicles = Optional.of(new AntiType(Type.VEHICLE, Probability.FOUR_UP));
		when(haywireCannon.getAntiType()).thenReturn(vehicles);
		
		//Add vehicle type
		when(lemanRussTank.getType()).thenReturn(Type.VEHICLE);
		
		byte quantity = 10;
		Unit harlequinBiker = new Unit();		
		harlequinBiker.equip(quantity, haywireCannon);
		float damage = harlequinBiker.attack(lemanRussTank);
		
		float hits = (quantity * haywireCannon.getAttacks()) * haywireCannon.getToHit();
		//shall return an optional of a double a <enum, double)(probability)
		float wounds = hits * haywireCannon.getAntiType().orElseThrow().probability(); 
		float missedSaves = wounds - (wounds * Probability.THREE_UP);	
		float expectedDamage = missedSaves * haywireCannon.getDamage();

		assertEquals(expectedDamage, damage);
	}
	
	
	/**
	 * Now we implement a mechanic called sustained hits
	 * these hits generate extra hits on 6es.
	 */
	@Test @DisplayName("Special Rules - Sustainded hits 2") @Disabled
	void GivenSpaceMarines_WhenAddingSustainedHits_ThenDamageIsCalculatedCorrect() {
		when(bolter.getSustainedHits()).thenReturn((byte)2);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float hits = (quantity * bolter.getAttacks()) * Probability.THREE_UP;
		float sustainedHits = (hits / 6) * 2;
		hits += sustainedHits;
		float wounds = hits * Probability.THREE_UP;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Devastating wounds here we go! Every 6 Rolled on the wound roll shall
	 * bypass the armour save.
	 */
	@Test @DisplayName("Special Rules - Devastating wounds") @Disabled
	void GivenSpaceMarines_WhenAddingDevastaingWounds_TheDamageIsCalculatedCorrect() {
		when(bolter.has(SpecialRuleWeapon.DEVASTATING_WOUNDS)).thenReturn(true);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float hits = (quantity * bolter.getAttacks()) * Probability.THREE_UP;
		float wounds = hits * Probability.THREE_UP;
		float devastatingWounds = wounds / 6;
		wounds -= devastatingWounds;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		expectedDamage += devastatingWounds;
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Now We will test lethal hits on weapons(damn you deathguard!)
	 * lethal hits always wound on 6es to hit. We take the full wound pool
	 */
	@Test @DisplayName("Special Rules - Lethal Hits") @Disabled
	void GivenSpaceMarinesWeaponsWithLethalHit_WhenAttack_ThenMoreWoundsAreProduced(){
		when(bolter.has(SpecialRuleWeapon.LETHAL_HITS)).thenReturn(true);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		float damage = spaceMarines.attack(guardsmen);
		
		float hitPool = quantity * bolter.getAttacks();
		float hits = hitPool * Probability.THREE_UP;
		float lethalHits = hitPool * Probability.SIX_UP;
		//Lethal hits wander into the wound pool
		hits -= lethalHits;
		
		float wounds = (hits * Probability.THREE_UP) + lethalHits;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Now we come to the real warcrimes - we combine add 1 to hit roll and 
	 * the lethal hits. Means that the lethal hits trigger now on 5
	 */ 
	@Test @DisplayName("Special Rules - Lethal Hits on 5") @Disabled
	void GivenSpaceMarinesWithLethalHitsWeapons_WhenAddOneToHit_ThenMoreLethalHitsAreProduced() {
		when(bolter.has(SpecialRuleWeapon.LETHAL_HITS)).thenReturn(true);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(SpecialRuleUnit.ADD_ONE_TO_HIT);
		float damage = spaceMarines.attack(guardsmen);
		
		float hitPool = quantity * bolter.getAttacks();
		float hits = hitPool * Probability.TWO_UP;
		float lethalHits = hitPool * Probability.FIVE_UP;
		//Lethal hits wander into the wound pool
		hits -= lethalHits;
		
		float wounds = (hits * Probability.THREE_UP) + lethalHits;
		float expectedDamage = wounds - (wounds * Probability.FIVE_UP);
		
		assertEquals(expectedDamage, damage);
	}
	
	/**
	 * Now we further go down ne warcrime path. We let our units do the same trick again
	 * but now with reroll ones to hit
	 */
	@Test @DisplayName("Special Rules - Lethal Hits / reroll 1s to hit") @Disabled
	void GivenSpaceMarinesWithLethalHitsWeapons_WhenRerollOnesToHit_ThenProduceMoreLethalHits() {
		when(bolter.has(SpecialRuleWeapon.LETHAL_HITS)).thenReturn(true);
		
		byte quantity = 5;
		Unit spaceMarines = new Unit();
		spaceMarines.equip(quantity, bolter);
		spaceMarines.add(SpecialRuleUnit.REROLL_ONES_TO_HIT);
		float damage = spaceMarines.attack(guardsmen);
		
		//the first roll
		float attacks = 10;
		float lethalHits = attacks * (1/6f); //1,66666666667
		float hits = 10 * (4/6f); //6,666666667
		hits -= lethalHits; //5
		
		//Do the reroll
		float misses = (5) * (1/6f); //0,833333333333
		float rerollLethalHits = misses * (1/6f); //0,1388888
		float rerolledHits = misses * (4/6f); //0,5555555
		rerolledHits -= rerollLethalHits; //0,41666667
		hits += rerolledHits; //5,416666667
		lethalHits += rerollLethalHits; //1,8055555
		
		//Calculate the wounds
		float wounds = hits * Probability.THREE_UP; //3,6111111
		wounds += lethalHits; ///wounds = 5,4166666
		float expectedDamage = wounds - ( wounds * Probability.FIVE_UP); //-1,8055555
		//3,6111 

		assertEquals(expectedDamage, damage);
	}
	
}

package unittests.weapon;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import core.Probability;
import core.Probability.Dice;
import core.UserNumberInput;
import core.Weapon;

class WeaponTest {

	@Test
	void testAttackApiWhenNoUserInputIsGivenNumberOfAttacksIsZero() {
		Weapon bolter = Weapon.builder().build();
		assertEquals(0f, bolter.getAttacks());
	}
	
	@Test
	void testAttackApiWhenUserWantsToUseDiceTakeValueAndGenerateDiceValue() {
		byte numberOfDice = 5;
		float d6 = Probability.MEDIAN_D6; 
		
		Weapon bolter = Weapon.builder()
				.attackInput(Optional.of(UserNumberInput.withDice(numberOfDice, Dice.d6)))
				.build();
		
		assertEquals(numberOfDice * d6, bolter.getAttacks());
	}
	
	@Test 
	void testAttackApiWhenUserHasEnteredNumberOfAttacksReturnThatNumber() {
		byte numberOfAttacks = 22;
		
		Weapon bolter = Weapon.builder()
				.attackInput(Optional.of(UserNumberInput.withNumber(numberOfAttacks)))
				.build();
		
		assertEquals(numberOfAttacks, bolter.getAttacks());
	}
	
	@Test
	void testDamageApiWhenNothingIsEnteredThanReturnZero() {
		Weapon bolter = Weapon.builder().build();
		assertEquals(0f, bolter.getDamage());
	}
	
	@Test
	void testDamageApiWhenDiceAreEnteredReturnNumberOfDiceTimesDiceValue() {
		byte numberOfDice = 5;
		float d6 = Probability.MEDIAN_D6; 
		
		
		Weapon bolter = Weapon.builder()
				.damageInput(Optional.of(UserNumberInput.withDice(numberOfDice, Dice.d6)))
				.build();
		
		assertEquals(numberOfDice * d6, bolter.getDamage());
	}
	
	@Test 
	void testAttackApiWhenUserEntersNumberOfAttacksThanReturnThisNumber(){
		byte numberOfAttacks = 22;
		
		Weapon bolter = Weapon.builder()
				.damageInput(Optional.of(UserNumberInput.withNumber(numberOfAttacks)))
				.build();
		
		assertEquals(numberOfAttacks, bolter.getDamage());
	}

}

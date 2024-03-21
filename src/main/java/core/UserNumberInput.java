package core;

import core.Probability.Dice;

/**
 * We don't want the User to pick floats 
 * when it comes to determining the attack numbers
 * we want the user to choose
 * A) using an int (number of attacks)
 * or B) using a number of dices to generate the
 * number of attacks 
 */
public record UserNumberInput(
		boolean useDice,
		byte fixedNumber,
		byte diceQuantity,
		Probability.Dice dice
) {
	public static UserNumberInput withNumber(byte fixedNumber) {
		return new UserNumberInput(false, fixedNumber, (byte)0, Dice.d3);
	}
	
	public static UserNumberInput withDice(byte diceQuantity, Dice dice) {
		return new UserNumberInput(true, (byte)0, diceQuantity, dice);
	}
}

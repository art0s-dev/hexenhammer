package core;

import static core.Probability.Dice.d3;

import core.Probability.Dice;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * We don't want the User to pick floats 
 * when it comes to determining the attack numbers
 * we want the user to choose
 * A) using an int (number of attacks)
 * or B) using a number of dices to generate the
 * number of attacks 
 */
@AllArgsConstructor @Data
	public class UserNumberInput {
	boolean useDice;
	int fixedNumber;
	int diceQuantity;
	Probability.Dice dice;
	
	public static UserNumberInput withNumber(int fixedNumber) {
		return new UserNumberInput(false, fixedNumber, (byte)0, d3);
	}
	
	public static UserNumberInput withDice(int diceQuantity, Dice dice) {
		return new UserNumberInput(true, 0, diceQuantity, dice);
	}
}

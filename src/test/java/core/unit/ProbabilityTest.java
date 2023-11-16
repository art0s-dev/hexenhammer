package core.unit;

import static core.Probability.modifyRoll;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import core.Probability;

class ProbabilityTest {

	/**
	 * This Test covers the functionality of our roll modification
	 * The modification API allows just one modification (+1 or -1) at a time
	 */
	@Test @Disabled
	void modify_roll_test() {
		//Test the edge cases
		assertEquals(modifyRoll(7526.88, '+'), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88, '+'), Probability.SIX_UP);
		assertEquals(modifyRoll(7526.88, '-'), Probability.TWO_UP);
		assertEquals(modifyRoll(-7526.88, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(0, '+'), Probability.SIX_UP);
		assertEquals(modifyRoll(0, '?'), Probability.SIX_UP);
		assertEquals(modifyRoll(2, '?'), Probability.TWO_UP);
		
		//Test the main cases for positive modification
		assertEquals(modifyRoll(Probability.SIX_UP, '+'), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, '+'), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, '+'), Probability.THREE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, '+'), Probability.TWO_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, '+'), Probability.TWO_UP);
		
		//Test the main cases for negative modification
		assertEquals(modifyRoll(Probability.SIX_UP, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FIVE_UP, '-'), Probability.SIX_UP);
		assertEquals(modifyRoll(Probability.FOUR_UP, '-'), Probability.FIVE_UP);
		assertEquals(modifyRoll(Probability.THREE_UP, '-'), Probability.FOUR_UP);
		assertEquals(modifyRoll(Probability.TWO_UP, '-'), Probability.THREE_UP);
	}

}

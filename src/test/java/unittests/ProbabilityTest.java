package unittests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import core.entitys.Probability;

class ProbabilityTest {

	@Test
	void modify_roll_test() {
		//Test the edge cases
		assertEquals(Probability.modifyRoll(7526.88, '+'), Probability.TWO_UP);
		assertEquals(Probability.modifyRoll(-7526.88, '+'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(7526.88, '-'), Probability.TWO_UP);
		assertEquals(Probability.modifyRoll(-7526.88, '-'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(0, '+'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(0, '?'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(2, '?'), Probability.TWO_UP);
		
		//Test the main cases for positive modification
		assertEquals(Probability.modifyRoll(Probability.SIX_UP, '+'), Probability.FIVE_UP);
		assertEquals(Probability.modifyRoll(Probability.FIVE_UP, '+'), Probability.FOUR_UP);
		assertEquals(Probability.modifyRoll(Probability.FOUR_UP, '+'), Probability.THREE_UP);
		assertEquals(Probability.modifyRoll(Probability.THREE_UP, '+'), Probability.TWO_UP);
		assertEquals(Probability.modifyRoll(Probability.TWO_UP, '+'), Probability.TWO_UP);
		
		//Test the main cases fr negative modification
		assertEquals(Probability.modifyRoll(Probability.SIX_UP, '-'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(Probability.FIVE_UP, '-'), Probability.SIX_UP);
		assertEquals(Probability.modifyRoll(Probability.FOUR_UP, '-'), Probability.FIVE_UP);
		assertEquals(Probability.modifyRoll(Probability.THREE_UP, '-'), Probability.FOUR_UP);
		assertEquals(Probability.modifyRoll(Probability.TWO_UP, '-'), Probability.THREE_UP);
	}

}

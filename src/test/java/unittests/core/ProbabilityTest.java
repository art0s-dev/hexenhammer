package unittests.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import core.Probability;

class ProbabilityTest {

	@Test
	void testD6IsDeterministic() {
		assertEquals((1+2+3+4+5+6) / 6.00, Probability.d6((byte)1));
	}
	
	@Test
	void testD3IsDeterministic() {
		assertEquals((1+2+3) / 3.00, Probability.d3((byte)1));
	}

}

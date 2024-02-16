package core;

/**
 * Those are the Probs we use to calculate everything
 * the probabilities resemble a dice roll + determenistic constants
 * like an event is happening for sure or not at all
 * a roll of a one always fails
 */
public final class Probability {
	public final static float TWO_UP = 5 / 6f;
	public final static float THREE_UP = 4 / 6f;
	public final static float FOUR_UP = 3 / 6f;
	public final static float FIVE_UP = 2 / 6f;
	public final static float SIX_UP = 1 / 6f;
	public final static float NONE = 0;

	/**
	 * recreates a dice roll with a deterministic result
	 * @param quantity the number of d6 that that be rolled
	 */
	public final static float d6(byte quantity) {
		return quantity * MEDIAN_D6;
	}

	/**
	 * decribes the average dice roll (1+2+3+4+5+6) / 6
	 */
	public final static float MEDIAN_D6 = 3.5f;

	/**
	 * recreates a halved dice roll wih deterministic result
	 * @param quantity the number of d3 that shall be rolled
	 */
	public final static float d3(byte quantity) {
		return quantity * MEDIAN_D3;
	}

	/**
	 * describes the average of a halved dice roll (1+2+3) / 3
	 */
	public final static float MEDIAN_D3 = 2;

	/**
	 * modifies the roll + or - one result
	 * + makes the roll better (Five up -> four up)
	 * - makes the roll worse (Five up -> Six up)
	 * @param probability - the probability given (1 - 0.277)
	 * @param operator - char the operator - (+ OR -)
	 */
	public static float modifyRoll(float probability, Modifier modifier) {
		float newProbability = (float) ((Math.ceil(probability * 6) + modifier.value ) / 6);
		
		if (newProbability <= 0) {
			return Probability.SIX_UP;
		}
		
		if (newProbability > Probability.TWO_UP) {
			return Probability.TWO_UP;
		}

		return newProbability;
	}
	
	/**
	 * Modify a dice roll with +1 or -1
	 */
	public enum Modifier {
		PLUS_ONE((byte)1),
		MINUS_ONE((byte)-1);

		public final byte value;
		private Modifier(byte value) {
			this.value = value;
		}
	}
}

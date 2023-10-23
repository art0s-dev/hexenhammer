package core.entitys;

/*
 * Those are the Probs we use to calculate everything
 * the probabilities resemble a dice roll + determenistic constants
 * like an event is happening for sure or not at all
 * a roll of a one always fails
 */
public final class Probability {
	public final static double SURE = 1;
	public final static double TWO_UP = 5 / 6.00;
	public final static double THREE_UP = 4 / 6.00;
	public final static double FOUR_UP = 3 / 6.00;
	public final static double FIVE_UP = 2 / 6.00;
	public final static double SIX_UP = 1 / 6.00;
	public final static double NONE = 0.00;
}

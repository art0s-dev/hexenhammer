package core.entitys;

import java.util.HashMap;
import java.util.function.Function;

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
	
	/**
	 * recreates a dice roll with a determenistic result
	 */
	public final static double d6(int quantity) {
		return quantity * MEDIAN_D6;
	}
	
	/**
	 * decribes the average dice roll
	 * (1+2+3+4+5+6) / 6
	 */
	public final static double MEDIAN_D6 = 3.5;
	
	/**
	 * recreates a halved dice roll wih deterministic result
	 */
	public final static double d3(int quantity) {
		return quantity * MEDIAN_D3;
	}
	
	/**
	 * describes the average of a halved dice roll
	 * (1+2+3) / 2
	 */
	public final static double MEDIAN_D3 = 2;
	
	/**
	 * modifies the roll + or - one result
	 */
	public static double modifyRoll(double probability, char operator) {
		//+ makes the roll better (Five up -> four up)
		//- makes the roll worse (Fivr up -> Six up)
		int incrementOrDecrement = operator == '+' ? 1 : -1;
		double newProbability = (Math.ceil(probability * 6) + incrementOrDecrement) / 6;
		if(newProbability <= 0) {
			return Probability.SIX_UP;
		}
		if(newProbability > Probability.TWO_UP) {
			return Probability.TWO_UP;
		}
		
		return newProbability;
	}
	
}

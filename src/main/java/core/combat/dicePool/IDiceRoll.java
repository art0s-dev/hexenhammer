package core.combat.dicePool;

public interface IDiceRoll {
	/**
	 * Applies the implemented Functionality to the dice pool 
	 * in order to hit, wound etc...
	 */
	public DicePool roll(DicePool dicePool);
}

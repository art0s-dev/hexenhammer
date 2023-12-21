package core.combat;

public class WoundDicePool extends DicePool {
	
	private final boolean canBeRerolled;

	public WoundDicePool(float total, float result, boolean canBeRerolled) {
		super(total, result);
		this.canBeRerolled = canBeRerolled;
	}

}

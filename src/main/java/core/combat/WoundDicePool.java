package core.combat;

public class WoundDicePool extends DicePool {

	private final float devastatingWounds;
	
	public WoundDicePool(float total, float result, float devastatingWounds) {
		super(total, result);
		this.devastatingWounds = devastatingWounds;
	}
	
	public float getDevastatingWounds() {
		return this.devastatingWounds;
	}

}

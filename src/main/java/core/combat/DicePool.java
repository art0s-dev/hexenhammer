package core.combat;

/**
 * Base Data Structure to be consumed by the DiceRoll mechanics
 * It should only contain informations about the dice pool itself
 */
public class DicePool{
	private final float total;
	private final float result;
	
	public DicePool(float total, float result) {
		this.total = total;
		this.result = result;
	}
	
	public float getTotal() {
		return total;
	}

	public float getResult() {
		return result;
	}
}

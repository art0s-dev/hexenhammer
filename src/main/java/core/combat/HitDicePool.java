package core.combat;

public final class HitDicePool extends DicePool {

	private final float lethalHits;

	public HitDicePool(float total, float result, float lethalHits) {
		super(total, result);
		this.lethalHits = lethalHits;
	}
	
	public float getLethalHits() {
		return lethalHits;
	}
}

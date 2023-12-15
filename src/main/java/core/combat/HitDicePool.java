package core.combat;

public final class HitDicePool extends DicePool {

	private final float lethalHits;
	private final float sustainedHits;

	public HitDicePool(float total, float result, float lethalHits, float sustainedHits) {
		super(total, result);
		this.lethalHits = lethalHits;
		this.sustainedHits = sustainedHits;
	}
	
	public float getLethalHits() {
		return lethalHits;
	}


	public float getSustainedHits() {
		return sustainedHits;
	}

}

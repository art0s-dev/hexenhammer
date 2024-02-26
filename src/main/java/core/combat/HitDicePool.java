package core.combat;

import lombok.Getter;

public final class HitDicePool extends DicePool {
	
	@Getter private float lethalHits = 0f;
	@Getter private float sustainedHits = 0f;
	@Getter private float misses = 0f;
	@Getter private float hits = 0f;
	 
	public HitDicePool(float total, float result) {
		super(total, result);
	}
	
	public HitDicePool withLethalHits(float lethalHits) {
		this.lethalHits = lethalHits;
		return this;
	}
	
	public HitDicePool withSustainedHits(float sustainedHits) {
		this.sustainedHits = sustainedHits;
		return this;
	}
	
	public HitDicePool withMisses(float misses) {
		this.misses = misses;
		return this;
	}
	
	public HitDicePool withHits(float hits) {
		this.hits = hits;
		return this;
	}
}

package core.combat;

import lombok.Getter;

public class WoundDicePool extends DicePool {

	@Getter private float devastatingWounds;
	@Getter private float fails = 0f;
	
	public WoundDicePool(float total, float result) {
		super(total, result);
	}
	
	public WoundDicePool withDevastatingWounds(float devastatingWounds) {
		this.devastatingWounds = devastatingWounds;
		return this;
	}
	
	public WoundDicePool withFails(float fails) {
		this.fails = fails;
		return this;
	}

}

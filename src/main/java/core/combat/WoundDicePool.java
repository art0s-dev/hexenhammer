package core.combat;

import lombok.Getter;

public class WoundDicePool extends DicePool {

	@Getter private float devastatingWounds;
	
	public WoundDicePool(float total, float result, float devastatingWounds) {
		super(total, result);
		this.devastatingWounds = devastatingWounds;
	}
	

}

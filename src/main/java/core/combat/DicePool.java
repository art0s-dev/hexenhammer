package core.combat;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Base Data Structure to be consumed by the DiceRoll mechanics
 * It should only contain informations about the dice pool itself
 */
@AllArgsConstructor
public class DicePool {
	@Getter protected float total;
	@Getter protected float result;
}

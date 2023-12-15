package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.CombatRules;
import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import core.combat.DicePool;
import core.combat.FeelNoPainDiceRoll;
import lombok.val;

class FeelNoPainDiceRollTest {

	@Test @DisplayName("Test Feel No Pain")
	void feelNoPain() {
		val total = 10f;
		val dicePool = mock(DicePool.class);
		//The Feel no pain always uses the result of the saving throw
		when(dicePool.getTotal()).thenReturn(0f);
		when(dicePool.getResult()).thenReturn(total);
		
		val unit = mock(Unit.class);
		val weapon = mock(Weapon.class);
		val combatRules = mock(CombatRules.class);
		val enemy = mock(Enemy.class);
		when(enemy.getFeelNoPain()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new FeelNoPainDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		val feelNoPainCalc = total - (total * Probability.FIVE_UP);
		
		assertEquals(feelNoPainCalc, resultPool.getResult());
	}

}

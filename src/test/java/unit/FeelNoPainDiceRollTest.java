package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;
import core.combat.dicePool.DicePool;
import core.combat.dicePool.FeelNoPainDiceRoll;
import lombok.val;

class FeelNoPainDiceRollTest {

	@Test @DisplayName("Test Feel No Pain")
	void feelNoPain() {
		val total = 10f;
		val dicePool = mock(DicePool.class);
		when(dicePool.total()).thenReturn(total);
		when(dicePool.result()).thenReturn(0f);
		
		val unit = mock(Unit.class);
		val weapon = mock(Weapon.class);
		val combatRules = mock(CombatRules.class);
		val enemy = mock(Enemy.class);
		when(enemy.getFeelNoPain()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new FeelNoPainDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		val feelNoPainCalc = total - (total * Probability.FIVE_UP);
		
		assertEquals(feelNoPainCalc, resultPool.result());
	}

}

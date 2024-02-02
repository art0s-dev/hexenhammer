package unit.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;
import core.combat.DicePool;
import core.combat.FeelNoPainDiceRoll;
import lombok.val;

class FeelNoPainDiceRollTest {
	
	private float total = 10;
	private DicePool dicePool;
	private Unit unit;
	private CombatRules rules;
	private Enemy enemy;
	private Weapon weapon;
	
	@BeforeEach
	void setup(){
		dicePool = mock(DicePool.class);
		//The saving throw also works with the result given from the wound roll
		when(dicePool.getTotal()).thenReturn(0f);
		when(dicePool.getResult()).thenReturn(total);
		
		unit = mock(Unit.class);
		weapon = mock(Weapon.class);
		rules = mock(CombatRules.class);
		enemy = mock(Enemy.class);
	}

	@Test @DisplayName("Test Feel No Pain")
	void testFeelNoPain() {
		when(enemy.getFeelNoPain()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new FeelNoPainDiceRoll(unit, weapon, enemy, rules);
		val resultPool = diceRoll.roll(dicePool);
		val feelNoPainCalc = total - (total * Probability.FIVE_UP);
		
		assertEquals(feelNoPainCalc, resultPool.getResult());
	}
	
	@Test @DisplayName("Melter")
	void testMelter() {
		val melterModifier = (byte) 2;
		when(weapon.getMelter()).thenReturn(melterModifier);
		
		val diceRoll = new FeelNoPainDiceRoll(unit, weapon, enemy, rules);
		val resultPool = diceRoll.roll(dicePool);
		
		assertEquals(total + (total * melterModifier), resultPool.getResult());
	}
	
	

}

package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.CombatRules;
import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import core.combat.DicePool;
import core.combat.DiceRoll;
import core.combat.HitDiceRoll;
import lombok.val;

@TestMethodOrder(MethodOrderer.Random.class)
class HitDiceRollTest {
	private float total = 10;
	private DicePool dicePool;
	private Unit unit;
	private CombatRules rules;
	private Enemy enemy;
	private Weapon weapon;
	
	private DiceRoll hitRoll;
	
	@BeforeEach
	void setup(){
		dicePool = mock(DicePool.class);
		when(dicePool.total()).thenReturn(total);
		when(dicePool.result()).thenReturn(0f);
		
		unit = mock(Unit.class);
		weapon = mock(Weapon.class);
		rules = mock(CombatRules.class);
		enemy = mock(Enemy.class);
	}
	
	@Test
	void testWeaponCanHit() {
		assertEquals(total * Probability.THREE_UP, _result(Probability.THREE_UP));
	}
	
	@Test 
	void testReroll() {
		when(rules.rerollHitRoll()).thenReturn(true);
		
		val hits = total * Probability.THREE_UP;
		val misses = total - hits;
		val sum = hits + (misses * Probability.THREE_UP);
		
		assertEquals(sum, _result(Probability.THREE_UP));
	}
	
	@Test
	void testRerollPartial() {
		when(rules.rerollOnesToHit()).thenReturn(true);
		
		val hits = total * Probability.THREE_UP;
		val misses = total * Probability.SIX_UP;
		val sum = hits + (misses * Probability.THREE_UP);
		
		assertEquals(sum, _result(Probability.THREE_UP));
	}
	
	@Test 
	void testAddOneToHit() {
		when(rules.addOneToHitRoll()).thenReturn(true);
		assertEquals(total * Probability.TWO_UP, _result(Probability.THREE_UP));
	}
	
	@Test 
	void testSubtractOneFromHit() {
		when(rules.subtractOneFromHitRoll()).thenReturn(true);
		assertEquals(total * Probability.FOUR_UP, _result(Probability.THREE_UP));
	}
	
	@Test 
	void testBothModifiers() {
		when(rules.subtractOneFromHitRoll()).thenReturn(true);
		when(rules.addOneToHitRoll()).thenReturn(true);
		assertEquals(total * Probability.THREE_UP, _result(Probability.THREE_UP));
	}
	
	
	private float _result(float toHit) {
		when(weapon.getToHit()).thenReturn(toHit);
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		return hitRoll.roll(dicePool).result();
	}
}

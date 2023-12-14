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
import core.combat.WoundDiceRoll;
import lombok.val;

@TestMethodOrder(MethodOrderer.Random.class)
class WoundDiceRollTest {
	
	private float total = 10;
	private DicePool dicePool;
	private Unit unit;
	private CombatRules rules;
	private Enemy enemy;
	private Weapon weapon;
	
	private DiceRoll woundRoll;
	
	@BeforeEach
	void setup(){
		dicePool = mock(DicePool.class);
		//The wound roll uses the result of the hit roll
		when(dicePool.total()).thenReturn(0f);
		when(dicePool.result()).thenReturn(total);
		
		unit = mock(Unit.class);
		weapon = mock(Weapon.class);
		rules = mock(CombatRules.class);
		enemy = mock(Enemy.class);
	}
	
	//We test the different Wounding probabilities of the compare method
	@Test void testTwoUp() { assertEquals(total * Probability.TWO_UP, _result(10, 5)); }
	@Test void testThreeUp() { assertEquals(total * Probability.THREE_UP, _result(9, 5)); }
	@Test void testFourUp() { assertEquals(total * Probability.FOUR_UP, _result(5, 5));}
	@Test void testFiveUp() { assertEquals(total * Probability.FIVE_UP, _result(4, 5));}
	@Test void testSixUp() { assertEquals(total * Probability.SIX_UP, _result(3, 6)); }
	
	@Test 
	void rerollWoundRoll() {
		when(rules.rerollWoundRoll()).thenReturn(true);
		val successes = total * Probability.FIVE_UP;
		val rerolls = total - successes;
		val rerolledWounds = rerolls * Probability.FIVE_UP;
		assertEquals(successes + rerolledWounds, _result(4,5));
	}
	
	@Test 
	void rerollPartialWoundRoll() {
		when(rules.rerollOnesToWound()).thenReturn(true);
		val successes = total * Probability.FIVE_UP;
		val rerolls = total * Probability.SIX_UP;
		val rerolledWounds = rerolls * Probability.FIVE_UP;
		assertEquals(successes + rerolledWounds, _result(4,5));
	}
	
	@Test 
	void addOneToWound() {
		when(rules.addOneToWoundRoll()).thenReturn(true);
		val successes = total * Probability.FOUR_UP;
		assertEquals(successes, _result(4,5));
	}
	
	@Test 
	void subtractOneFromWound() {
		when(rules.subtractOneFromWoundRoll()).thenReturn(true);
		val successes = total * Probability.SIX_UP;
		assertEquals(successes, _result(4,5));
	}
	
	@Test 
	void addOneToWoundAndSubtractOneFromWound() {
		when(rules.addOneToWoundRoll()).thenReturn(true);
		when(rules.subtractOneFromWoundRoll()).thenReturn(true);
		val successes = total * Probability.FIVE_UP;
		assertEquals(successes, _result(4,5));
	}
	
	
	private float _result(int strength, int toughness) {
		when(enemy.getToughness()).thenReturn((byte)toughness);
		when(weapon.getStrength()).thenReturn((byte)strength);
		woundRoll = new WoundDiceRoll(unit, weapon, enemy, rules);
		return woundRoll.roll(dicePool).result();
	}

}

package unittests.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.function.Function;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Enemy;
import core.Probability;
import core.Unit;
import core.Weapon;
import core.combat.CombatRules;
import core.combat.DicePool;
import core.combat.DiceRoll;
import core.combat.HitDicePool;
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
	void setup() {
		dicePool = mock(DicePool.class);
		when(dicePool.getTotal()).thenReturn(total);
		when(dicePool.getResult()).thenReturn(0f);

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

	@Test 
	void testLethalHits() {
		when(rules.lethalHits()).thenReturn(true);
		
		val lethalHits = total * Probability.SIX_UP;
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(lethalHits, hitDicePool.getLethalHits());
	}

	@Test 
	void testLethalHitsAndReroll() {
		when(rules.lethalHits()).thenReturn(true);
		when(weapon.getToHit()).thenReturn(Probability.THREE_UP);
		when(rules.rerollHitRoll()).thenReturn(true);
		
		val hits = total * Probability.THREE_UP;
		val misses = total - hits;
		val lethalHits = (total * Probability.SIX_UP) + (misses * Probability.SIX_UP);
		
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(lethalHits, hitDicePool.getLethalHits());
	}

	@Test
	void testLethalHitsAndRerollPartial() {
		when(rules.lethalHits()).thenReturn(true);
		when(rules.rerollOnesToHit()).thenReturn(true);
		
		val rerolls = total * Probability.SIX_UP;
		val lethalHits = rerolls + (rerolls * Probability.SIX_UP);
		
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(lethalHits, hitDicePool.getLethalHits());
	}

	@Test
	 void testTorrent() {
		 when(rules.torrent()).thenReturn(true);
		 assertEquals(total, _result(Probability.THREE_UP));
	 }

	@Test
	 void testSustainedHits() {
		when(rules.sustainedHits()).thenReturn(true);
		when(weapon.getToHit()).thenReturn(Probability.THREE_UP);
		val sustainedHitsModificator = (byte) 2;
		when(weapon.getSustainedHits()).thenReturn(sustainedHitsModificator);
		val sustainedHits = total * Probability.SIX_UP;
		val hits = total * Probability.THREE_UP + (sustainedHits * sustainedHitsModificator);
					
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(hits, hitDicePool.getResult());
	 }
	
	@Test
	 void testSustainedHitsRerollPartial() {
		when(rules.sustainedHits()).thenReturn(true);
		when(weapon.getToHit()).thenReturn(Probability.THREE_UP);
		val sustainedHitsModificator = (byte) 2;
		when(weapon.getSustainedHits()).thenReturn(sustainedHitsModificator);
		when(rules.rerollOnesToHit()).thenReturn(true);
		
		val hits = total * Probability.THREE_UP;
		val misses = total * Probability.SIX_UP;
		val sustainedHits = misses * sustainedHitsModificator;
		
		val rerolls = misses * Probability.THREE_UP;
		val rerolledSustainedHits = (misses * Probability.SIX_UP) * sustainedHitsModificator;
		val sum = hits + rerolls + sustainedHits + rerolledSustainedHits;
					
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(sum, hitDicePool.getResult());
	 }
	
	@Test 
	void testSustainedHitsReroll() {
		when(rules.sustainedHits()).thenReturn(true);
		when(weapon.getToHit()).thenReturn(Probability.THREE_UP);
		val sustainedHitsModificator = (byte) 2;
		when(weapon.getSustainedHits()).thenReturn(sustainedHitsModificator);
		when(rules.rerollHitRoll()).thenReturn(true);
		
		val hits = total * Probability.THREE_UP;
		val misses = total - hits;
		val sustainedHits = (total * Probability.SIX_UP) * sustainedHitsModificator;
		
		val rerolls = misses * Probability.THREE_UP;
		val rerolledSustainedHits = (misses * Probability.SIX_UP) * sustainedHitsModificator;
		val sum = hits + rerolls + sustainedHits + rerolledSustainedHits;
					
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		HitDicePool hitDicePool = (HitDicePool) hitRoll.roll(dicePool);
		assertEquals(sum, hitDicePool.getResult());
	}

	private float _result(float toHit) {
		when(weapon.getToHit()).thenReturn(toHit);
		hitRoll = new HitDiceRoll(unit, weapon, enemy, rules);
		return hitRoll.roll(dicePool).getResult();
	}
}

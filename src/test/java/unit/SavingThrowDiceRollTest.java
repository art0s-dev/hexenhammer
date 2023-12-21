package unit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import core.Enemy;
import core.Enemy.SpecialRuleEnemy;
import core.combat.CombatRules;
import core.combat.DicePool;
import core.combat.SavingThrowDiceRoll;
import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

@TestMethodOrder(MethodOrderer.Random.class)
class SavingThrowDiceRollTest {
	
	private float total = 10;
	private DicePool dicePool;
	private Unit unit;
	private CombatRules combatRules;
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
		combatRules = mock(CombatRules.class);
		enemy = mock(Enemy.class);
	}

	@Test @DisplayName("Test saving throws - normal - without modifier")
	void testArmorSave() {
		when(enemy.getArmorSave()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new SavingThrowDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		val missedSavingThrows = total - (total * Probability.FIVE_UP);
		
		assertEquals(missedSavingThrows, resultPool.getResult());
	}
	
	@Test @DisplayName("Test saving throws - with mods - no armor save")
	void testArmorPen() {
		when(weapon.getArmorPenetration()).thenReturn((byte) 2);
		when(enemy.getArmorSave()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new SavingThrowDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		
		assertEquals(total, resultPool.getResult());
	}
	
	@Test @DisplayName("Test saving throws - with mods - invul save")
	void testInvulSave() {
		when(weapon.getArmorPenetration()).thenReturn((byte) 3);
		when(enemy.getInvulnerableSave()).thenReturn(Probability.FIVE_UP);
		when(enemy.getArmorSave()).thenReturn(Probability.FIVE_UP);
		
		val diceRoll = new SavingThrowDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		val missedSavingThrows = total - (total * Probability.FIVE_UP);
		
		assertEquals(missedSavingThrows, resultPool.getResult());
	}
	
	@Test @DisplayName("Test saving throws - with mods - no invul - cover")
	void testCover() {
		when(enemy.getArmorSave()).thenReturn(Probability.FIVE_UP);
		when(enemy.has(SpecialRuleEnemy.HAS_COVER)).thenReturn(true);
		
		val diceRoll = new SavingThrowDiceRoll(unit, weapon, enemy, combatRules);
		val resultPool = diceRoll.roll(dicePool);
		val missedSavingThrows = total - (total * Probability.FOUR_UP);
		
		assertEquals(missedSavingThrows, resultPool.getResult());
	}

}

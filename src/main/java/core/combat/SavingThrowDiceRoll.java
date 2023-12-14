package core.combat;

import java.util.Collections;
import java.util.HashMap;

import core.CombatRules;
import core.Enemy;
import core.Enemy.SpecialRuleProfile;
import core.Probability;
import core.Unit;
import core.Weapon;
import lombok.val;

/**
 * Makes Saving throws for an Enemy after determining the wounds
 * @see WoundDiceRoll
 */
public final class SavingThrowDiceRoll extends DiceRoll {

	public SavingThrowDiceRoll(Unit unit, Weapon weapon, Enemy enemy, CombatRules rules) {
		super(unit, weapon, enemy, rules);
	}

	public DicePool roll(DicePool dicePool) {
		val total = dicePool.result();
		val armourSave = enemy.getArmorSave();
		val invulSave = enemy.getInvulnerableSave();
		
		val cover = enemy.has(SpecialRuleProfile.HAS_COVER) ? 1 : 0;
		val modifiedArmourSave = (byte) (ARMOR_SAVES.get(armourSave) - weapon.getArmorPenetration() + cover);
		val armorSaveProbability = modifiedArmourSave / 6f;
		val mustTakeInvulSave = invulSave > armorSaveProbability;
		val save = mustTakeInvulSave ? invulSave : armorSaveProbability;
		
		val missedSavingThrows = total - (total * save);
		return new DicePool(total, missedSavingThrows); 
	}
	
	/**
	 * The armor save characteristica of an enemy unit tells us how
	 * tanky a unit is. This implementation subtracts the armor penetration value
	 * from the weapon from the given values, until there is no armor save left
	 */
	private static final HashMap<Float,Byte> ARMOR_SAVES = new HashMap<>();
	static {
		ARMOR_SAVES.put(Probability.SIX_UP, (byte) 1);
		ARMOR_SAVES.put(Probability.FIVE_UP, (byte) 2);
		ARMOR_SAVES.put(Probability.FOUR_UP, (byte) 3);
		ARMOR_SAVES.put(Probability.THREE_UP, (byte) 4);
		ARMOR_SAVES.put(Probability.TWO_UP, (byte) 5);
		Collections.unmodifiableMap(ARMOR_SAVES);
	}

}

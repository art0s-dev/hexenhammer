package core;

import java.util.List;

import lombok.Builder;

/**
 * The Combat Results reflects the variables 
 * used in the underlying implementation of the combat mechanic.
 * It can be used to show the results to the user.
 * A combat results always differs from weapon to weapon
 */
@Builder
public class CombatResult {
		Weapon weapon;
		int quantity;
		float attacks;
		float chanceToHit;
		float hits;
		float lethalHits;
		float missedHits;
		float probabilityToWound;
		float wounds;
		float probabilityToSave;
		float missedSaves;
		float damageMultiplier;
		float damagePotential;
		float woundsAfterFeelNoPain;
		float damage;
		
		public static float getOverallDamage(List<CombatResult> results) {
			float damage = 0;
			for(CombatResult result: results) {
				damage += result.damage;
			}
				
			return damage;
		}
}

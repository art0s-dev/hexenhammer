package core.combat;

/**
 * These are the Features that can occure during a combat sequence
 * It combines all Special rules from the unit, the enemy and the weapon entity
 * in order to provide a good readable interface
 */
public record CombatRules(
	boolean addOneToHitRoll,
	boolean subtractOneFromHitRoll,
	boolean lethalHits,
	boolean rerollOnesToHit,
	boolean rerollHitRoll,
	boolean torrent,
	boolean addOneToWoundRoll,
	boolean subtractOneFromWoundRoll,
	boolean rerollOnesToWound,
	boolean rerollWoundRoll,
	boolean antiTypeWeapon,
	boolean enemyHasCover,
	boolean ignoreCover,
	boolean sustainedHits,
	boolean melter
) {
};
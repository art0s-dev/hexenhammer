package core.combat;

/**
 * Base Data Structure to be consumed by the DiceRoll mechanics
 * It should only contain informations about the dice pool itself
 */
public record DicePool(float total, float result) {}

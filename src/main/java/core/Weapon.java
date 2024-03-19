package core;

import java.util.HashSet;
import java.util.Optional;

import arch.Model;
import core.Probability.Dice;
import core.Unit.Type;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.val;

/**
 * The Weapons should just contain values
 * and be attached to units. This entity will be used for comparation 
 * against the Profile entity.  
 */
@Builder @Data
public class Weapon extends Model {
	
	private String name;
	@Builder.Default private Optional<UserNumberInput> attackInput = Optional.empty();
	@Builder.Default private float toHit = Probability.SIX_UP;
	@Builder.Default private byte strength = 1;
	@Builder.Default private byte armorPenetration = 0;
	@Builder.Default private Optional<UserNumberInput> damageInput = Optional.empty();
	@Builder.Default private byte sustainedHits = 0;
	@Builder.Default private byte melter = 0;
	@Builder.Default private Phase phase = Phase.SHOOTING; 
	
	/**
	 * calculates the number of attacks dependant on
	 * the userNumberInput to provide the user with the choice to either enter
	 * a attacknumber or to generate a number through a quantity and a die
	 */
	public float getAttacks() {
		if(attackInput.isEmpty()) {
			return 0f;
		}
		
		UserNumberInput attackInput = this.attackInput.orElseThrow();
		
		if(attackInput.useDice()) {
			val diceValue = attackInput.dice().equals(Dice.d3) 
					? Probability.MEDIAN_D3 
					: Probability.MEDIAN_D6;
			
			return attackInput.quantity() * diceValue;
		}
		
		return attackInput.number();
	}
	
	/**
	 * calculates the damages based on the
	 * userNumberInput for the damage. Same one as getAttacks(), 
	 * we want the user to choose either to enter a number of attacks or
	 * a quantity and a die.
	 */
	public float getDamage() {
		if(damageInput.isEmpty()) {
			return 0f;
		}
		
		UserNumberInput damageInput = this.damageInput.orElseThrow();
		
		if(damageInput.useDice()) {
			val diceValue = damageInput.dice().equals(Dice.d3) 
					? Probability.MEDIAN_D3 
					: Probability.MEDIAN_D6;
			
			return damageInput.quantity() * diceValue;
		}
		
		return damageInput.number();
	}
	
	/**
	 * This is a tuple explains the specific efficiency for wound rolls
	 * against a certain profile type.
	 */
	public record AntiType(Type type, Float probability) {}
	@Getter @Setter @Builder.Default private Optional<AntiType> antiType = Optional.empty();
	
	/**
	 * Distinguishes the weapon between a combat and a shooting weapon 
	 */
	public enum Phase { SHOOTING, FIGHT, BOTH }
	
	/**
	 * The Set of special rules each weapon can have
	 * it applies after the global unit special rules
	 */
	@Builder.Default
	private HashSet<SpecialRuleWeapon> specialRules = new HashSet<>();
	public enum SpecialRuleWeapon{
		TORRENT,
		HEAVY_AND_UNIT_REMAINED_STATIONARY,
		REROLL_WOUND_ROLL, 
		LETHAL_HITS,
		DEVASTATING_WOUNDS,
	}
	
	public void add(SpecialRuleWeapon specialRule) {
		this.specialRules.add(specialRule);
	}
	
	public void remove(SpecialRuleWeapon specialRule) {
		this.specialRules.remove(specialRule);
	}
	
	public boolean has(SpecialRuleWeapon specialRule) {
		return this.specialRules.contains(specialRule);
	}
}

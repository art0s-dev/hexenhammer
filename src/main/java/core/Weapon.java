package core;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import arch.Model;
import core.Probability.Dice;
import core.Unit.Phase;
import core.Unit.SpecialRuleUnit;
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
@Builder
public class Weapon extends Model {
	
	@Getter @Setter @Builder.Default private String name;
	@Getter @Setter @Builder.Default private Optional<UserNumberInput> attackInput = Optional.empty();
	@Getter @Setter @Builder.Default private float toHit = Probability.SIX_UP;
	@Getter @Setter @Builder.Default private byte strength = 1;
	@Getter @Setter @Builder.Default private byte armorPenetration = 0;
	@Getter @Setter @Builder.Default private Optional<UserNumberInput> damageInput = Optional.empty();
	@Getter @Setter @Builder.Default private byte sustainedHits = 0;
	@Getter @Setter @Builder.Default private byte melter = 0;
	@Getter @Setter @Builder.Default private Range range = Range.SHOOTING;  
	
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
		
		if(attackInput.isUseDice()) {
			val diceValue = attackInput.getDice().equals(Dice.d3) 
					? Probability.MEDIAN_D3 
					: Probability.MEDIAN_D6;
			
			return attackInput.getDiceQuantity() * diceValue;
		}
		
		return attackInput.getFixedNumber();
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
		
		if(damageInput.isUseDice()) {
			val diceValue = damageInput.getDice().equals(Dice.d3) 
					? Probability.MEDIAN_D3 
					: Probability.MEDIAN_D6;
			
			return damageInput.getDiceQuantity() * diceValue;
		}
		
		return damageInput.getFixedNumber();
	}
	
	/**
	 * This is a tuple explains the specific efficiency for wound rolls
	 * against a certain profile type.
	 */
	public record AntiType(Type type, Float probability) {}
	@Getter @Setter @Builder.Default private Optional<AntiType> antiType = Optional.empty();
	
	/**
	 * Defines a range on the weapon, which decides how the weapon is used
	 * the user can choose to use only weapons with shooting, meele or bother
	 * weapons can only be shooting or meelee
	 */
	public enum Range { SHOOTING, MELEE }
	
	/**
	 * The Set of special rules each weapon can have
	 * it applies after the global unit special rules
	 */
	@Builder.Default
	private HashSet<SpecialRuleWeapon> specialRules = new HashSet<>();
	public enum SpecialRuleWeapon{
		TORRENT,
		HEAVY_AND_UNIT_REMAINED_STATIONARY,
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

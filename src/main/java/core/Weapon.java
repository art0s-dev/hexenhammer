package core;

import static core.Probability.MEDIAN_D3;
import static core.Probability.MEDIAN_D6;
import static core.Probability.Dice.d3;
import static core.UserNumberInput.withNumber;
import static java.util.Optional.of;
import static java.util.Optional.empty;

import java.util.HashSet;
import java.util.Optional;

import arch.Model;
import core.Unit.Type;
import lombok.Builder;
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
	
	@Getter private int id;
	@Getter @Setter @Builder.Default private String name = "";
	@Getter @Setter @Builder.Default private Optional<UserNumberInput> attackInput = of(withNumber(0));
	@Getter @Setter @Builder.Default private float toHit = Probability.SIX_UP;
	@Getter @Setter @Builder.Default private int strength = 1;
	@Getter @Setter @Builder.Default private int armorPenetration = 0;
	@Getter @Setter @Builder.Default private Optional<UserNumberInput> damageInput = of(withNumber(0));
	@Getter @Setter @Builder.Default private int sustainedHits = 0;
	@Getter @Setter @Builder.Default private int melter = 0;
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
			val diceValue = attackInput.getDice().equals(d3) 
					? MEDIAN_D3 
					: MEDIAN_D6;
			
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
			val diceValue = damageInput.getDice().equals(d3) 
					? MEDIAN_D3 
					: MEDIAN_D6;
			
			return damageInput.getDiceQuantity() * diceValue;
		}
		
		return damageInput.getFixedNumber();
	}
	
	/**
	 * This is a tuple explains the specific efficiency for wound rolls
	 * against a certain profile type.
	 */
	public record AntiType(Type type, Float probability) {}
	@Getter @Setter @Builder.Default private Optional<AntiType> antiType = empty();
	
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

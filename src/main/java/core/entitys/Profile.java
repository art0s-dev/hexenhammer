package core.entitys;

import lombok.Builder;
import lombok.Data;
import lombok.Generated;

/**
 * This class just contains values for weapons
 * to compare against. The units that use a certain profile should be targets.
 */
@Data @Generated @Builder
public class Profile {
	private int toughness;
	private int wounds;
	private double armorSave;
	@Builder.Default private int hitPoints = 1;
	@Builder.Default private double invulnerableSave = Probability.NONE;
	@Builder.Default private double feelNoPain = Probability.NONE;
}

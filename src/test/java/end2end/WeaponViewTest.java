package end2end;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.swt.widgets.Shell;

import core.Probability;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;
import utils.I18n;
import weapon.WeaponController;
import weapon.WeaponList;
import weapon.WeaponRepository;
import weapon.WeaponView;

public class WeaponViewTest extends SWTEnd2EndTestcase {
	/**
	 * Just the main method for running an SWT application
	 */
	public static void main(String[] args) {
		Shell shell = before();
		test(shell);
		after(shell);
	}

	private static void test(Shell shell) {
		I18n i18n = new I18n();
		i18n.setLanguage(I18n.german());
		
		Weapon bolter = Weapon.builder()
				.name("bolter")
				.attackInput(Optional.of(UserNumberInput.withNumber(2)))
				.strength(4)
				.toHit(Probability.THREE_UP)
				.armorPenetration( 1)
				.damageInput(Optional.of(UserNumberInput.withNumber(1)))
				.range(Range.SHOOTING)
				.build();
		
		Weapon chainsword = Weapon.builder()
				.name("chainsword")
				.attackInput(Optional.of(UserNumberInput.withNumber(2)))
				.strength(4)
				.toHit(Probability.FOUR_UP)
				.armorPenetration(0)
				.range(Range.MELEE)
				.damageInput(Optional.of(UserNumberInput.withNumber(1)))
				.build();
		
		WeaponView view = new WeaponView(shell, new I18n());
		WeaponRepository repo = mock(WeaponRepository.class);
		
		ArrayList<Weapon> list = new ArrayList<>();
		list.add(bolter);
		list.add(chainsword);
		
		when(repo.load()).thenReturn(new WeaponList(list));
		WeaponController controller = new WeaponController(view, repo);
		controller.loadModels();
		controller.initView();
		controller.injectListener();
	}
}


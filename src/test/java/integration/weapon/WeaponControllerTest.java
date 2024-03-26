package integration.weapon;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Event;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.Probability;
import core.Unit;
import core.UserNumberInput;
import core.Weapon;
import core.Weapon.Range;
import unittests.gui.SWTGuiTestCase;
import utils.I18n;
import weapon.WeaponController;
import weapon.WeaponList;
import weapon.WeaponRepository;
import weapon.WeaponView;

class WeaponControllerTest extends SWTGuiTestCase{
	
	Weapon bolter;
	Weapon chainsword;
	
	@BeforeEach
	void setup() {
		bolter = Weapon.builder()
				.name("bolter")
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.toHit(Probability.THREE_UP)
				.strength((byte)4)
				.armorPenetration((byte) 0)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.range(Range.SHOOTING)
				.build();
		
		chainsword = Weapon.builder()
				.name("chainsword")
				.attackInput(Optional.of(UserNumberInput.withNumber((byte) 2)))
				.toHit(Probability.FOUR_UP)
				.strength((byte)4)
				.armorPenetration((byte) 0)
				.range(Range.MELEE)
				.damageInput(Optional.of(UserNumberInput.withNumber((byte) 1)))
				.build();
	}

	@Test
	void testAddingAWeapon() {
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
		
		view.getAddButton().notifyListeners(SWT.Selection, new Event());
		assertEquals(3, view.getSelectionList().getItemCount());
	}

}

package weapon;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import arch.BaseView;
import arch.Model;
import arch.ModelList;
import lombok.Getter;
import utils.ButtonFactory;
import utils.I18n;

public class WeaponView extends BaseView {
	protected final static String LIST_VIEW = "Your Weapons";
	protected final static String GROUP_NAME = "Profile";
	protected static final String TAB_NAME = "Weapons";
	private static final String INPUT_ATTACKS = "Attacks";
	private static final String TO_HIT = "To hit";
	
	@Getter private Text inputAttacks;
	@Getter private Combo inputToHit;
	@Getter private Text inputStrenght;
	@Getter private Text inputArmorPenetration;
	@Getter private Text inputDamage;
	
	public WeaponView(Composite parent, I18n i18n) {
		super(parent, i18n);
	}
	
	@Override
	public void draw() {
		super.draw();
		_initializeInputFields();
	}

	public void drawList(ModelList modelList) {
		// TODO Auto-generated method stub
	}

	public void drawEditor(Model model) {
		
	}

	private void _initializeInputFields() {
		ButtonFactory buttonFactory = new ButtonFactory(entityEditorGroup);
		inputAttacks = buttonFactory.createTextInput(INPUT_ATTACKS);
		_initializeToHit();
	}
	
	private void _initializeToHit() {
		Label labelToHit = new Label(entityEditorGroup, SWT.NONE);
		labelToHit.setText(TO_HIT);
		inputToHit = new Combo(entityEditorGroup, SWT.NONE);
		inputToHit.add("2+");
		inputToHit.add("3+");
		inputToHit.add("4+");
		inputToHit.add("5+");
		inputToHit.add("6+");
	}
}

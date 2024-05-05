package utils;

import static core.Probability.FIVE_UP;
import static core.Probability.FOUR_UP;
import static core.Probability.SIX_UP;
import static core.Probability.THREE_UP;
import static core.Probability.TWO_UP;
import static core.Probability.Dice.d3;
import static core.Probability.Dice.d6;
import static core.Unit.Type.INFANTRY;
import static core.Unit.Type.MONSTER;
import static core.Unit.Type.VEHICLE;
import static org.eclipse.swt.SWT.CHECK;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.RADIO;
import static utils.Theme.DEFAULT_GRID_DATA;
import static utils.Theme.DEFAULT_VERTICAL_INDENT_COMBO;

import java.util.HashMap;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;

import core.Probability;
import core.Unit;
import core.Probability.Dice;

/**
 * Use this class if you need to paint like 20 
 * Checkboxes and you don't want to manually
 * define all buttons
 */
public class GuiFactory {

	/**
	 * The possible max for an input field
	 * its 40 because no value in the rulebook of 40k is
	 * really greater than 40
	 */
	private final static int INPUT_MAX = 40;
	
	/**
	 * the possible min value for an input field
	 */
	private final static int INPUT_MIN = 0;
	
	private final Composite parent;
	
	public GuiFactory(Composite parent) {
		this.parent = parent;
	}
	
	public Button createRadioButton() {
		return _createButton(RADIO);
	}
	
	public Button createCheckBox() {
		return _createButton(CHECK);
	}
	
	public Button createTextButton() {
		return _createButton(NONE);
	}
	
	public Text createTextInput(String label) {
		Label inputLabel = createLabel();
		inputLabel.setText(label);
		
		Text text = new Text(parent, NONE);
		text.setLayoutData(DEFAULT_GRID_DATA);
		return text;
	}
	
	public Spinner createNumberInput() {
		Spinner spinner = new Spinner(parent, NONE);
		GridData comboLayout = DEFAULT_GRID_DATA;
		comboLayout.heightHint = 2 * DEFAULT_VERTICAL_INDENT_COMBO;
		spinner.setLayoutData(comboLayout);
		spinner.setMaximum(INPUT_MAX);
		spinner.setMinimum(INPUT_MIN);
		return spinner;
	}
	
	public Combo createProbabilityCombo() {
		Combo probabilityCombo = new Combo(parent, NONE);
		GridData comboLayout = DEFAULT_GRID_DATA;
		comboLayout.verticalIndent = DEFAULT_VERTICAL_INDENT_COMBO;
		probabilityCombo.setLayoutData(comboLayout);
		COMBO_PROBABILITY_LISTING.forEach((key, value) -> probabilityCombo.add(value, key));
		probabilityCombo.select(0);
		return probabilityCombo;
	}
	
	/**
	 * The list of all possible probabilities, that can be selected by the combos
	 */
	private final static HashMap<Integer, String> COMBO_PROBABILITY_LISTING = new HashMap<>();
	static {
		COMBO_PROBABILITY_LISTING.put(0, "-");
		COMBO_PROBABILITY_LISTING.put(1, "6+");
		COMBO_PROBABILITY_LISTING.put(2, "5+");
		COMBO_PROBABILITY_LISTING.put(3, "4+");
		COMBO_PROBABILITY_LISTING.put(4, "3+");
		COMBO_PROBABILITY_LISTING.put(5, "2+");
	}
	
	public final static float mapComboSelectionToProbability(int index) {
		return switch(index) {
			case 1 -> SIX_UP;
			case 2 -> FIVE_UP;
			case 3 -> FOUR_UP;
			case 4 -> THREE_UP;
			case 5 -> TWO_UP;
			default -> Probability.NONE;
		};
	}
	
	public final static int mapProbabilityToComboSelection(float probability) {
		
		if(probability == SIX_UP) {
			return 1;
		}
		
		if(probability == FIVE_UP) {
			return 2;
		}
		
		if(probability == FOUR_UP) {
			return 3;
		}
		
		if(probability == THREE_UP) {
			return 4;
		}
		
		if(probability == TWO_UP) {
			return 5;
		}
		
		return 0;
	}
	
	public Combo createDiceCombo() {
		Combo diceChooser = new Combo(parent, SWT.NONE);
		COMBO_DICES.forEach((key, value) -> diceChooser.add(value));
		GridData comboGridData = new GridData(FILL, FILL, true, false);
		comboGridData.verticalIndent = DEFAULT_VERTICAL_INDENT_COMBO;
		diceChooser.setLayoutData(comboGridData);
		return diceChooser;
	}
	
	/**
	 * The list of all possible chosen dices
	 */
	private final static HashMap<Integer, String> COMBO_DICES= new HashMap<>();
	static {
		COMBO_DICES.put(0, "W3");
		COMBO_DICES.put(1, "W6");
	}
	
	public final static int mapDiceToComboSelection(Dice dice) {
		return switch(dice) {
			case d6 -> 1;
			default -> 0;
		};
	}
	
	public final static Dice mapComboSelectionToDice(int index) {
		return switch(index) {
			case 1 -> d6;
			default -> d3;
		};
	}
	
	public Label createLabel() {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(DEFAULT_GRID_DATA);
		return label;
	}
	
	private Button _createButton(int style) {
		Button button = new Button(parent, style);
		button.setLayoutData(Theme.DEFAULT_GRID_DATA);
		return button;
	}
	
	/**
	 * This is the listing for the unit types that can be chosen  
	 */
	public static HashMap<Integer, String> UNIT_TYPES = new HashMap<>();
	static {
		String prefix = "unit.UnitView.editor.unitType.";
		UNIT_TYPES.put(0, prefix + "infantry");
		UNIT_TYPES.put(1, prefix + "monster");
		UNIT_TYPES.put(2, prefix + "vehicle");
	}
	
	/**
	 * Maps the selectable Combo options of the unit type
	 * to an enum for further processing
	 */
	public static Unit.Type mapUnitTypeComboSelectionToEnum(int index) {
		return switch (index) {
			case 1 -> MONSTER;
			case 2 -> VEHICLE;
			default -> INFANTRY;
		};
	}
	
	/**
	 * Maps a given unit type to the combo selection index
	 */
	public static int mapTypeEnumToComboSelection(Unit.Type type) {
		boolean noTypeWasSet = type == null;
		if(noTypeWasSet) {
			return 0; 
		}
		
		return switch (type) {
			case MONSTER -> 1;
			case VEHICLE -> 2;
			default -> 0; //Infantry
		};
	}
	
}

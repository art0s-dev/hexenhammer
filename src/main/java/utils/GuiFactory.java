package utils;

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
		return _createButton(SWT.RADIO);
	}
	
	public Button createCheckBox() {
		return _createButton(SWT.CHECK);
	}
	
	public Text createTextInput(String label) {
		Label inputLabel = createLabel();
		inputLabel.setText(label);
		
		Text text = new Text(parent, SWT.NONE);
		text.setLayoutData(Theme.DEFAULT_GRID_DATA);
		return text;
	}
	
	public Spinner createNumberInput() {
		Spinner spinner = new Spinner(parent, SWT.NONE);
		spinner.setLayoutData(Theme.DEFAULT_GRID_DATA);
		spinner.setMaximum(INPUT_MAX);
		spinner.setMinimum(INPUT_MIN);
		return spinner;
	}
	
	public Combo createProbabilityCombo() {
		Combo probabilityCombo = new Combo(parent, SWT.NONE);
		GridData comboLayout = Theme.DEFAULT_GRID_DATA;
		comboLayout.verticalIndent = Theme.DEFAULT_VERTICAL_INDENT_COMBO;
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
			case 1 -> Probability.SIX_UP;
			case 2 -> Probability.FIVE_UP;
			case 3 -> Probability.FOUR_UP;
			case 4 -> Probability.THREE_UP;
			case 5 -> Probability.TWO_UP;
			default -> Probability.NONE;
		};
	}
	
	public final static int mapProbabilityToComboSelection(float probability) {
		
		if(probability == Probability.SIX_UP) {
			return 1;
		}
		
		if(probability == Probability.FIVE_UP) {
			return 2;
		}
		
		if(probability == Probability.FOUR_UP) {
			return 3;
		}
		
		if(probability == Probability.THREE_UP) {
			return 4;
		}
		
		if(probability == Probability.TWO_UP) {
			return 5;
		}
		
		return 0;
	}
	
	public Combo createDiceCombo() {
		Combo diceChooser = new Combo(parent, SWT.NONE);
		diceChooser.add("W3");
		diceChooser.add("W6");
		GridData comboGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		comboGridData.verticalIndent = Theme.DEFAULT_VERTICAL_INDENT_COMBO;
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
			case 1 -> Dice.d6;
			default -> Dice.d3;
		};
	}
	
	public Label createLabel() {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(Theme.DEFAULT_GRID_DATA);
		return label;
	}
	
	private Button _createButton(int style) {
		Button button = new Button(parent, style);
		button.setLayoutData(Theme.DEFAULT_GRID_DATA);
		return button;
	}
	
}

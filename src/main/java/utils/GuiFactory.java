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

/**
 * Use this class if you need to paint like 20 
 * Checkboxes and you don't want to manually
 * define all buttons
 */
public class GuiFactory {

	public final static GridData DEFAULT_GRID_DATA = new GridData(SWT.FILL, SWT.FILL, true, false);
	public final static int DEFAULT_VERTICAL_INDENT_COMBO = 15;
	
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
	
	/**
	 * The parent to which the Gui factory attaches the created elements
	 */
	private final Composite parent;
	
	public GuiFactory(Composite parent) {
		this.parent = parent;
	}
	
	public Button createCheckBox() {
		Button button = new Button(parent, SWT.CHECK);
		button.setLayoutData(DEFAULT_GRID_DATA);
		return button;
	}
	
	public Text createTextInput(String label) {
		Label inputLabel = createLabel();
		inputLabel.setText(label);
		
		Text text = new Text(parent, SWT.NONE);
		text.setLayoutData(DEFAULT_GRID_DATA);
		return text;
	}
	
	public Spinner createNumberInput() {
		Spinner spinner = new Spinner(parent, SWT.NONE);
		spinner.setLayoutData(DEFAULT_GRID_DATA);
		spinner.setMaximum(INPUT_MAX);
		spinner.setMinimum(INPUT_MIN);
		return spinner;
	}
	
	public Combo createProbabilityCombo() {
		Combo probabilityCombo = new Combo(parent, SWT.NONE);
		GridData comboLayout = DEFAULT_GRID_DATA;
		comboLayout.verticalIndent = DEFAULT_VERTICAL_INDENT_COMBO;
		probabilityCombo.setLayoutData(comboLayout);
		COMBO_PROBABILITY_LISTING.forEach((key, value) -> probabilityCombo.add(value, key));
		probabilityCombo.select(0);
		return probabilityCombo;
	}
	
	public Label createLabel() {
		Label label = new Label(parent, SWT.NONE);
		label.setLayoutData(DEFAULT_GRID_DATA);
		return label;
	}
	
}

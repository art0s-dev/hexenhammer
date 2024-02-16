package unit;

import static utils.View.placeholder;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import arch.Model;
import arch.ModelList;
import arch.View;
import core.Unit;
import core.Unit.SpecialRuleUnit;
import lombok.Getter;
import lombok.val;
import utils.ButtonFactory;

public class UnitView implements View {
	
	//Dependencys
	private final TabFolder mainTab;
	private final Display display;
	
	//Language Strings
	private final static String UNIT_TAB_NAME = "Units";
	private final static String UNIT_LIST_VIEW = "Your units";
	private final static String UNIT_EDITOR_GROUP_NAME = "Special Rules";
	private final static String UNIT_EDITOR_UNIT_NAME = "Display name";
	private final static String UNIT_EDITOR_ADD_ONE_TO_HIT = "Add one to hit";
	private final static String UNIT_EDITOR_HAS_LETHAL_HITS = "Has Lethal hits";
	private final static String UNIT_EDITOR_REROLL_ONES_TO_HIT = "Reroll ones to hit";
	private final static String UNIT_EDITOR_REROLL_HIT_ROLL = "Reroll hit roll";
	private final static String UNIT_EDITOR_ADD_ONE_TO_WOUND = "Add one to wound";
	private final static String UNIT_EDITOR_REROLL_ONES_TO_WOUND = "Reroll ones to wound";
	private final static String UNIT_EDITOR_REROLL_WOUND_ROLL = "Reroll wound roll";
	private final static String UNIT_EDITOR_IGNORE_COVER = "Ignore cover";
	
	//"Puppet strings" for the controller
	@Getter private TabItem unitTab;
	@Getter private Text nameInput;
	@Getter private Button checkBoxAddOneToHit;
	@Getter private Button checkBoxLethalHits;
	@Getter private Button checkBoxRerollOnesToHit;
	@Getter private Button checkBoxRerollHitRoll;
	@Getter private Button checkBoxAddOneToWound;
	@Getter private Button checkBoxRerollOnesToWound;
	@Getter private Button checkBoxRerollWound;
	@Getter private Button checkBoxIgnoreCover;
	@Getter private List selectionList;
	
	//"Recycled" Widgets - you mostly have to use draw() before using these
	private Composite compositeUnitEditor;
	private Group unitListGroup;
	
	public UnitView(TabFolder mainTab) {
		this.mainTab = mainTab;
		this.display = mainTab.getDisplay();
	}
	
	@Override
	public void draw() {
		unitTab = new TabItem(mainTab, SWT.NONE);
		unitTab.setText(UNIT_TAB_NAME);
		
		Composite compositeUnits = new Composite(mainTab, SWT.NONE);
		compositeUnits.setLayout(new FillLayout());
		unitTab.setControl(compositeUnits);
		
		SashForm sashFormUnits = new SashForm(compositeUnits, SWT.HORIZONTAL);
		sashFormUnits.SASH_WIDTH = 3;
		sashFormUnits.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		
		val layout = new FillLayout();
		layout.marginHeight = 5;
		layout.marginWidth = 5;
		
		//List View
		Composite compositeUnitList = new Composite(sashFormUnits, SWT.NONE);
		compositeUnitList.setLayout(layout);
		compositeUnitList.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		unitListGroup = new Group(compositeUnitList, SWT.NONE); 
		unitListGroup.setText(UNIT_LIST_VIEW);
		unitListGroup.setLayout(new FillLayout());
		
		selectionList = new List(unitListGroup, SWT.NONE);
			
		//View for Editor 
		compositeUnitEditor = new Composite(sashFormUnits, SWT.NONE);
		compositeUnitEditor.setLayout(layout);
		compositeUnitEditor.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		
		Group unitEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
		unitEditorGroup.setText(UNIT_EDITOR_GROUP_NAME);
		GridLayout fillLayoutUnitEditor = new GridLayout(4, true);
		fillLayoutUnitEditor.marginHeight = 5;
		fillLayoutUnitEditor.marginWidth = 5;
		unitEditorGroup.setLayout(fillLayoutUnitEditor); 
		
		Label nameLabel = new Label(unitEditorGroup, SWT.NONE);
		nameLabel.setText(UNIT_EDITOR_UNIT_NAME); 
		nameInput = new Text(unitEditorGroup, SWT.NONE);
		nameInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		nameInput.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		
		placeholder(6, unitEditorGroup);
		
		ButtonFactory buttonFactory = new ButtonFactory(unitEditorGroup);
		checkBoxAddOneToHit = buttonFactory.createCheckBox(UNIT_EDITOR_ADD_ONE_TO_HIT);
		checkBoxLethalHits = buttonFactory.createCheckBox(UNIT_EDITOR_HAS_LETHAL_HITS);
		checkBoxRerollOnesToHit = buttonFactory.createCheckBox(UNIT_EDITOR_REROLL_ONES_TO_HIT);
		checkBoxRerollHitRoll = buttonFactory.createCheckBox(UNIT_EDITOR_REROLL_HIT_ROLL);
		
		checkBoxAddOneToWound = buttonFactory.createCheckBox(UNIT_EDITOR_ADD_ONE_TO_WOUND);
		checkBoxRerollOnesToWound = buttonFactory.createCheckBox(UNIT_EDITOR_REROLL_ONES_TO_WOUND);
		checkBoxRerollWound = buttonFactory.createCheckBox(UNIT_EDITOR_REROLL_WOUND_ROLL);
		checkBoxIgnoreCover = buttonFactory.createCheckBox(UNIT_EDITOR_IGNORE_COVER);
	}

	@Override
	public void drawEditor(Model model) {
		Unit unit = (Unit) model; 
		val formIsInitialized = model == null;
		
		if(formIsInitialized) {
			return;
		}
		
		val unitHasNoName = unit.getName() == null;
		nameInput.setText(unitHasNoName ? "" : unit.getName());
		
		checkBoxAddOneToHit.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_HIT));
		checkBoxLethalHits.setSelection(unit.has(SpecialRuleUnit.LETHAL_HITS));
		checkBoxRerollOnesToHit.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_HIT));
		checkBoxRerollHitRoll.setSelection(unit.has(SpecialRuleUnit.REROLL_HIT_ROLL));
		
		checkBoxAddOneToWound.setSelection(unit.has(SpecialRuleUnit.ADD_ONE_TO_WOUND));
		checkBoxRerollOnesToWound.setSelection(unit.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND));
		checkBoxRerollWound.setSelection(unit.has(SpecialRuleUnit.REROLL_WOUND_ROLL));
		checkBoxIgnoreCover.setSelection(unit.has(SpecialRuleUnit.IGNORE_COVER));
	}

	@Override
	public void drawList(ModelList modelList) { 
		UnitList unitList = (UnitList) modelList;
		selectionList.removeAll();
		
		for(Unit unit: unitList.getUnits()) {
			val unitHasNoName = unit.getName() == null;
			selectionList.add(unitHasNoName ? "" : unit.getName());
		}
	}

}

package unit;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
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
import utils.ImageServer;

public class UnitView implements View {
	
	//Dependencys
	private final TabFolder mainTab;
	private final ImageServer imageServer;
	
	//Language Strings
	private final static String TAB_NAME = "Units";
	private final static String LIST_VIEW = "Your units";
	private final static String GROUP_NAME = "Special Rules";
	private final static String UNIT_NAME = "Display name";
	private final static String ADD_ONE_TO_HIT = "Add one to hit";
	private final static String HAS_LETHAL_HITS = "Has Lethal hits";
	private final static String REROLL_ONES_TO_HIT = "Reroll ones to hit";
	private final static String REROLL_HIT_ROLL = "Reroll hit roll";
	private final static String ADD_ONE_TO_WOUND = "Add one to wound";
	private final static String REROLL_ONES_TO_WOUND = "Reroll ones to wound";
	private final static String REROLL_WOUND_ROLL = "Reroll wound roll";
	private final static String IGNORE_COVER = "Ignore cover";
	private final static String ADD = "Klick here, to add an entry to the chosen tab list";
	private final static String DELETE = "Klick here, to delete the selected entry from the chosen tab";
	
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
	@Getter private Button addButton;
	@Getter private Button deleteButton;
	
	//"Recycled" Widgets - you mostly have to use draw() before using these
	private Composite compositeUnitEditor;
	private Composite compositeUnits;
	private Composite compositeUnitList;
	private Group unitEditorGroup;
	
	public UnitView(TabFolder mainTab) {
		this.mainTab = mainTab;
		this.imageServer = new ImageServer(mainTab.getShell().getDisplay());
	}
	
	@Override
	public void draw() {
		_initalizeGeneralView();
		_initializeMenuBar();
		_initializeUnitList();
		_initalizeEditorView();
		_initializeCheckBoxes();
	}
	
	@Override
	public void drawList(ModelList modelList) { 
		UnitList unitList = (UnitList) modelList;
		selectionList.removeAll();
		
		val unitListIsEmpty = modelList == null;
		if(unitListIsEmpty) {
			return;
		}
		
		for(Unit unit: unitList.getUnits()) {
			val unitHasNoName = unit.getName() == null;
			selectionList.add(unitHasNoName ? "" : unit.getName());
		}
		
		selectionList.setSelection(0);
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
	
	private void _initalizeGeneralView() { 
		unitTab = new TabItem(mainTab, SWT.NONE);
		unitTab.setText(TAB_NAME);
		
		compositeUnits = new Composite(mainTab, SWT.NONE);
		unitTab.setControl(compositeUnits);
		compositeUnits.setLayout(new GridLayout(1, true));
		compositeUnits.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		compositeUnits.setLayout(new GridLayout(12, true));	
		
		compositeUnitList = new Composite(compositeUnits, SWT.NONE);
		GridData gridDataCompositeUnitList = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataCompositeUnitList.horizontalSpan = 4;
		compositeUnitList.setLayoutData(gridDataCompositeUnitList);
		compositeUnitList.setLayout(new GridLayout(1, true));
	}

	private void _initializeUnitList() {
		Group unitListGroup = new Group(compositeUnitList, SWT.VERTICAL); 
		unitListGroup.setText(LIST_VIEW);
		unitListGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		unitListGroup.setLayout(new GridLayout(1, true));
		
		Composite unitListComposite = new Composite(unitListGroup, SWT.NONE);
		unitListComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		unitListComposite.setLayout(new GridLayout(1, true));

		selectionList = new List(unitListComposite, SWT.NONE);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		selectionList.setLayoutData(listGridData);
	}

	private void _initializeMenuBar() {
		CoolBar menuBar = new CoolBar(compositeUnitList, SWT.NONE);
		GridData menuBarGridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		menuBarGridData.verticalSpan = 8;
		menuBar.setLayoutData(menuBarGridData);
		menuBar.setLayout(new GridLayout(2, true));
		
		addButton = new Button(menuBar, SWT.PUSH);
		addButton.setImage(imageServer.createImageForButton("plus"));
		addButton.setToolTipText(ADD);
		
		deleteButton = new Button(menuBar, SWT.PUSH);
		deleteButton.setImage(imageServer.createImageForButton("trash-can"));
		deleteButton.setToolTipText(DELETE);
	}
	
	private void _initalizeEditorView() { 
		compositeUnitEditor = new Composite(compositeUnits, SWT.NONE);
		GridData gridDataCompositeUnitEditor = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataCompositeUnitEditor.horizontalSpan = 8;
		compositeUnitEditor.setLayoutData(gridDataCompositeUnitEditor);
		compositeUnitEditor.setLayout(new GridLayout(1, true));
		
		unitEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
		unitEditorGroup.setText(GROUP_NAME);
		unitEditorGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		unitEditorGroup.setLayout(new GridLayout(4, true));
		
		Label nameLabel = new Label(unitEditorGroup, SWT.NONE);
		nameLabel.setText(UNIT_NAME); 
		nameInput = new Text(unitEditorGroup, SWT.NONE);
		GridData nameInputGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		nameInputGridData.horizontalSpan = 3;
		nameInput.setLayoutData(nameInputGridData);
		
		Label placeholder = new Label(unitEditorGroup, SWT.NONE);
		GridData placeholderGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		placeholderGridData.horizontalSpan = 4;
		placeholder.setLayoutData(placeholderGridData);
	}

	private void _initializeCheckBoxes() {
		ButtonFactory buttonFactory = new ButtonFactory(unitEditorGroup);
		checkBoxAddOneToHit = buttonFactory.createCheckBox(ADD_ONE_TO_HIT);
		checkBoxLethalHits = buttonFactory.createCheckBox(HAS_LETHAL_HITS);
		checkBoxRerollOnesToHit = buttonFactory.createCheckBox(REROLL_ONES_TO_HIT);
		checkBoxRerollHitRoll = buttonFactory.createCheckBox(REROLL_HIT_ROLL);
		
		checkBoxAddOneToWound = buttonFactory.createCheckBox(ADD_ONE_TO_WOUND);
		checkBoxRerollOnesToWound = buttonFactory.createCheckBox(REROLL_ONES_TO_WOUND);
		checkBoxRerollWound = buttonFactory.createCheckBox(REROLL_WOUND_ROLL);
		checkBoxIgnoreCover = buttonFactory.createCheckBox(IGNORE_COVER);
	}
}

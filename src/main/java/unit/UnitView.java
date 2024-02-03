package unit;

import java.util.ArrayList;

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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import arch.IModel;
import arch.IView;
import lombok.Getter;
import lombok.val;

public class UnitView implements IView {
	
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
	@Getter public TabItem unitTab;
	@Getter public Button checkBoxAddOneToHit;
	@Getter public Button checkBoxLethalHits;
	@Getter public Button checkBoxRerollOnes;
	@Getter public Button checkBoxRerollHitRoll;
	@Getter public Button checkBoxAddOneToWound;
	@Getter public Button checkBoxRerollOnesToWound;
	@Getter public Button checkBoxRerollWound;
	@Getter public Button checkBoxIgnoreCover;
	
	//"Recycled" Widgets - you mostly have to use draw() beforehand
	private Composite compositeUnitEditor;
	
	
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
		
		//List View
		{
			Composite compositeUnitList = new Composite(sashFormUnits, SWT.NONE);
			val layout = new FillLayout();
			layout.marginHeight = 5;
			layout.marginWidth = 5;
			
			compositeUnitList.setLayout(layout);
			compositeUnitList.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			
			Group unitListGroup = new Group(compositeUnitList, SWT.NONE);
			unitListGroup.setText(UNIT_LIST_VIEW);
			unitListGroup.setLayout(new FillLayout());
			
			//listToChooseUnitsFrom = new org.eclipse.swt.widgets.List(unitListGroup, SWT.NONE);
			//displayUnitList(Collections.emptyList());
		}
		
		//Sash form column 2
		{
			compositeUnitEditor = new Composite(sashFormUnits, SWT.NONE);
			val layout = new FillLayout(SWT.VERTICAL);
			layout.spacing = 5;
			layout.marginHeight = 5;
			layout.marginWidth = 5;
			compositeUnitEditor.setLayout(layout);
			compositeUnitEditor.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			//Group for editing Units  
			{
				
				
				//displaySelectedUnit(null);
			}
			
			/**Group for Editing Weapons TODO implement
			{
				Group unitWeaponEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
				unitWeaponEditorGroup.setText("Weapon Arsenal");
				GridLayout gridUnitWeaponEditor = new GridLayout(4, true);
				unitWeaponEditorGroup.setLayout(gridUnitWeaponEditor);
			}*/
		}
	}

	@Override
	public void drawEditor(IModel model) {
		Group unitEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
		unitEditorGroup.setText(UNIT_EDITOR_GROUP_NAME);
		GridLayout fillLayoutUnitEditor = new GridLayout(4, true);
		fillLayoutUnitEditor.marginHeight = 5;
		fillLayoutUnitEditor.marginWidth = 5;
		unitEditorGroup.setLayout(fillLayoutUnitEditor);
		
		Label nameLabel = new Label(unitEditorGroup, SWT.NONE);
		nameLabel.setText(UNIT_EDITOR_UNIT_NAME);
		Text nameInput = new Text(unitEditorGroup, SWT.NONE);
		nameInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		nameInput.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		new Label(unitEditorGroup, SWT.NONE); //Placeholder
		
		checkBoxAddOneToHit = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxAddOneToHit.setText(UNIT_EDITOR_ADD_ONE_TO_HIT);
		checkBoxLethalHits = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxLethalHits.setText(UNIT_EDITOR_HAS_LETHAL_HITS);
		checkBoxRerollOnes = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxRerollOnes.setText(UNIT_EDITOR_REROLL_ONES_TO_HIT);
		checkBoxRerollHitRoll = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxRerollHitRoll.setText(UNIT_EDITOR_REROLL_HIT_ROLL);
		
		checkBoxAddOneToWound = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxAddOneToWound.setText(UNIT_EDITOR_ADD_ONE_TO_WOUND);
		checkBoxRerollOnesToWound = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxRerollOnesToWound.setText(UNIT_EDITOR_REROLL_ONES_TO_WOUND);
		checkBoxRerollWound = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxRerollWound.setText(UNIT_EDITOR_REROLL_WOUND_ROLL);
		checkBoxIgnoreCover = new Button(unitEditorGroup,SWT.CHECK);
		checkBoxIgnoreCover.setText(UNIT_EDITOR_IGNORE_COVER);
	}

	@Override
	public void drawList(ArrayList<IModel> modelList) {
		// TODO Auto-generated method stub

	}
	
	

}

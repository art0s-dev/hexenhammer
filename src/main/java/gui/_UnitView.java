package gui;

import java.util.Collections;
import java.util.List;

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

import core.Unit;
import lombok.val;

public class _UnitView {
	
	private final TabFolder mainTab;
	private final Display display;
	
	private List<Unit> unitList;
	
	private Group unitEditorGroup;
	private Text nameInput;
	private Button checkBoxAddOneToHit;
	private Button checkBoxLethalHits;
	private Button checkBoxRerollOnes;
	private Button checkBoxRerollHitRoll;
	private Button checkBoxAddOneToWound;
	private Button checkBoxAddOnesToWound;
	private Button checkBoxRerollOnesToWound;
	private Button checkBoxRerollWound;
	private Button checkBoxIgnoreCover;
	
	private org.eclipse.swt.widgets.List listToChooseUnitsFrom;
	
	public _UnitView(TabFolder mainTab) {
		this.mainTab = mainTab;
		this.display = mainTab.getDisplay();
	}
	
	public void draw() {
		TabItem tabItem = new TabItem(mainTab, SWT.NONE);
		tabItem.setText("Units");
		Composite compositeUnits = new Composite(mainTab, SWT.NONE);
		compositeUnits.setLayout(new FillLayout());
		tabItem.setControl(compositeUnits);
		
		SashForm sashFormUnits = new SashForm(compositeUnits, SWT.HORIZONTAL);
		sashFormUnits.SASH_WIDTH = 3;
		sashFormUnits.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		
		//Sash form column 1
		{
			Composite compositeUnitList = new Composite(sashFormUnits, SWT.NONE);
			val layout = new FillLayout();
			layout.marginHeight = 5;
			layout.marginWidth = 5;
			
			compositeUnitList.setLayout(layout);
			compositeUnitList.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
			
			Group unitListGroup = new Group(compositeUnitList, SWT.NONE);
			unitListGroup.setText("Your Units");
			unitListGroup.setLayout(new FillLayout());
			
			listToChooseUnitsFrom = new org.eclipse.swt.widgets.List(unitListGroup, SWT.NONE);
			displayUnitList(Collections.emptyList());
		}
		
		//Sash form column 2
		{
			Composite compositeUnitEditor = new Composite(sashFormUnits, SWT.NONE);
			val layout = new FillLayout(SWT.VERTICAL);
			layout.spacing = 5;
			layout.marginHeight = 5;
			layout.marginWidth = 5;
			compositeUnitEditor.setLayout(layout);
			compositeUnitEditor.setBackground(display.getSystemColor(SWT.COLOR_WHITE));

			//Group for editing Units  
			{
				Group unitEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
				unitEditorGroup.setText("Special Rules");
				GridLayout fillLayoutUnitEditor = new GridLayout(4, true);
				fillLayoutUnitEditor.marginHeight = 5;
				fillLayoutUnitEditor.marginWidth = 5;
				unitEditorGroup.setLayout(fillLayoutUnitEditor);
				
				Label nameLabel = new Label(unitEditorGroup, SWT.NONE);
				nameLabel.setText("Display Name");
				Text nameInput = new Text(unitEditorGroup, SWT.NONE);
				nameInput.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
				nameInput.setBackground(display.getSystemColor(SWT.COLOR_WIDGET_LIGHT_SHADOW));
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				new Label(unitEditorGroup, SWT.NONE); //Placeholder
				
				Button checkBoxAddOneToHit = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxAddOneToHit.setText("Add one to hit");
				Button checkBoxLethalHits = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxLethalHits.setText("Has Lethal hits");
				Button checkBoxRerollOnes = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxRerollOnes.setText("Reroll ones to hit");
				Button checkBoxRerollHitRoll = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxRerollHitRoll.setText("Reroll to hit");
				Button checkBoxAddOneToWound = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxAddOneToWound.setText("Add one to wound");
				
				Button checkBoxAddOnesToWound = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxAddOnesToWound.setText("Add one to wound");
				Button checkBoxRerollOnesToWound = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxRerollOnesToWound.setText("Reroll ones to wound");
				Button checkBoxRerollWound = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxRerollWound.setText("Reroll wound roll");
				Button checkBoxIgnoreCover = new Button(unitEditorGroup,SWT.CHECK);
				checkBoxIgnoreCover.setText("Ignore cover");
				
				displaySelectedUnit(null);
			}
			
			//Group for Editing Weapons
			{
				Group unitWeaponEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
				unitWeaponEditorGroup.setText("Weapon Arsenal");
				GridLayout gridUnitWeaponEditor = new GridLayout(4, true);
				unitWeaponEditorGroup.setLayout(gridUnitWeaponEditor);
			}

		}

		sashFormUnits.setWeights(new int[] { 3 , 5 });
	}
	
	public void displayUnitList(List<Unit> unitList) {
		this.listToChooseUnitsFrom.removeAll();
		
//		for(Unit unit: unitList) {
//			this.unitList.add(unit.);
//		}
		
		//For each unit add unit name as item 
		//unitList.add("Space Marines");
		//unitList.add("Aeldari Ranger");
	}
	
	public void displaySelectedUnit(Unit selectedUnit) {
//		this.has(SpecialRuleUnit.ADD_ONE_TO_HIT),
//		this.has(SpecialRuleUnit.LETHAL_HITS) || weapon.has(SpecialRuleWeapon.LETHAL_HITS),
//		this.has(SpecialRuleUnit.REROLL_ONES_TO_HIT),
//		this.has(SpecialRuleUnit.REROLL_HIT_ROLL),
//		this.has(SpecialRuleUnit.ADD_ONE_TO_WOUND),
//		this.has(SpecialRuleUnit.REROLL_ONES_TO_WOUND),
//		this.has(SpecialRuleUnit.REROLL_WOUND_ROLL) || weapon.has(SpecialRuleWeapon.REROLL_WOUND_ROLL),
//		this.has(SpecialRuleUnit.IGNORE_COVER),
//		
//		nameInput.setText(selectedUnit.);
//		checkBoxAddOneToHit.set
//		checkBoxLethalHits.setText("Has Lethal hits");
//		checkBoxRerollOnes.setText("Reroll ones to hit");
//		checkBoxRerollHitRoll.setText("Reroll to hit");
//		checkBoxAddOneToWound.setText("Add one to wound");
//		checkBoxAddOnesToWound.setText("Add one to wound");
//		checkBoxRerollOnesToWound.setText("Reroll ones to wound");
//		checkBoxRerollWound.setText("Reroll wound roll");
//		checkBoxIgnoreCover.
	}
	


}

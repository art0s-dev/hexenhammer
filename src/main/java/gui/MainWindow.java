package gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;

import lombok.val;

public class MainWindow {
	
	public static void main(String[] args) {
		//Set the shell
		Display display = new Display();
		Shell shell = new Shell();
		//shell.setSize(1920, 1080); 
		shell.setLayout(new FillLayout());
		
		//Build the Tab Folder
		TabFolder mainTab = new TabFolder(shell, SWT.NONE);
		mainTab.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		//Build the "Units" Tab and its contents
		{
			TabItem tabItem = new TabItem(mainTab, SWT.NONE);
			tabItem.setText("Units");
			Composite compositeUnits = new Composite(mainTab, SWT.NONE);
			compositeUnits.setLayout(new FillLayout());
			tabItem.setControl(compositeUnits);
			
			SashForm sashFormUnits = new SashForm(compositeUnits, SWT.HORIZONTAL);
			sashFormUnits.SASH_WIDTH = 5;
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
				
				List unitList = new List(unitListGroup, SWT.NONE);
				//For each unit add unit name as item
				unitList.add("Space Marines");
				unitList.add("Aeldari Ranger");
			}
			
			//Sash form column 2
			{
				Composite compositeUnitEditor = new Composite(sashFormUnits, SWT.NONE);
				val layout = new FillLayout();
				layout.marginHeight = 5;
				layout.marginWidth = 5;
				compositeUnitEditor.setLayout(layout);
				compositeUnitEditor.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
				
				Group unitEditorGroup = new Group(compositeUnitEditor, SWT.NONE);
				unitEditorGroup.setText("Edit your Units");
				GridLayout gridUnitEditor = new GridLayout(2, false);
				unitEditorGroup.setLayout(gridUnitEditor);
				
				Label labelUnitName = new Label(unitEditorGroup, SWT.NONE);
				labelUnitName.setText("Display Name:");
				
				Text textUnitName = new Text(unitEditorGroup, SWT.NONE);
				textUnitName.setBackground(display.getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND));
				GridData gridDataUnitText = new GridData(SWT.FILL, SWT.FILL, true, false);
				textUnitName.setLayoutData(gridDataUnitText);
			}
			

			sashFormUnits.setWeights(new int[] { 3 , 5 });
		}
		
		

		//Main loop
		shell.open();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
}
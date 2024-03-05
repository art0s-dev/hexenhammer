package arch;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.CoolBar;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;

import lombok.Getter;
import utils.ImageServer;

/**
 * The Base View for all Forms in the Software
 * This is basically an swt view for working in the window builder
 */
abstract public class BaseView extends Composite implements View {
	
	//Dependencys
	protected final Composite parent;
	protected final ImageServer imageServer;
	
	//Internal Attributes
	private Composite controllingComposite;
	private Composite compositeEntityList;
	
	//Language Strings - these can be overwritten in the extending class to specify the string
	protected final static String TAB_NAME = "Template";
	protected final static String ADD = "Klick here, to add an entry to the chosen tab list";
	protected final static String DELETE = "Klick here, to delete the selected entry from the chosen tab";
	protected final static String LIST_VIEW = "Your Entities";
	protected final static String GROUP_NAME = "Special Rules";
	protected final static String UNIT_NAME = "Display name";
	
	//"Puppet strings" for the controller
	@Getter protected List selectionList;
	@Getter protected Button addButton;
	@Getter protected Button deleteButton;
	@Getter protected Text nameInput;

	/**
	 * You can use this to expand the editor.
	 * Just implement this as the parent
	 * in order to add thing to the grid
	 */
	protected Group entityEditorGroup;
	
	protected BaseView(Composite parent) {
		super(parent, SWT.NONE);
		this.parent = parent;
		this.imageServer = new ImageServer(parent.getShell().getDisplay());
		this.setLayout(new GridLayout(12, true));
	}
	
	//The extending class shall implement the interface
	abstract public void drawList(ModelList modelList);
	abstract public void drawEditor(Model model);
	
	public void draw() {
		_drawControllingComposite();
		_drawMenu();
		_drawUnitList();
		_drawEditor();
	}
	
	/**
	 * Implementation for the menu bar
	 */
	private void _drawMenu() {
		CoolBar menuBar = new CoolBar(compositeEntityList, SWT.NONE);
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
	
	/**
	 * Implementation of the base grid
	 */
	private void _drawControllingComposite() {
		controllingComposite = new Composite(parent, SWT.NONE);
		controllingComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		controllingComposite.setLayout(new GridLayout(12, true));
		
		compositeEntityList = new Composite(controllingComposite, SWT.NONE);
		GridData gridDataCompositeUnitList = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDataCompositeUnitList.horizontalSpan = 4;
		compositeEntityList.setLayoutData(gridDataCompositeUnitList);
		compositeEntityList.setLayout(new GridLayout(1, true));
	}

	private void _drawUnitList() {
		Group entityListGroup = new Group(compositeEntityList, SWT.VERTICAL); 
		entityListGroup.setText(LIST_VIEW);
		entityListGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityListGroup.setLayout(new GridLayout(1, true));
		
		Composite entityListComposite = new Composite(entityListGroup, SWT.NONE);
		entityListComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityListComposite.setLayout(new GridLayout(1, true));

		selectionList = new List(entityListComposite, SWT.NONE);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		selectionList.setLayoutData(listGridData);
	}

	/**
	 * Implementation of the basic editor 
	 */
	private void _drawEditor() {
		Composite compositeEntityEditor = new Composite(controllingComposite, SWT.NONE);
		GridData gridDateUnitEditor = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDateUnitEditor.horizontalSpan = 8;
		compositeEntityEditor.setLayoutData(gridDateUnitEditor);
		compositeEntityEditor.setLayout(new GridLayout(1, true));
		
		entityEditorGroup = new Group(compositeEntityEditor, SWT.NONE);
		entityEditorGroup.setText(GROUP_NAME);
		entityEditorGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityEditorGroup.setLayout(new GridLayout(4, true));
		
		Label nameLabel = new Label(entityEditorGroup, SWT.NONE);
		nameLabel.setText(UNIT_NAME); 
		nameInput = new Text(entityEditorGroup, SWT.NONE);
		GridData nameInputGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		nameInputGridData.horizontalSpan = 3;
		nameInput.setLayoutData(nameInputGridData);
		
		Label placeholder = new Label(entityEditorGroup, SWT.NONE);
		GridData placeholderGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		placeholderGridData.horizontalSpan = 4;
		placeholder.setLayoutData(placeholderGridData);
	}

}

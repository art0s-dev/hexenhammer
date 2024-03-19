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
import utils.I18n;
import utils.ImageServer;

/**
 * The Base View for all forms in the Software
 */
abstract public class BaseView implements View {
	
	//Dependencys
	protected final Composite parent;
	protected final ImageServer imageServer;
	protected final I18n i18n;
	
	//Internal Attributes
	private Composite controllingComposite;
	private Composite compositeEntityList;
	private Label inputNameLabel;
	
	//"Puppet strings" for the controller
	@Getter protected Button addButton;
	@Getter protected Button deleteButton;
	@Getter protected List selectionList;
	@Getter protected Text inputName;

	/**
	 * You can use this to expand the editor.
	 * Just implement this as the parent
	 * in order to add thing to the grid
	 */
	protected Group entityEditorGroup;
	protected Group entityListGroup;
	
	protected BaseView(Composite parent, I18n i18n) {
		this.i18n = i18n;
		this.parent = parent;
		this.imageServer = new ImageServer(parent.getShell().getDisplay());
	}
	
	//The extending class shall implement the interface
	abstract public void drawList(ModelList modelList);
	abstract public void drawEditor(Model model);
	
	public void draw() {
		_drawControllingComposite();
		_drawMenu();
		_drawEntityList();
		_drawEditor();
	}

	public void translate() {
		addButton.setToolTipText(i18n.get(defineAddButtonToolTip()));
		deleteButton.setToolTipText(i18n.get(defineDeleteButtonToolTip()));
		entityListGroup.setText(i18n.get(defineListViewLabel()));
		entityEditorGroup.setText(i18n.get(defineEditorGroupName()));
		inputNameLabel.setText(i18n.get("arch.BaseView.editor.inputNameLabel")); 
	}
	
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
	
	private void _drawMenu() {
		CoolBar menuBar = new CoolBar(compositeEntityList, SWT.NONE);
		GridData menuBarGridData = new GridData(SWT.FILL, SWT.FILL, false, false);
		menuBarGridData.verticalSpan = 8;
		menuBar.setLayoutData(menuBarGridData);
		menuBar.setLayout(new GridLayout(2, true));
		
		addButton = new Button(menuBar, SWT.PUSH);
		addButton.setImage(imageServer.createImageForButton("plus"));
		
		deleteButton = new Button(menuBar, SWT.PUSH);
		deleteButton.setImage(imageServer.createImageForButton("trash-can"));
	}

	private void _drawEntityList() {
		entityListGroup = new Group(compositeEntityList, SWT.VERTICAL); 
		entityListGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityListGroup.setLayout(new GridLayout(1, true));
		
		Composite entityListComposite = new Composite(entityListGroup, SWT.NONE);
		entityListComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityListComposite.setLayout(new GridLayout(1, true));

		selectionList = new List(entityListComposite, SWT.NONE);
		GridData listGridData = new GridData(SWT.FILL, SWT.FILL, true, true);
		selectionList.setLayoutData(listGridData);
	}

	private void _drawEditor() {
		Composite compositeEntityEditor = new Composite(controllingComposite, SWT.NONE);
		GridData gridDateUnitEditor = new GridData(SWT.FILL, SWT.FILL, true, true);
		gridDateUnitEditor.horizontalSpan = 8;
		compositeEntityEditor.setLayoutData(gridDateUnitEditor);
		compositeEntityEditor.setLayout(new GridLayout(1, true));
		
		entityEditorGroup = new Group(compositeEntityEditor, SWT.NONE);
		entityEditorGroup.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		entityEditorGroup.setLayout(new GridLayout(4, true));
		
		inputNameLabel = new Label(entityEditorGroup, SWT.NONE);
		inputName = new Text(entityEditorGroup, SWT.NONE);
		GridData nameInputGridData = new GridData(SWT.FILL, SWT.CENTER, true, false);
		nameInputGridData.horizontalSpan = 3;
		inputName.setLayoutData(nameInputGridData);
		
		Label placeholder = new Label(entityEditorGroup, SWT.NONE);
		GridData placeholderGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		placeholderGridData.horizontalSpan = 4;
		placeholder.setLayoutData(placeholderGridData);
	}
	
	protected String defineAddButtonToolTip() { 
		return "arch.BaseView.listView.addButtonToolTip";
	}
	
	protected String defineDeleteButtonToolTip() {
		return "arch.BaseView.listView.deleteButtonToolTip";
	}
	
	protected String defineListViewLabel() {
		return "arch.BaseView.listView.label";
	}
	
	protected String defineEditorGroupName() {
		return "arch.BaseView.editor.groupName";
	}
	
}

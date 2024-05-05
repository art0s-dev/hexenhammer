package arch;

import static org.eclipse.swt.SWT.CENTER;
import static org.eclipse.swt.SWT.FILL;
import static org.eclipse.swt.SWT.NONE;
import static org.eclipse.swt.SWT.PUSH;
import static org.eclipse.swt.SWT.VERTICAL;
import static utils.Theme.GRID_FILL;

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

/**
 * The Base View for all forms in the Software
 */
abstract public class BaseView implements View {
	
	//Dependencys
	protected final Composite parent;
	
	//Internal Attributes
	private Composite controllingComposite;
	private Composite compositeEntityList;
	private Label inputNameLabel;
	
	//"Puppet strings" for the controller
	@Getter protected Button addButton;
	@Getter protected Button deleteButton;
	@Getter protected List selectionList;
	@Getter protected Text inputName;
	@Getter protected final I18n i18n;

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
		controllingComposite = new Composite(parent, NONE);
		controllingComposite.setLayoutData(GRID_FILL);
		controllingComposite.setLayout(new GridLayout(12, true));
		
		compositeEntityList = new Composite(controllingComposite, NONE);
		GridData gridDataCompositeUnitList = GRID_FILL;
		gridDataCompositeUnitList.horizontalSpan = 4;
		compositeEntityList.setLayoutData(gridDataCompositeUnitList);
		compositeEntityList.setLayout(new GridLayout(1, true));
	}
	
	private void _drawMenu() {
		CoolBar menuBar = new CoolBar(compositeEntityList, NONE);
		GridData menuBarGridData = new GridData(FILL, FILL, false, false);
		menuBarGridData.verticalSpan = 9;
		menuBar.setLayoutData(menuBarGridData);
		menuBar.setLayout(new GridLayout(2, true));
		
		addButton = new Button(menuBar, PUSH);
		addButton.setText("+");
		
		deleteButton = new Button(menuBar, PUSH);
		deleteButton.setText("-");
	}

	private void _drawEntityList() {
		entityListGroup = new Group(compositeEntityList, VERTICAL); 
		entityListGroup.setLayoutData(GRID_FILL);
		entityListGroup.setLayout(new GridLayout(1, true));
		
		Composite entityListComposite = new Composite(entityListGroup, NONE);
		entityListComposite.setLayoutData(GRID_FILL);
		entityListComposite.setLayout(new GridLayout(1, true));

		selectionList = new List(entityListComposite, NONE);
		GridData listGridData = GRID_FILL;
		selectionList.setLayoutData(listGridData);
	}

	private void _drawEditor() {
		Composite compositeEntityEditor = new Composite(controllingComposite, NONE);
		GridData gridDateUnitEditor = GRID_FILL;
		gridDateUnitEditor.horizontalSpan = 8;
		compositeEntityEditor.setLayoutData(gridDateUnitEditor); 
		compositeEntityEditor.setLayout(new GridLayout(1, true));
		
		entityEditorGroup = new Group(compositeEntityEditor, NONE);
		entityEditorGroup.setLayoutData(GRID_FILL);
		entityEditorGroup.setLayout(new GridLayout(4, true));
		
		inputNameLabel = new Label(entityEditorGroup, NONE);
		inputName = new Text(entityEditorGroup, NONE);
		GridData nameInputGridData = new GridData(FILL, CENTER, true, false);
		nameInputGridData.horizontalSpan = 3;
		inputName.setLayoutData(nameInputGridData);
		
		Label placeholder = new Label(entityEditorGroup, NONE);
		GridData placeholderGridData = new GridData(FILL, FILL, true, false);
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

package statistics;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import core.Unit;
import lombok.Getter;
import utils.I18n;

public final class StatisticsView {
	
	private final Composite parent;
	private final I18n i18n;
	@Getter private Button startCalculationButton;
	private Table resultsTable;
	
	public StatisticsView(Composite parent, I18n i18n) {
		this.parent = parent;
		this.i18n = i18n;
	}

	public void draw() {
		_drawWidgets();
	}
	
	public void drawTableColumns(List<Unit> units, List<Unit> enemys) {
		_cleanTable();
		
		TableColumn emptyCol = new TableColumn(resultsTable, SWT.NONE);
		emptyCol.setWidth(100);
		
		for(Unit enemy: enemys) {
			TableColumn enemyCol = new TableColumn(resultsTable, SWT.NONE);
			enemyCol.setText(enemy.getName());
			enemyCol.setWidth(100);
		}
		
		for(Unit unit: units) {
			ArrayList<String> results = new ArrayList<>();
			results.add(unit.getName());
			
			for(Unit enemy: enemys) {
				results.add("" + unit.attack(enemy));
			}
			
			TableItem unitRow = new TableItem(resultsTable, SWT.NONE);
			unitRow.setText(results.toArray(new String[0]));
		}	
	}

	private void _cleanTable() {
		TableItem[] items = resultsTable.getItems();
		TableColumn[] cols = resultsTable.getColumns();
		for (TableItem item : items) {
		    item.dispose();
		}
		for (TableColumn item : cols) {
		    item.dispose();
		}
	}

	private void _drawWidgets() {
		Composite mainStatisticsComposite = new Composite(parent, SWT.NONE);
		mainStatisticsComposite.setLayout(new GridLayout(1, true));
		mainStatisticsComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		startCalculationButton = new Button(mainStatisticsComposite, SWT.NONE);
		
		resultsTable = new Table(mainStatisticsComposite, SWT.NONE);
		resultsTable.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		resultsTable.setHeaderVisible(true);
		resultsTable.setLinesVisible(true);
	}

	public void translate() {
		String prefix = "statistics.StatisticsView.damageResults.";
		startCalculationButton.setText(i18n.get(prefix + "startCalculationButton"));
	}

}

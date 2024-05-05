package statistics;

import static utils.Lambda.select;

import java.util.ArrayList;
import java.util.List;

import arch.Controller;
import arch.Model;
import core.Unit;
import unit.UnitController;

public class StatisticsController implements Controller {
	
	private UnitController unitController;
	private StatisticsView view;
	
	private List<Unit> units;
	private List<Unit> enemys;
	
	public StatisticsController(StatisticsView view, UnitController unitController) {
		this.view = view;
		this.unitController = unitController;
	}

	@Override
	public void loadModels() {
		units = unitController.getUnitList().getUnits().parallelStream()
				.filter(unit -> !unit.isUseAsEnemy())
				.toList();
		
		enemys = unitController.getUnitList().getUnits().parallelStream()
				.filter(unit -> unit.isUseAsEnemy())
				.toList();
	}

	@Override
	public void initView() {
		view.draw();
		view.translate();
	}

	@Override
	public void injectListener() {
		view.getStartCalculationButton().addSelectionListener(select(() -> {
			loadModels();
			view.drawTableColumns(units, enemys);
		}));
	}

	@Override
	public void save(ArrayList<Model> modelList) {
		// TODO Auto-generated method stub
	}

}

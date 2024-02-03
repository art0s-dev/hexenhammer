package unit;

import java.util.ArrayList;

import arch.IController;
import arch.IModel;

public class UnitController implements IController {
	
	private final UnitView unitView;
	
	public UnitController(UnitView unitView) {
		this.unitView = unitView;
	}

	@Override
	public ArrayList<IModel> loadModels() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void save(ArrayList<IModel> modelList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void injectListener() {
		
	}

}

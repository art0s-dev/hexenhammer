package arch;

import java.util.ArrayList;

public interface IController {
	
	/**
	 * Loads all Specific Models into the controller
	 * @return
	 */
	ArrayList<IModel> loadModels();
	
	/**
	 *	Saves a List of models
	 */
	void save(ArrayList<IModel> modelList);
	
	/**
	 * Injects a listener into the view, which operates
	 * the triggers of all functionalities in the Model
	 * like a property change listener or vice versa
	 * TODO This might be a gathering point for a big mud ball of logic 
	 * how can i break this into parts?
	 */
	void injectListener();
	
}

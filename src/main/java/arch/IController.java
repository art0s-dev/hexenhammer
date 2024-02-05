package arch;

import java.util.ArrayList;

public interface IController {
	
	/**
	 * Loads all Specific Models into the controller
	 */
	void loadModels();
	
	/**
	 * initializes the view before injecting listeners
	 * or performing operations
	 */
	void initView();
	
	/**
	 * Injects a listener into the view, which operates
	 * the triggers of all functionalities in the Model
	 * like a property change listener or vice versa
	 * TODO This might be a gathering point for a big mud ball of logic 
	 * how can i break this into parts?
	 */
	void injectListener();
	
	/**
	 *	Saves a List of models
	 */
	void save(ArrayList<Model> modelList);
	
}

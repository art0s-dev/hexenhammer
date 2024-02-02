package arch;

import java.util.ArrayList;

public interface IView {
	
	/**
	 * Draws a selection Editor for a singe model.
	 * Shall get redrawn, if the user wants to change the model
	 * @param model
	 */
	void drawEditor(IModel model);
	
	/**
	 * Draws a List of Models that the user can see
	 * @param modeList
	 */
	void drawList(ArrayList<IModel> modelList);
}

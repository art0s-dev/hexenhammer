package arch;

public interface View {
	/**
	 * draws all that labels, groups und stuff that
	 * will be necessary, to draw the editor and the list.
	 * This is the stuff that don't changes in the first place
	 * it should only change if i18N is implemented
	 */
	void draw();
	
	/**
	 * Draws a List of Models that the user can see
	 * @param modeList
	 */
	void drawList(ModelList modelList);
	
	/**
	 * Draws a selection Editor for a single model.
	 * Shall get redrawn, if the user wants to change the model
	 * @param model
	 */
	void drawEditor(Model model);
	

}

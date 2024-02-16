package arch;

/**
 * The Repository incorporates all Database calls and the 
 * persistence layer in general.
 */
public interface Repository {
	
	/**
	 * Loads all entities
	 */
	public ModelList load();
}

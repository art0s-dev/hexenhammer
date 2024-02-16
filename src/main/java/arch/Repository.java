package arch;

/**
 * The Repository incorporates all Database Calls and the 
 * persistence layer in general.
 */
public interface Repository {
	
	/**
	 * Loads all entities
	 */
	public ModelList load();
}

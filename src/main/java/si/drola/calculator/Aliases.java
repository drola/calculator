package si.drola.calculator;

import java.util.HashMap;

/**
 * Collection of aliases
 *
 */
public class Aliases {
	HashMap<String, Alias> map;
	
	public Aliases()
	{
		map = new HashMap<String, Alias>();
	}
	
	/**
	 * Retrieve alias by name
	 * 
	 * @param name
	 * @return Alias if exists, null otherwise
	 */
	public Alias find(String name)
	{
		return map.get(name);
	}

}

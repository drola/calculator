package si.drola.calculator;

import java.util.HashMap;

/**
 * Collection of units
 *
 */
public class Units {
	HashMap<String, Unit> map;
	
	public Units()
	{
		map = new HashMap<String, Unit>();
	}
	
	/**
	 * Retrieve unit by name
	 * 
	 * @param name
	 * @return Unit if exists, null otherwise
	 */
	public Unit find(String name)
	{
		return map.get(name);
	}

}

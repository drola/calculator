package si.drola.calculator;

import java.util.HashMap;

/**
 * Collection of functions
 * 
 */
public class Functions {
	HashMap<String, Function> map;

	public Functions() {
		map = new HashMap<String, Function>();
	}

	/**
	 * Retrieve function by name
	 * 
	 * @param name
	 * @return Function if exists, null otherwise
	 */
	public Function find(String name) {
		return map.get(name);
	}

	/**
	 * Add a function to collection
	 * 
	 * @param f
	 *            Function
	 */
	public void add(String name, Function f) {
		map.put(name, f);
	}

	/**
	 * Check whether the string is a function name. Whitespace is ignored.
	 * 
	 * @param s
	 *            Function name
	 * @return Function if one is found, null otherwise
	 */
	public Function isFunction(String s) {
		return this.find(s.trim());
	}

}

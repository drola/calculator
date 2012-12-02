package si.drola.calculator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Collection of prefixes
 * 
 */
public class Prefixes {
	Map<Integer, List<Prefix>> prefixes;

	public Prefixes() {
		prefixes = new HashMap<Integer, List<Prefix>>();
	}

	/**
	 * Retrieve the longest matching prefix
	 * 
	 * @param name
	 * @return Prefix if exists, null otherwise
	 */
	public Prefix find(String name) {
		int longest = 0;
		Prefix longest_ = null;
		List<Prefix> list = prefixes.get(hash(name));
		if (list == null) {
			return null;
		}

		Iterator<Prefix> it = list.iterator();
		while (it.hasNext()) {
			Prefix p = it.next();
			if (p.getLength() > longest
					&& p.getName() == name.substring(0, p.getLength())) {
				longest = p.getLength();
				longest_ = p;
			}
		}

		return longest_;
	}

	/**
	 * Hash unit name
	 * 
	 * @param name
	 * @return hash
	 */
	protected int hash(String name) {
		return name.charAt(0) & 127;
	}
}

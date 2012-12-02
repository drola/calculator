package si.drola.calculator;

/**
 * Prefix
 *
 */
public class Prefix {
	protected String name;
	protected String value;
	protected int length;
	
	

	/**
	 * Get prefix name
	 * @return prefix name
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Set prefix name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
		this.length = name.length();
	}
	
	/**
	 * Get prefix value
	 * 
	 * @return prefix value
	 */
	public String getValue() {
		return value;
	}
	
	/**
	 * Set prefix value
	 * @param value
	 */
	public void setValue(String value) {
		this.value = value;
	}
	
	/**
	 * Get prefix length
	 * 
	 * @return prefix length
	 */
	public int getLength() {
		return length;
	}	
}

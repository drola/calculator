package si.drola.calculator;

import java.util.Arrays;


public class Value {
	ValueUnit[] units;
	double factor;

	protected class ValueUnit implements Comparable<ValueUnit>, Cloneable {
		public static final double EPSILON = 0.0000000000000001;
		public double exponent;
		public String name;

		public int compareTo(ValueUnit o) {
			return name.compareTo(o.name);
		}

		public boolean equals(ValueUnit o) {
			return name.equals(o.name)
					&& Math.abs(exponent - o.exponent) < EPSILON;
		}
		
		public ValueUnit clone() {
			ValueUnit c = new ValueUnit();
			c.exponent = exponent;
			c.name = name;
			return c;
		}
	}

	/**
	 * Construct a new Value with default value of 1.
	 */
	public Value() {
		units = new ValueUnit[] {};
		factor = 1;
	}
	
	/**
	 * Construct a new unitless Value
	 * @param i numeric part
	 */
	public Value(double i) {
		units = new ValueUnit[] {};
		factor = i;
	}



	/**
	 * Add two values
	 * 
	 * @param b
	 *            Value to be added
	 * @return this + b
	 * @throws Exception
	 */
	public Value add(Value b) throws Exception {
		if (!areUnitsIdentical(b)) {
			throw new Exception("Different units, cannot do summation.");
		}

		Value result = new Value();
		result.factor = this.factor + b.factor;
		result.units = cloneUnits(units);
		return result;
	}
	
	/**
	 * Substract
	 * 
	 * @param b
	 * @return this - b
	 * @throws Exception
	 */
	public Value substract(Value b) throws Exception {
		if (!areUnitsIdentical(b)) {
			throw new Exception("Different units, cannot do summation.");
		}

		Value result = new Value();
		result.factor = this.factor - b.factor;
		result.units = cloneUnits(units);
		return result;
	}
	
	protected ValueUnit[] cloneUnits(ValueUnit[] in) {
		ValueUnit[] out = new ValueUnit[in.length];
		for(int i = 0; i<out.length; i++) {
			out[i] = in[i].clone();
		}
		return out;
	}

	/**
	 * Multiply two Values
	 * 
	 * @param b
	 * @return this * b
	 */
	public Value multiply(Value b) {
		Value result = new Value();
		result.factor = this.factor * b.factor;
		result.units = multiplyUnits(this.units, b.units, 1);
		return result;
	}

	/**
	 * Divide two values
	 * 
	 * @param b
	 * 
	 * @return this / b
	 */
	public Value divide(Value b) {
		Value result = new Value();
		result.factor = this.factor / b.factor;
		result.units = multiplyUnits(this.units, b.units, -1);
		return result;
	}

	/**
	 * Exponentiate unit
	 * 
	 * @param power
	 *            Power
	 * @return
	 */
	public Value exp(double power) {
		Value result = new Value();
		result.factor = Math.pow(factor, power);
		result.units = multiplyUnits(new ValueUnit[0], units, power);
		return result;
	}

	protected ValueUnit[] multiplyUnits(ValueUnit[] a, ValueUnit[] b, double b_exp) {
		ValueUnit[] result = new ValueUnit[a.length + b.length];
		for(int i = 0; i<a.length; i++) {
			result[i] = a[i].clone();
		}
		for(int i = 0; i<b.length; i++) {
			result[i + a.length] = b[i].clone();
			result[i + a.length].exponent *= b_exp;
		}
		result = compactUnits(result);
		return result;
	}

	

	/**
	 * Remove all empty units that remained from cancelling, etc
	 */
	protected ValueUnit[] compactUnits(ValueUnit[] units) {
		if(units.length<1) {
			return units;
		}
		
		//1. sort
		Arrays.sort(units);
		
		//2. merge all units with same name into one ValueUnit
		ValueUnit prev = units[0];
		for(int i = 1; i<units.length; i++) {
			if(prev.name.equals(units[i].name)) {
				prev.exponent += units[i].exponent;
				units[i] = null;
			}
		}
		
		//3. clean-up
		int count = 0;
		for(int i = 0; i<units.length; i++)
		{
			if(units[i] != null) {
				count++;
			}
		}
		
		int pos = 0;
		ValueUnit[] tmp = new ValueUnit[count];
		for(int i = 0; i<units.length; i++)
		{
			tmp[pos++] = units[i];
		}
		
		return tmp;
	}
	
	protected void compactUnits() {
		units = compactUnits(units);
	}

	/**
	 * Compare units.
	 * 
	 * @param b
	 * 
	 * @return true if units are identical
	 */
	protected boolean areUnitsIdentical(Value b) {
		compactUnits();
		b.compactUnits();
		if(b.units.length != units.length) {
			return false;
		}
		
		for(int i = 0; i<units.length; i++) {
			if(!units[i].equals(b.units[i])) {
				return false;
			}
		}
		
		return true;
	}

	public Value exp(Value value) {
		// TODO Auto-generated method stub
		return null;
	}

}

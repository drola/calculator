package si.drola.calculator;

import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

public class Value {
	String[] numerator;
	String[] denominator;
	double factor;

	/**
	 * Construct a new Value with default value of 1.
	 */
	public Value() {
		numerator = new String[] {};
		denominator = new String[] {};
		factor = 1;
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
		reduceUnits();
		b.reduceUnits();

		if (compareUnits(b)) {
			throw new Exception("Different units, cannot do summation.");
		}

		Value result = new Value();
		result.factor = this.factor + b.factor;
		result.numerator = this.numerator;
		result.denominator = this.denominator;
		return result;
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
		result.numerator = multiplyUnits(this.numerator, b.numerator);
		result.denominator = multiplyUnits(this.denominator, b.denominator);
		return result;
	}

	/**
	 * Divide two values
	 * @param b
	 * 
	 * @return this / b
	 */
	public Value divide(Value b) {
		Value result = new Value();
		result.factor = this.factor / b.factor;
		result.numerator = multiplyUnits(this.numerator, b.denominator);
		result.denominator = multiplyUnits(this.denominator, b.numerator);
		return result;
	}

	/**
	 * Exponentiate unit
	 * 
	 * @param power Positive integer
	 * @return
	 */
	public Value exp(int power) {
		int numlen, denlen;
		Value result = new Value();
		
		if(power == 0) {
			return result;
		}
		
		compactUnits();
		numlen = numerator.length;
		denlen = denominator.length;
		result.numerator = new String[numlen * power];
		result.denominator = new String[denlen * power];
		for(int i = 0; i<power; i++) {
			for(int j = 0; j<numlen; j++) {
				result.numerator[j + i*numlen] = numerator[j];
			}
			
			for(int j = 0; j<denlen; j++) {
				result.denominator[j + i*denlen] = denominator[j];
			}
		}
		
		result.factor = Math.pow(factor, power);
		return result;
	}

	protected String[] multiplyUnits(String[] a, String[] b) {
		return ArrayUtils.addAll(a, b);
	}

	/**
	 * Cancel units in numerator and denominator as much as possible.
	 */
	protected void reduceUnits() {
		sortUnits();
		int den = 0;
		int num = 0;
		while (denominator.length > den && numerator.length > num) {
			int cmp = denominator[den].compareTo(numerator[num]);
			if (cmp == 0) {
				denominator[den++] = "";
				numerator[num++] = "";
			} else if (cmp < 0) {
				den++;
			} else {
				num++;
			}
		}
	}

	/**
	 * Remove all empty units that remained from cancelling, etc
	 */
	protected void compactUnits() {
		// 1. count
		int len = 0;
		for (int i = 0; i < numerator.length; i++) {
			if (numerator[i].length() > 0) {
				len++;
			}
		}

		// 2. new temporary array
		String[] tmp = new String[len];
		int pos = 0;
		for (int i = 0; i < numerator.length; i++) {
			if (numerator[i].length() > 0) {
				// 3. copy only non-empty units
				tmp[pos++] = numerator[i];
			}
		}
		numerator = tmp;

		// 1. count
		len = 0;
		for (int i = 0; i < denominator.length; i++) {
			if (denominator[i].length() > 0) {
				len++;
			}
		}

		// 2. new temporary array
		tmp = new String[len];
		pos = 0;
		for (int i = 0; i < denominator.length; i++) {
			if (denominator[i].length() > 0) {
				// 3. copy only non-empty units
				tmp[pos++] = denominator[i];
			}
		}
		denominator = tmp;
	}

	/**
	 * Sort units in numerator and denominator
	 */
	protected void sortUnits() {
		Arrays.sort(this.numerator);
		Arrays.sort(this.denominator);
	}

	/**
	 * Compare units.
	 * 
	 * @param b
	 * 
	 * @return 0 if units are identical
	 */
	protected boolean compareUnits(Value b) {
		return compareUnitProducts(denominator, b.denominator)
				|| compareUnitProducts(numerator, b.numerator);
	}

	/**
	 * Compare two products of units.
	 * 
	 * @param a
	 * @param b
	 * @return 0 if products are identical
	 */
	protected boolean compareUnitProducts(String[] a, String[] b) {
		int ai = 0;
		int bi = 0;
		boolean ablank, bblank;

		while (ai < a.length && bi < b.length) {
			ablank = a[ai] == "";
			bblank = b[bi] == "";
			if (!(ai < a.length) && !bblank) {
				return true;
			} else if (!(bi < b.length) && !ablank) {
				return true;
			} else if (ablank) {
				ai++;
			} else if (bblank) {
				bi++;
			} else if (a[ai] != b[bi]) {
				return true;
			} else {
				ai++;
				bi++;
			}
		}

		return false;
	}

}

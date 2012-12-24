package si.drola.calculator;

import java.io.IOException;

import si.drola.calculator.Parser;

public class Lexer implements Parser.Lexer {
	protected Object lval = null;
	protected String input = null;
	protected int pos = 0;
	protected int tmppos = 0;

	protected static final String nonunitchars = "~;+-*/|\t\n^ ()";
	protected static final String nonunitends = ".,_";
	protected static final String number_start = ".,0123456789";
	protected static final String whitespace = " \t\n";
	protected static Operator[] optable = null;

	public Lexer(String input) {
		this.input = input;
		this.pos = 0;

		if (optable == null)
			optable = new Operator[] { new Operator('*', Parser.MULTIPLY),
					new Operator('/', Parser.DIVIDE),
					new Operator('|', Parser.NUMDIV),
					new Operator('+', Parser.ADD), new Operator('(', '('),
					new Operator(')', ')'), new Operator('^', Parser.EXPONENT),
					new Operator('~', Parser.FUNCINV), };
	}

	protected boolean valid() {
		return input.length() > pos;
	}

	public Object getLVal() {
		return lval;
	}

	protected class Operator {
		public int code;
		public char symbol;

		public Operator(char symbol, int code) {
			this.code = code;
			this.symbol = symbol;
		}
	}

	protected double strtod(int pos) {
		boolean negative = false;
		boolean after_comma = false;
		double exp = 1;
		double result = 0;
		double after_comma_exp = 0.1;
		double exp2 = 1;
		if (input.charAt(pos) == '-') {
			negative = true;
			pos++;
		}

		while (input.length() > pos) {
			char current = input.charAt(pos);
			if (current >= '0' && current <= '9') {
				result += exp * (current - '0');
				exp /= 10.0;
				if(after_comma) {
					after_comma_exp/=10.0;
				}
				exp2 *= 10;
			} else if (current == '.') {
				after_comma = true;
			} else {
				break;
			}

			pos++;
		}

		tmppos = pos;
		return (negative ? -1 : 1) * result * exp2 * after_comma_exp;
	}

	protected int strcspn(String forbidden, int pos) {
		int x = pos;
		while (input.length() > x && forbidden.indexOf(input.charAt(x)) < 0) {
			x++;
		}
		return x - pos;
	}

	public int yylex() throws IOException {
		if (pos == -1)
			return 0;

		// Skip whitespace
		while (valid() && whitespace.indexOf(input.charAt(pos)) >= 0)
			pos++;

		if (!valid()) {
			pos = -1;
			return Parser.EOL; // Return failure if string has ended
		}

		// Check for **, an exponent operator
		if (input.length() > (pos + 2)
				&& 0 == input.substring(pos, pos + 2).compareTo("**")) {
			pos += 2;
			return Parser.EXPONENT;
		}

		char current = input.charAt(pos);

		if (current == '-') {
			pos++;
			return Parser.MULTMINUS;
		}

		if (current == '*') {
			pos++;
			return Parser.MULTSTAR;
		}

		// Check for single char operators
		for (int i = 0; i < optable.length; i++) {
			if (current == optable[i].symbol) {
				pos++;
				return optable[i].code;

			}
		}

		// Look for numbers
		if (number_start.indexOf(current) >= 0) {
			lval = strtod(pos);
			if (tmppos != pos) {
				pos = tmppos;
				if (valid() && number_start.indexOf(input.charAt(pos)) >= 0) {
					return Parser.BADNUMBER;
				} else {
					return Parser.REAL;
				}
			}
		}

		int length = strcspn(nonunitchars, pos);
		// TODO!

		// Didn't find a special string -> treat as a unit name
		int exp = 1;
		tmppos = pos;
		pos += length;
		if ((pos - 1) >= 0) {
			char prev = input.charAt(pos - 1);
			if (prev >= '2' && prev <= '9' /* && !hassubscript */) {
				exp = prev - '0';

				length--;
				if ((pos - 2) >= 0) {
					prev = input.charAt(pos - 2);
					if (number_start.indexOf(prev) >= 0) {
						return 0;
					}
				}
			}
		}

		lval = new Value(1, new Value.ValueUnit[] { new Value.ValueUnit(
				input.substring(tmppos, tmppos + length), exp) });
		return Parser.UNIT;

		// return 0;

	}

	public void yyerror(String s) {
		// TODO Auto-generated method stub

	}

}

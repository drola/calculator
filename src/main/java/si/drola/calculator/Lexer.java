package si.drola.calculator;

import java.io.IOException;

import si.drola.calculator.Parser;

public class Lexer implements Parser.Lexer {
	protected Object lval = null;
	protected String input = null;
	protected int pos = 0;

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
		return input.length() < pos;
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
		if (0 == input.substring(pos, pos + 2).compareTo("**")) {
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
		for(int i = 0; i<optable.length; i++) {
			if(current == optable[i].symbol) {
				pos++;
				return optable[i].code;
			}
		}
		
		if(number_start.indexOf(current)>=0) {
				//TODO!
			
		}
		
		//TODO!
		return 0;

	}

	public void yyerror(String s) {
		// TODO Auto-generated method stub

	}

}

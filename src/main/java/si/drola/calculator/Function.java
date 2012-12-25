package si.drola.calculator;

/**
 * Mathematical function
 * 
 */
public interface Function {
	public static final int DIMENSIONLESS = 0;
	public static final int ANGLE = 1;
	
	public Value evaluate(Value x);
	public Value evaluateInverse(Value x);
	public int getArgumentDimension();
	public int getResultDimension();
}

package si.drola.calculator;

public class SinFunction implements Function {

	public Value evaluate(Value x) {
		x.factor = Math.sin(x.factor);
		return x;
	}

	public Value evaluateInverse(Value x) {
		x.factor = Math.asin(x.factor);
		return x;
	}

	public int getArgumentDimension() {
		return Function.ANGLE;
	}

	public int getResultDimension() {
		return Function.DIMENSIONLESS;
	}

}

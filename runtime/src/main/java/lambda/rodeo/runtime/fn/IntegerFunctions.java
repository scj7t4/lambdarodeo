package lambda.rodeo.runtime.fn;

import java.math.BigInteger;
import lambda.rodeo.runtime.lambda.Lambda0;

public class IntegerFunctions {
  public static Lambda0<BigInteger> makeAdd(Lambda0<BigInteger> left, Lambda0<BigInteger> right) {
    return () -> left.get().add(right.get());
  }

  public static Lambda0<BigInteger> makeSubtract(Lambda0<BigInteger> left, Lambda0<BigInteger> right) {
    return () -> left.get().subtract(right.get());
  }

  public static Lambda0<BigInteger> makeDivide(Lambda0<BigInteger> left, Lambda0<BigInteger> right) {
    return () -> left.get().divide(right.get());
  }

  public static Lambda0<BigInteger> makeMultiply(Lambda0<BigInteger> left, Lambda0<BigInteger> right) {
    return () -> left.get().multiply(right.get());
  }

  public static Lambda0<BigInteger> makeNegate(Lambda0<BigInteger> item) {
    return () -> item.get().negate();
  }
}

package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;

public class BasicModule1 {

  public static Lambda0<BigInteger> fibonacci(Lambda0<BigInteger> v1) {
    /**
     * def closure1(v1: Int) => () => Int {
     *   (v2: Int) => {
     *     () => v1 + v2;
     *   }(v1);
     * }
     */
    Lambda1<Lambda0<BigInteger>, Lambda0<Lambda0<BigInteger>>> f = (v2) -> () -> IntegerFunctions.makeAdd(v1, v2);
    return f.apply(v1).apply();
  }
}

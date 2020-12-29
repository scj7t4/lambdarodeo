package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.BitSet;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;

public interface BasicModule1 {

  public static Lambda0<BigInteger> fibonacci(Lambda0<BigInteger> v1) {
    /**
     * def closure1(v1: Int) => () => Int {
     *   (v2: Int) => {
     *     () => v1 + v2;
     *   }(v1);
     * }
     */
    return outer(v1).apply(v1);
  }

  public static Lambda1<Lambda0<BigInteger>, Lambda0<BigInteger>> outer(Lambda0<BigInteger> v1) {
    return (v2) -> IntegerFunctions.makeAdd(v1, v2);
  }
}

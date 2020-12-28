package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.BitSet;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;

public interface BasicModule1 {

  public static Lambda0<BigInteger> fibonacci(Lambda0<Lambda0<BigInteger>> fn) {
    return IntegerFunctions.makeAdd(
        Lambda0.make(fn.get().get()),
        Lambda0.make(BigInteger.valueOf(6L)));
  }

}

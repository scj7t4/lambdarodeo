package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.BitSet;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.lambda.Value;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;

public class BasicModule1 {

  public static Lambda0<Lambda1<Lambda0<Atom>, Lambda0<BigInteger>>> fibonacci() {
    /**
     * def closure1(v1: Int) => () => Int {
     *   (v2: Int) => {
     *     () => v1 + v2;
     *   }(v1);
     * }
     */
//    Lambda1<Lambda0<BigInteger>, Lambda0<Lambda0<BigInteger>>> f = (v2) -> () -> IntegerFunctions.makeAdd(v1, v2);
//
//    return f.apply(v1);
    return Value.of((v) -> Value.of(1337));
  }
}

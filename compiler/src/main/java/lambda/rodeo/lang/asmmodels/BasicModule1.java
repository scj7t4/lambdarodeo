package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.Objects;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.lambda.Value;

public class BasicModule1 {

  public static Lambda0<BigInteger> sum(Lambda0<BigInteger> v1) {
    if (Objects.equals(v1.get(), BigInteger.ZERO)) {
      return Value.of(0);
    } else if (Objects.equals(v1.get(), BigInteger.ONE)) {
      return Value.of(1);
    } else {
      return Trampoline.make(() ->
          IntegerFunctions.makeAdd(v1, sum(IntegerFunctions.makeSubtract(v1, Value.of(1)))));
    }
  }

  public static void main(String[] args) {
    Lambda0<BigInteger> sum = sum(Value.of(20000));
    System.out.println(sum.get());
  }
}

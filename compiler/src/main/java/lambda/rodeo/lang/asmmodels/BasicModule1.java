package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import lambda.rodeo.runtime.patterns.matchers.TypeMatcher;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;
import lambda.rodeo.runtime.types.LRType;
import lambda.rodeo.runtime.types.LRTypeUnion;

public class BasicModule1 {

  public static TypeMatcher typeMatcher = new TypeMatcher(LRString.INSTANCE);

  public static BigInteger fibonacci(Object arg) {
    BigInteger var;
    var = (BigInteger) arg;
    return var;
  }

}

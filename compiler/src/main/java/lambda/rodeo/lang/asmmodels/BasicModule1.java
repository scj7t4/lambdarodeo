package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import lambda.rodeo.runtime.patterns.matchers.TypeMatcher;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRInterface;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;
import lambda.rodeo.runtime.types.LRType;
import lambda.rodeo.runtime.types.LRTypeUnion;

public class BasicModule1 {

  public static TypeMatcher typeMatcher = new TypeMatcher(LRString.INSTANCE);

  public static LRType fibonacci(LRObject arg) {
    Map<String, LRType> members = new HashMap<>();
    members.put("mem1", LRInteger.INSTANCE);
    members.put("mem2", LRString.INSTANCE);
    return new LRInterface(members);
  }

}

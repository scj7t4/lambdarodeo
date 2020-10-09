package lambda.rodeo.lang.asmmodels;

import java.math.BigInteger;
import java.util.Arrays;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRLambda;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;
import lambda.rodeo.runtime.types.LRType;

public class BasicModule1 {
  public static LRObject fibonacci() {
    int bugger = 69420;
    return LRObject.create()
        .set("key1", "v1", LRString.INSTANCE)
        .set("key2", "v2", LRInteger.INSTANCE)
        .done();
  }
}

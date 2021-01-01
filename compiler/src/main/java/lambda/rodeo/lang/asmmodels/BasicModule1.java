package lambda.rodeo.lang.asmmodels;

import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;
import lambda.rodeo.runtime.types.LRType;
import lambda.rodeo.runtime.types.LRTypeUnion;

public class BasicModule1 {

  public static LRType fibonacci() {
    return new LRTypeUnion(new LRType[]{
        LRInteger.INSTANCE,
        LRString.INSTANCE,
        LRInteger.INSTANCE,
        LRString.INSTANCE,
        LRInteger.INSTANCE,
        LRString.INSTANCE
    });
  }

}

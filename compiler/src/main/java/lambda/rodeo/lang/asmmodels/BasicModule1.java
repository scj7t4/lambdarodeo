package lambda.rodeo.lang.asmmodels;

import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRString;

public interface BasicModule1 {

  public static LRObject fibonacci() {
    return LRObject.create()
        .set("key1", "v1", LRString.INSTANCE)
        .set("key2", "v2", LRInteger.INSTANCE)
        .done();
  }

}

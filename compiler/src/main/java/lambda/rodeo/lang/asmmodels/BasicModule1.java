package lambda.rodeo.lang.asmmodels;

import lambda.rodeo.runtime.types.LRInteger;
import lambda.rodeo.runtime.types.LRObject;
import lambda.rodeo.runtime.types.LRPackaged;
import lambda.rodeo.runtime.types.LRString;

public interface BasicModule1 {

  public interface MyInterface {

    String getKey1();

    String getKey2();
  }

  public static class MyInterfacePackaged implements MyInterface, LRPackaged {

    LRObject packaged;

    public MyInterfacePackaged(LRObject packaged) {
      this.packaged = packaged;
    }

    @Override
    public String getKey1() {
      return (String) packaged.get("key1");
    }

    @Override
    public String getKey2() {
      return (String) packaged.get("key2");
    }

    @Override
    public LRObject unpack$() {
      return packaged;
    }
  }

  public static MyInterface fibonacci() {
    return new MyInterfacePackaged(LRObject.create()
        .set("key1", "v1", LRString.INSTANCE)
        .set("key2", "v2", LRInteger.INSTANCE)
        .done());
  }

  void someMethod();
}

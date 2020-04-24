package lambda.rodeo.lang.utils;

import lambda.rodeo.lang.ModuleAst;

public class CompileUtils {
  public static class TestClassLoader extends ClassLoader {

    public TestClassLoader(ClassLoader parent) {
      super(parent);
    }

    public Class<?> defineClass(String name, byte[] b) {
      return defineClass(name, b, 0, b.length);
    }
  }

  public static Class<?> createClass(ModuleAst moduleAst) {
    return new TestClassLoader(CompileUtils.class.getClassLoader())
        .defineClass(moduleAst.getName(), moduleAst.compile());
  }
}

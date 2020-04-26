package lambda.rodeo.lang.utils;

import java.io.PrintWriter;
import lambda.rodeo.lang.ModuleAst;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.util.ASMifier;
import org.objectweb.asm.util.TraceClassVisitor;

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

  public static void asmifyModule(ModuleAst moduleAst) {
    ASMifier asMifier = new ASMifier();
    ClassReader classReader = new ClassReader(moduleAst.compile());
    PrintWriter printWriter = new PrintWriter(System.out);
    TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, asMifier, printWriter);
    classReader.accept(traceClassVisitor, 0);
  }
}

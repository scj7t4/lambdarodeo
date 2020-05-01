package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.PrintWriter;
import lambda.rodeo.lang.ModuleAst;
import lambda.rodeo.lang.compilation.CompileContext;
import org.hamcrest.collection.IsEmptyCollection;
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
    CompileContext compileContext = CompileContext.builder().build();
    Class<?> compiledClass = new TestClassLoader(CompileUtils.class.getClassLoader())
        .defineClass(moduleAst.getName(),
            moduleAst.toTypedModuleAst(compileContext).compile(compileContext));
    assertThat(compileContext.getCompileErrorCollector().getCompileErrors(),
        IsEmptyCollection.empty());
    return compiledClass;
  }

  public static void asmifyModule(ModuleAst moduleAst) {
    CompileContext compileContext = CompileContext.builder().build();
    ASMifier asMifier = new ASMifier();
    ClassReader classReader = new ClassReader(
        moduleAst.toTypedModuleAst(compileContext).compile(compileContext));
    PrintWriter printWriter = new PrintWriter(System.out);
    TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, asMifier, printWriter);
    classReader.accept(traceClassVisitor, 0);
  }
}

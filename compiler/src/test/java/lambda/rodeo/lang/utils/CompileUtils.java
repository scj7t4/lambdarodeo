package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;

import java.io.PrintWriter;
import java.util.Collections;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s3compileable.CompileableModule;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import org.hamcrest.Matchers;
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

  public static CompileErrorCollector expectCompileErrors(ModuleAst moduleAst) {
    S2CompileContextImpl compileContext = S2CompileContextImpl.builder()
        .source("test")
        .build();
    CompileableModule compileableModule = convertToCompileableModule(moduleAst, compileContext);
    compileableModule.compile(compileContext);
    assertThat(compileContext.getCompileErrorCollector().getCompileErrors(), Matchers.not(empty()));
    return compileContext.getCompileErrorCollector();
  }

  public static Class<?> createClass(ModuleAst moduleAst) {
    S2CompileContextImpl compileContext = S2CompileContextImpl.builder()
        .source("test")
        .build();
    CompileableModule compileableModule = convertToCompileableModule(moduleAst, compileContext);

    Class<?> compiledClass = new TestClassLoader(CompileUtils.class.getClassLoader())
        .defineClass(compileableModule.getName(), compileableModule.compile(compileContext));
    assertThat(
        "There were compile errors: \n" + compileContext.getCompileErrorCollector(),
        compileContext.getCompileErrorCollector().getCompileErrors(),
        IsEmptyCollection.empty());
    return compiledClass;
  }

  private static CompileableModule convertToCompileableModule(ModuleAst moduleAst,
      S2CompileContextImpl compileContext) {
    // TODO: This will need to change when compiling multiple modules:
    ModuleScope moduleScope = moduleAst.getModuleScope(compileContext);
    TypedModuleScope typedModuleScope = moduleScope.toTypedModuleScope(Collections.emptyList());
    TypedModule typedModule = moduleAst.toTypedModuleAst(compileContext, typedModuleScope);
    return typedModule.toCompileableModule();
  }

  public static void asmifyModule(ModuleAst moduleAst) {
    S2CompileContextImpl compileContext = S2CompileContextImpl.builder().build();
    ASMifier asMifier = new ASMifier();
    ClassReader classReader = new ClassReader(
        convertToCompileableModule(moduleAst, compileContext)
            .compile(compileContext));
    PrintWriter printWriter = new PrintWriter(System.out);
    TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, asMifier, printWriter);
    classReader.accept(traceClassVisitor, 0);
  }
}

package lambda.rodeo.lang.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.CompileUnit;
import lambda.rodeo.lang.CompilerChain;
import lambda.rodeo.lang.CompilerChain.CompileResult;
import lambda.rodeo.lang.S3Compiler.CompiledUnit;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.s3compileable.CompileableModule;
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

  public static CompileErrorCollector expectCompileErrors(List<CompileUnit> units)
      throws IOException {
    CompilerChain chain = CompilerChain.builder()
        .compileUnits(units)
        .build();
    CompileResult compile = chain.compile();
    assertThat(compile.getErrorCollector().getCompileErrors(), Matchers.not(empty()));
    assertThat(compile.isSuccess(), equalTo(false));
    return compile.getErrorCollector();
  }

  public static Class<?> createClass(ModuleAst moduleAst) {
    S2CompileContextImpl compileContext = S2CompileContextImpl.builder()
        .source("test")
        .build();
    CompileableModule compileableModule = convertToCompileableModule(moduleAst, compileContext);

    Class<?> compiledClass = new TestClassLoader(CompileUtils.class.getClassLoader())
        .defineClass(compileableModule.getName(),
            compileableModule.compile(compileContext).get(null));
    assertThat(
        "There were compile errors: \n" + compileContext.getCompileErrorCollector(),
        compileContext.getCompileErrorCollector().getCompileErrors(),
        IsEmptyCollection.empty());
    return compiledClass;
  }

  private static CompileableModule convertToCompileableModule(ModuleAst moduleAst,
      S2CompileContextImpl compileContext) {
    // TODO: This will need to change when compiling multiple modules:
    ModuleScope moduleScope = moduleAst.getModuleScope(compileContext, null);
    TypedModuleScope typedModuleScope = moduleScope.toTypedModuleScope(Collections.emptyList());
    TypedModule typedModule = moduleAst.toTypedModule(compileContext, typedModuleScope);
    return typedModule.toCompileableModule(compileContext);
  }

  public static void asmifyModule(ModuleAst moduleAst) {
    S2CompileContextImpl compileContext = S2CompileContextImpl.builder().build();
    ASMifier asMifier = new ASMifier();
    ClassReader classReader = new ClassReader(
        convertToCompileableModule(moduleAst, compileContext)
            .compile(compileContext).get(null));
    PrintWriter printWriter = new PrintWriter(System.out);
    TraceClassVisitor traceClassVisitor = new TraceClassVisitor(null, asMifier, printWriter);
    classReader.accept(traceClassVisitor, 0);
  }

  public static Map<String, Class<?>> createClasses(List<CompileUnit> units) throws IOException {
    CompilerChain chain = CompilerChain.builder()
        .compileUnits(units)
        .build();
    CompileResult compile = chain.compile();
    assertThat(compile.getErrorCollector().getCompileErrors(), empty());
    assertThat(compile.isSuccess(), equalTo(true));
    TestClassLoader classLoader = new TestClassLoader(CompileUtils.class.getClassLoader());

    assertThat(compile.getCompiledUnits().isPresent(), equalTo(true));
    List<CompiledUnit> compiledUnits = compile.getCompiledUnits().orElse(Collections.emptyList());

    Map<String, Class<?>> compiledClasses = new HashMap<>();

    for(CompiledUnit compiledUnit : compiledUnits) {
      Class<?> aClass = classLoader
          .defineClass(compiledUnit.getModuleName(), compiledUnit.getByteCode());
      compiledClasses.put(compiledUnit.getModuleName(), aClass);
    }

    return compiledClasses;
  }
}

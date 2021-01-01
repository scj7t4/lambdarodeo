package lambda.rodeo.lang.s2typed;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.lang.s2typed.type.TypedTypeDef;
import lambda.rodeo.lang.s3compileable.CompileableModule;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunction;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableStaticPattern;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class TypedModule {

  @NonNull
  private final ModuleAst moduleAst;
  @NonNull
  private final List<TypedFunction> functionAsts;
  @NonNull
  private final List<TypedTypeDef> typeDefs;
  @NonNull
  private final TypedModuleScope typedModuleScope;
  @NonNull
  private final Map<TypedCaseArg, TypedStaticPattern> staticPatterns;

  public CompileableModule toCompileableModule(
      S2CompileContext compileContext) {
    List<CompileableFunction> compileableFunctions = functionAsts.stream()
        .map(fn -> fn.toCompileableFunction(staticPatterns, typedModuleScope, compileContext))
        .collect(Collectors.toList());

    Map<CompileableCaseArg, CompileableStaticPattern> collected = compileableFunctions.stream()
        .map(CompileableFunction::getFunctionBody)
        .map(CompileableFunctionBody::getAllCaseArgs)
        .flatMap(Collection::stream)
        .distinct()
        .collect(Collectors.toMap(k -> k, v -> v.getStaticPattern().toCompileableStaticPattern(v)));

    return CompileableModule.builder()
        .compileableFunctions(compileableFunctions)
        .staticPatterns(collected)
        .typedModule(this)
        .build();
  }

  public String getInternalJavaName() {
    return getModuleAst().getInternalJavaName();
  }

  public String getModuleJVMDescriptor() {
    return getModuleAst().getModuleJVMDescriptor();
  }

  @NonNull
  public String getName() {
    return getModuleAst().getName();
  }

  public String getSourceFile() {
    return getModuleAst().getSourceFile();
  }
}

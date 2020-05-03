package lambda.rodeo.lang.s2typed;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s3compileable.CompileableModule;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedModule {

  private final ModuleAst moduleAst;
  private final List<TypedFunction> functionAsts;
  private final ModuleScope moduleScope;

  public CompileableModule toCompileableModule(List<TypedModule> modules) {
    CompileableModuleScope compiledModuleScope = CompileableModuleScope.builder()
        .thisScope(moduleScope)
        .importedModules(modules.stream()
            .map(TypedModule::getModuleScope)
            .collect(Collectors.toList()))
        .build();
    return CompileableModule.builder()
        .compileableFunctions(functionAsts.stream()
            .map(fn -> fn.toCompileableFunction(compiledModuleScope))
            .collect(Collectors.toList()))
        .moduleScope(compiledModuleScope)
        .typedModule(this)
        .build();
  }

  public String getInternalJavaName() {
    return getModuleAst().getInternalJavaName();
  }

  public String getModuleJVMDescriptor() {
    return getModuleAst().getModuleJVMDescriptor();
  }
}

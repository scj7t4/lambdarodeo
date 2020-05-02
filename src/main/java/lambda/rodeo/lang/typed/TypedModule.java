package lambda.rodeo.lang.typed;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.ast.ModuleAst;
import lambda.rodeo.lang.compileable.CompileableModule;
import lambda.rodeo.lang.typed.functions.TypedFunction;
import lambda.rodeo.lang.types.TypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedModule {

  private final ModuleAst moduleAst;
  private final List<TypedFunction> functionAsts;
  private final TypeScope moduleScope;

  public CompileableModule toCompileableModule(List<TypedModule> modules) {
    return CompileableModule.builder()
        .compileableFunctions(functionAsts.stream()
            .map(fn -> fn.toCompileableFunction(modules))
            .collect(Collectors.toList()))
        .moduleScope(moduleScope.toCompileableTypeScope(modules))
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

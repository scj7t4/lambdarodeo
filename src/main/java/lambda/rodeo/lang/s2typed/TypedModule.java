package lambda.rodeo.lang.s2typed;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s3compileable.CompileableModule;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class TypedModule {

  private final ModuleAst moduleAst;
  private final List<TypedFunction> functionAsts;
  private final TypedModuleScope typedModuleScope;

  public CompileableModule toCompileableModule() {
    return CompileableModule.builder()
        .compileableFunctions(functionAsts.stream()
            .map(TypedFunction::toCompileableFunction)
            .collect(Collectors.toList()))
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
}

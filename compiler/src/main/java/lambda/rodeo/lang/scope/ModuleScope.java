package lambda.rodeo.lang.scope;

import java.util.List;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ModuleScope {

  private final ModuleAst thisModule;
  /**
   * Alias name of this scope, set to null if this module is the main one and not imported.
   */
  private final String alias;
  @NonNull
  private final List<FunctionAst> functions;
  @NonNull
  private final List<TypeDef> types;

  public TypedModuleScope toTypedModuleScope(List<ModuleScope> importedModules) {
    return TypedModuleScope.builder()
        .thisScope(this)
        .importedModules(importedModules)
        .build();
  }

  public String getSimpleModuleName() {
    return thisModule.getSimpleName();
  }
}

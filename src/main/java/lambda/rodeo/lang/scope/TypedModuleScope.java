package lambda.rodeo.lang.scope;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedModuleScope {
  private final ModuleScope thisScope;
  private final List<ModuleScope> importedModules;

  //TODO: this needs more work for function overloading, module imports...
  public Optional<FunctionAst> getCallTarget(String callTarget) {
    return thisScope.getFunctions()
        .stream()
        .filter(fn -> Objects.equals(callTarget, fn.getName()))
        .findFirst();
  }
}

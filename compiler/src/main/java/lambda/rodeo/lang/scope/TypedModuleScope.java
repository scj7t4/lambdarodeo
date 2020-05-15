package lambda.rodeo.lang.scope;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedModuleScope {
  private final ModuleScope thisScope;
  private final List<ModuleScope> importedModules;

  public Optional<FunctionAst> getCallTarget(String callTarget,
      List<LambdaRodeoType> argSig) {
    return thisScope.getFunctions()
        .stream()
        .filter(fn -> Objects.equals(callTarget, fn.getName()))
        .filter(fn -> fn.hasSignature(argSig))
        .findFirst();
  }
}

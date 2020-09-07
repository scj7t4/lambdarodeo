package lambda.rodeo.lang.scope;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedModuleScope {
  private final ModuleScope thisScope;
  private final List<ModuleScope> importedModules;

  public Optional<FunctionAst> getCallTarget(String callTarget,
      List<CompileableType> argSig) {
    Optional<FunctionAst> fnInThisModule = thisScope.getFunctions()
        .stream()
        .filter(fn -> Objects.equals(callTarget, fn.getName()))
        .filter(fn -> fn.hasSignature(argSig))
        .findFirst();

    if(fnInThisModule.isPresent()) {
      return fnInThisModule;
    }

    String[] callTargetTokens = callTarget.split("\\.");

    if(callTargetTokens.length != 2) {
      return Optional.empty();
    }

    String module = callTargetTokens[0];
    String fnName = callTargetTokens[1];

    return importedModules.stream()
        .filter(imported -> Objects.equals(imported.getAlias(), module))
        .flatMap(imported -> imported.getFunctions().stream())
        .filter(fn -> Objects.equals(fnName, fn.getName()))
        .filter(fn -> fn.hasSignature(argSig))
        .findFirst();
  }

  public boolean nameExists(String callTarget) {
    return thisScope.getFunctions()
        .stream()
        .anyMatch(fn -> Objects.equals(callTarget, fn.getName()));
  }
}

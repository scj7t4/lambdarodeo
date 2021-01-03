package lambda.rodeo.lang.scope;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.StringType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedModuleScope implements TypeResolver {

  private final ModuleScope thisScope;
  private final List<ModuleScope> importedModules;
  private final List<TypedFunction> typedFunctions;

  @Override
  public Optional<TypeDef> getTypeTarget(String typeTarget) {
    if (Objects.equals(typeTarget, "String")) {
      return Optional.of(TypeDef.builder()
          .type(StringType.INSTANCE)
          .identifier("String")
          .build());
    }
    if (Objects.equals(typeTarget, "Int")) {
      return Optional.of(TypeDef.builder()
          .type(IntType.INSTANCE)
          .identifier("Int")
          .build());
    }

    Optional<TypeDef> matchedInterface = this.thisScope.getTypes()
        .stream()
        .filter(typeDef -> Objects.equals(typeTarget, typeDef.getIdentifier()))
        .findFirst();
    if (matchedInterface.isPresent()) {
      return matchedInterface;
    }

    String[] callTargetTokens = typeTarget.split("\\.");
    if (callTargetTokens.length != 2) {
      return Optional.empty();
    }

    String module = callTargetTokens[0];
    String typeName = callTargetTokens[1];

    return importedModules.stream()
        .filter(imported -> Objects.equals(imported.getAlias(), module))
        .flatMap(imported -> imported.getTypes().stream())
        .filter(type -> Objects.equals(typeName, type.getIdentifier()))
        .findFirst();
  }

  public Optional<FunctionAst> getCallTarget(String callTarget,
      List<CompileableType> argSig) {
    Optional<FunctionAst> fnInThisModule = thisScope.getFunctions()
        .stream()
        .filter(fn -> Objects.equals(callTarget, fn.getName()))
        .filter(fn -> fn.hasArity(argSig.size()))
        .findFirst();

    if (fnInThisModule.isPresent()) {
      return fnInThisModule;
    }

    String[] callTargetTokens = callTarget.split("\\.");

    if (callTargetTokens.length != 2) {
      return Optional.empty();
    }

    String module = callTargetTokens[0];
    String fnName = callTargetTokens[1];

    return importedModules.stream()
        .filter(imported -> Objects.equals(imported.getAlias(), module))
        .flatMap(imported -> imported.getFunctions().stream())
        .filter(fn -> Objects.equals(fnName, fn.getName()))
        .filter(fn -> fn.hasArity(argSig.size()))
        .findFirst();
  }

  public boolean nameExists(String callTarget) {
    return thisScope.getFunctions()
        .stream()
        .anyMatch(fn -> Objects.equals(callTarget, fn.getName()));
  }
}

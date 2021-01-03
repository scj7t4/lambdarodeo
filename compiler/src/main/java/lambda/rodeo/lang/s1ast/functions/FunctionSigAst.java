package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionSignature;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class FunctionSigAst implements AstNode {

  private final String name;
  @Builder.Default
  private final List<TypedVar> arguments = new ArrayList<>();
  private final int startLine;
  private final int endLine;
  private final int characterStart;
  @NonNull
  private final LambdaRodeoType declaredReturnType;

  public TypeScope getInitialTypeScope(
      TypeScope moduleScope,
      TypeResolver typeResolver,
      S2CompileContext compileContext) {
    TypeScope typeScope = moduleScope;
    for (TypedVar arg : arguments) {
      typeScope = typeScope.declare(
          arg.getName(),
          arg.getType().toCompileableType(typeResolver, compileContext)
      );
    }
    return typeScope;
  }

  public void checkCollisionAgainstModule(TypedModuleScope typedModuleScope,
      S2CompileContext compileContext) {
    for (TypedVar arg : arguments) {
      if (typedModuleScope.nameExists(arg.getName())) {
        compileContext.getCompileErrorCollector().collect(
            CompileError.identifierAlreadyDeclaredInScope(arg, arg.getName())
        );
      }
    }
  }

  public TypedFunctionSignature toTypedFunctionSignature(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {
    return TypedFunctionSignature.builder()
        .arguments(arguments.stream()
            .map(typedVar -> typedVar.toS2TypedVar(typeResolver, compileContext))
            .collect(Collectors.toList()))
        .from(this)
        .declaredReturnType(declaredReturnType.toCompileableType(typeResolver,
            compileContext
        ))
        .build();
  }
}

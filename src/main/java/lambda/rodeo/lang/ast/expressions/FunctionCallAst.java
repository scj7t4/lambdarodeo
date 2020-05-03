package lambda.rodeo.lang.ast.expressions;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.FunctionType;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class FunctionCallAst implements ExpressionAst {

  private final String callTarget;
  private final List<ExpressionAst> args;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  //TODO: Wrong...
  public TypedExpression toTypedExpression(TypeScope typeScope, CompileContext compileContext) {
    Optional<Entry> entry = typeScope.get(callTarget);
    if(!entry.isPresent()) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.undefinedIdentifier(callTarget, this));
      return Atom.UNDEFINED.toTypedExpressionAst();
    }

    Type entryType = entry.get().getType();

    if(!(entryType instanceof FunctionType)) {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.triedToCallNonFunction(callTarget, this));
      return Atom.UNDEFINED.toTypedExpressionAst();
    }

    FunctionType functionType = (FunctionType) entryType;
    Type declaredReturnType = functionType.getFunctionAst().getFunctionSigAst()
        .getDeclaredReturnType();

    return TypedFunctionCall.builder()
        .args(args.stream()
            .map(arg -> arg.toTypedExpression(typeScope, compileContext))
            .collect(Collectors.toList()))
        .functionCallAst(this)
        .returnType(declaredReturnType)
        .typeScope(typeScope)
        .build();

  }
}

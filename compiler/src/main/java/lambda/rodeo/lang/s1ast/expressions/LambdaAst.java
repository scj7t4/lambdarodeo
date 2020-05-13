package lambda.rodeo.lang.s1ast.expressions;

import static lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst.getTypedStatements;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.TypedVar;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedLambda;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Lambda;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class LambdaAst implements ExpressionAst {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final List<TypedVar> arguments;

  @NonNull
  private final List<StatementAst> statements;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      CompileContext compileContext) {

    TypeScope lambdaScope = TypeScope.EMPTY;

    for (TypedVar typedVar : arguments) {
      lambdaScope = lambdaScope.declare(typedVar.getName(), typedVar.getType());
    }

    List<TypedVar> scopeArgs = new ArrayList<>();
    for (String var : getReferencedVariables()) {
      Type closureVarType = scope.get(var)
          .findFirst()
          .map(Entry::getType)
          .orElseThrow(() -> new UnsupportedOperationException(
              "Variable '" + var + "' is missing from scope?"));
      scopeArgs.add(TypedVar.builder()
          .name(var)
          .type(closureVarType)
          .build());
      lambdaScope = lambdaScope.declare(var, closureVarType);
    }

    List<TypedStatement> typedStatements = getTypedStatements(lambdaScope, typedModuleScope,
        compileContext, statements);

    List<? extends Type> argTypes = arguments.stream()
        .map(TypedVar::getType)
        .collect(Collectors.toList());

    return TypedLambda.builder()
        .expr(this)
        .scopeArgs(scopeArgs)
        .type(Lambda.builder()
            .args(argTypes)
            .returnType(
                typedStatements
                    .get(typedStatements.size() - 1)
                    .getTypedExpression()
                    .getType())
            .build())
        .typedStatements(typedStatements)
        .build();
  }

  @Override
  public Set<String> getReferencedVariables() {
    // The type scope will be the declared args, plus the args in the scope used by the
    // statements, but not those declared in the scope x_x
    Set<String> assignedVars = statements.stream()
        .map(StatementAst::getAssignment)
        .flatMap(x -> x.newDeclarations().stream())
        .collect(Collectors.toSet());
    return statements.stream()
        .flatMap(statement -> statement.getReferencedVariables().stream())
        .filter(referencedVar -> !assignedVars.contains(referencedVar))
        .collect(Collectors.toSet());
  }
}

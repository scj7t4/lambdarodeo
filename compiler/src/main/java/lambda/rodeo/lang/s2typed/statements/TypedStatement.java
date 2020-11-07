package lambda.rodeo.lang.s2typed.statements;

import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s3compileable.statement.CompileableStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedStatement {

  private final StatementAst statementAst;
  private final TypeScope beforeTypeScope;
  private final TypeScope afterTypeScope;
  private final TypedExpression typedExpression;
  private final TypedAssignment typedAssignment;

  public CompileableStatement toCompileableStatement(
      CollectsErrors compileContext) {
    return CompileableStatement.builder()
        .afterTypeScope(afterTypeScope.toCompileableTypeScope())
        .beforeTypeScope(beforeTypeScope.toCompileableTypeScope())
        .compileableExpr(typedExpression.toCompileableExpr(compileContext))
        .compileableAssignment(Optional.ofNullable(typedAssignment)
            .map(TypedAssignment::toCompileableAssignment)
            .orElse(null))
        .typedStatement(this)
        .build();
  }
}

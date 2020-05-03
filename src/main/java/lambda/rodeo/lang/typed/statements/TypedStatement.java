package lambda.rodeo.lang.typed.statements;

import java.util.Optional;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.compileable.statement.CompileableStatement;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
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
      CompileableModuleScope compileableModuleScope) {
    return CompileableStatement.builder()
        .afterTypeScope(afterTypeScope.toCompileableTypeScope())
        .beforeTypeScope(beforeTypeScope.toCompileableTypeScope())
        .compileableExpr(typedExpression.toCompileableExpr(compileableModuleScope))
        .compileableAssignment(Optional.ofNullable(typedAssignment)
            .map(TypedAssignment::toCompileableAssignment)
            .orElse(null))
        .typedStatement(this)
        .build();
  }
}

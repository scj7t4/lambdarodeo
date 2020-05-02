package lambda.rodeo.lang.typed.statements;

import java.util.List;
import java.util.Optional;
import lambda.rodeo.lang.ast.statements.StatementAst;
import lambda.rodeo.lang.compileable.statement.CompileableStatement;
import lambda.rodeo.lang.typed.TypedModule;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.types.TypeScope;
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
      List<TypedModule> modules) {
    return CompileableStatement.builder()
        .afterTypeScope(afterTypeScope.toCompileableTypeScope(modules))
        .beforeTypeScope(beforeTypeScope.toCompileableTypeScope(modules))
        .compileableExpr(typedExpression.toCompileableExpr())
        .compileableAssignment(Optional.ofNullable(typedAssignment)
            .map(TypedAssignment::toCompileableAssignment)
            .orElse(null))
        .typedStatement(this)
        .build();
  }
}

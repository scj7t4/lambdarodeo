package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class FunctionBodyAst {

  private final List<StatementAst> statements;
  private final List<PatternCaseAst> patternCases;

  public TypedFunctionBody toTypedFunctionBodyAst(
      TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    TypeScope current = initialTypeScope;
    List<TypedStatement> typedStatements = new ArrayList<>();
    for (StatementAst statement : statements) {
      TypedStatement typedStatement = statement.toTypedStatementAst(
          current,
          typedModuleScope,
          compileContext);
      current = typedStatement.getAfterTypeScope();
      typedStatements.add(typedStatement);
    }

    return TypedFunctionBody.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .statements(typedStatements)
        .build();
  }
}

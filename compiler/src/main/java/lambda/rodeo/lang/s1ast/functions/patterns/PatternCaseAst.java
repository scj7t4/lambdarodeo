package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PatternCaseAst {

  private final List<StatementAst> statements;
  private final List<CaseArgAst> caseArgs;

  public static List<TypedStatement> getTypedStatements(TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope, CompileContext compileContext,
      List<StatementAst> statements) {
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
    return typedStatements;
  }

  public TypedPatternCase toTypedPatternCase(
      TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    List<TypedStatement> typedStatements = getTypedStatements(initialTypeScope, typedModuleScope,
        compileContext, statements);

    return TypedPatternCase.builder()
        .typedStatements(typedStatements)
        .typedCaseArgs(caseArgs
            .stream()
            .map(arg -> arg.toTypedCaseArg(initialTypeScope, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .build();
  }

  public void checkForLastStatementAssignment(CompileContext compileContext) {
    StatementAst lastStatement = statements.get(statements.size() - 1);
    if (lastStatement.getAssignment() != null) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.lastStatementCannotBeAssignment(lastStatement)
      );
    }
  }
}

package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class PatternCaseAst implements AstNode {

  private final List<StatementAst> statements;
  private final List<CaseArgAst> caseArgs;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public static List<TypedStatement> getTypedStatements(TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext,
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
      ToTypedFunctionContext compileContext) {
    List<TypedStatement> typedStatements = getTypedStatements(initialTypeScope, typedModuleScope,
        compileContext, statements);

    return TypedPatternCase.builder()
        .typedStatements(typedStatements)
        .typedCaseArgs(caseArgs
            .stream()
            .map(arg -> arg.toTypedCaseArg(initialTypeScope, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .patternCaseAst(this)
        .build();
  }

  public void checkForLastStatementAssignment(S1CompileContext compileContext) {
    StatementAst lastStatement = statements.get(statements.size() - 1);
    if (lastStatement.getAssignment() != null) {
      compileContext.getCompileErrorCollector().collect(
          CompileError.lastStatementCannotBeAssignment(lastStatement)
      );
    }
  }
}

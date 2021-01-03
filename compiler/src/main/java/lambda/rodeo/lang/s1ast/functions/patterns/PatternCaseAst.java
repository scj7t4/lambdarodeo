package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s1ast.statements.StatementAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.scope.DerivedTypeScope;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;


@Getter
@Builder
public class PatternCaseAst implements AstNode {

  @NonNull
  private final List<StatementAst> statements;
  @NonNull
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
    // If any of the case args are a typed one, change the scope type based on the case arg?
    DerivedTypeScope withTypeChecks = new DerivedTypeScope(initialTypeScope);
    List<ScopeReplaceAndCasts> casts = new ArrayList<>();

    for (int i = 0; i < caseArgs.size(); i++) {
      CaseArgAst caseArgAst = caseArgs.get(i);
      if (caseArgAst instanceof TypeArgAst) {
        LambdaRodeoType typeCheck = ((TypeArgAst) caseArgAst).getType();
        Entry uncastEntry = initialTypeScope.getByIndex(i)
            .findFirst()
            .orElseThrow(() -> new CriticalLanguageException("Tried to check type"));
        CompileableType castTo = typeCheck
            .toCompileableType(typedModuleScope, compileContext.getCompileContext()
            );
        int slot = withTypeChecks.maxSlot() + 1;
        withTypeChecks = withTypeChecks.replace(
            uncastEntry.getName(),
            castTo,
            i);
        casts.add(ScopeReplaceAndCasts.builder()
            .toCastTo(castTo)
            .oldSlot(i)
            .newSlot(slot)
            .build());
      }
    }
    final TypeScope scopeToUse = withTypeChecks;

    List<TypedStatement> typedStatements = getTypedStatements(
        scopeToUse,
        typedModuleScope,
        compileContext,
        statements);

    return TypedPatternCase.builder()
        .typedStatements(typedStatements)
        .typedCaseArgs(caseArgs
            .stream()
            .map(arg -> arg.toTypedCaseArg(scopeToUse, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .scopeReplaceAndCasts(casts)
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

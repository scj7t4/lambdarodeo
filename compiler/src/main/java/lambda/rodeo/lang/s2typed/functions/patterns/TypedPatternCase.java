package lambda.rodeo.lang.s2typed.functions.patterns;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s1ast.functions.patterns.ScopeReplaceAndCasts;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class TypedPatternCase {

  @NonNull
  private final List<TypedStatement> typedStatements;
  @NonNull
  private final List<TypedCaseArg> typedCaseArgs;
  @NonNull
  private final List<ScopeReplaceAndCasts> scopeReplaceAndCasts;
  @NonNull
  private final PatternCaseAst patternCaseAst;

  public CompileablePatternCase toCompileablePatternCase(
      Map<TypedCaseArg, TypedStaticPattern> staticPatterns,
      TypedModuleScope scope,
      CollectsErrors compileContext) {
    return CompileablePatternCase.builder()
        .caseArgs(typedCaseArgs.stream()
            .map(caseArg -> caseArg.toCompileableCaseArg(staticPatterns.get(caseArg),
                scope,
                compileContext))
            .collect(Collectors.toList()))
        .statements(typedStatements.stream()
            .map(typedStatement -> typedStatement.toCompileableStatement(compileContext))
            .collect(Collectors.toList()))
        .typedPatternCase(this)
        .build();
  }

  public TypeScope getFinalTypeScope() {
    return typedStatements.get(typedStatements.size() - 1).getAfterTypeScope();
  }

  public CompileableType getReturnedType() {
    return typedStatements.get(typedStatements.size() - 1).getTypedExpression().getType();
  }
}

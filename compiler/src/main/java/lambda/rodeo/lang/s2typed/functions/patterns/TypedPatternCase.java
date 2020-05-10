package lambda.rodeo.lang.s2typed.functions.patterns;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScopeImpl;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedPatternCase {

  private final List<TypedStatement> typedStatements;
  private final List<TypedCaseArg> typedCaseArgs;

  public CompileablePatternCase toCompileablePatternCase() {
    return CompileablePatternCase.builder()
        .caseArgs(typedCaseArgs.stream()
            .map(TypedCaseArg::toCompileableCaseArg)
            .collect(Collectors.toList()))
        .statements(typedStatements.stream()
            .map(TypedStatement::toCompileableStatement)
            .collect(Collectors.toList()))
        .build();
  }

  public TypeScope getFinalTypeScope() {
    return typedStatements.get(typedStatements.size() - 1).getAfterTypeScope();
  }
}

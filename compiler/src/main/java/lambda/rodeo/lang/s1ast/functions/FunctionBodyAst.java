package lambda.rodeo.lang.s1ast.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.scope.DerivedTypeScope;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@Builder
@EqualsAndHashCode
public class FunctionBodyAst {

  private final List<PatternCaseAst> patternCases;

  public TypedFunctionBody toTypedFunctionBodyAst(
      TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {

    DerivedTypeScope patternScope = new DerivedTypeScope(initialTypeScope);
    for(PatternCaseAst patternCase : patternCases) {
      TypedPatternCase typedPatternCase = patternCase.toTypedPatternCase(
          patternScope,
          typedModuleScope,
          compileContext);
      TypeScope finalPatternScope = typedPatternCase.getFinalTypeScope().parent();
      patternScope = new DerivedTypeScope(finalPatternScope, initialTypeScope);
    }

    return TypedFunctionBody.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .finalTypeScope(patternScope.parent())
        .patternCases(patternCases
            .stream()
            .map(x -> x.toTypedPatternCase(initialTypeScope, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .build();
  }

  public void checkForLastStatementAssignment(CompileContext compileContext) {
    for(PatternCaseAst patternCaseAst : patternCases) {
      patternCaseAst.checkForLastStatementAssignment(compileContext);
    }
  }
}

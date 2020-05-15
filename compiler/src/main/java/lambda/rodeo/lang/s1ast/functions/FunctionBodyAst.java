package lambda.rodeo.lang.s1ast.functions;

import java.util.ArrayList;
import java.util.List;
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
      ToTypedFunctionContext compileContext) {

    DerivedTypeScope patternScope = new DerivedTypeScope(initialTypeScope);
    List<TypedPatternCase> typedPatternCases = new ArrayList<>();
    for(PatternCaseAst patternCase : patternCases) {
      TypedPatternCase typedPatternCase = patternCase.toTypedPatternCase(
          patternScope,
          typedModuleScope,
          compileContext);
      typedPatternCases.add(typedPatternCase);
      TypeScope finalPatternScope = typedPatternCase.getFinalTypeScope().parent();
      patternScope = new DerivedTypeScope(finalPatternScope, initialTypeScope);
    }

    return TypedFunctionBody.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .finalTypeScope(patternScope.parent())
        .patternCases(typedPatternCases)
        .build();
  }

  public void checkForLastStatementAssignment(CompileContext compileContext) {
    for(PatternCaseAst patternCaseAst : patternCases) {
      patternCaseAst.checkForLastStatementAssignment(compileContext);
    }
  }
}

package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PatternCaseAst {

  private final FunctionBodyAst functionBodyAst;
  private final List<CaseArgAst> caseArgAsts;

  public TypedPatternCase toTypedPatternCase(
      TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      CompileContext compileContext) {
    return TypedPatternCase.builder()
        .typedFunctionBody(functionBodyAst
            .toTypedFunctionBodyAst(initialTypeScope, typedModuleScope, compileContext))
        .typedCaseArgs(caseArgAsts
            .stream()
            .map(arg -> arg.toTypedCaseArg(initialTypeScope, typedModuleScope, compileContext))
            .collect(Collectors.toList()))
        .build();
  }
}

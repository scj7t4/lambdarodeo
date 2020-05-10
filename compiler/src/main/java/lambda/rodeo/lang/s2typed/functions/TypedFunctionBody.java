package lambda.rodeo.lang.s2typed.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedFunctionBody {

  private final TypeScope initialTypeScope;
  private final TypeScope finalTypeScope;
  private final List<TypedPatternCase> patternCases;
  private final FunctionBodyAst functionBodyAst;


  public CompileableFunctionBody toCompileableFunctionBody() {
    return CompileableFunctionBody.builder()
        .initialTypeScope(initialTypeScope.toCompileableTypeScope())
        .finalTypeScope(finalTypeScope.toCompileableTypeScope())
        .typedFunctionBody(this)
        .patternCases(patternCases.stream()
        .map(TypedPatternCase::toCompileablePatternCase)
        .collect(Collectors.toList()))
        .build();
  }

}

package lambda.rodeo.lang.s2typed.functions;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lambda.rodeo.lang.scope.TypeScope;
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


  public CompileableFunctionBody toCompileableFunctionBody(
      Map<TypedCaseArg, TypedStaticPattern> staticPatterns,
      CollectsErrors compileContext) {

    List<CompileablePatternCase> compileablePatternCases = patternCases
        .stream()
        .map(patternCase -> patternCase.toCompileablePatternCase(staticPatterns, compileContext))
        .collect(Collectors.toList());

    return CompileableFunctionBody.builder()
        .initialTypeScope(initialTypeScope.toCompileableTypeScope())
        .finalTypeScope(finalTypeScope.toCompileableTypeScope())
        .typedFunctionBody(this)
        .patternCases(compileablePatternCases)
        .build();
  }

  public List<TypedCaseArg> getAllCaseArgs() {
    return patternCases.stream()
        .map(TypedPatternCase::getTypedCaseArgs)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

}

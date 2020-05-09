package lambda.rodeo.lang.s2typed.functions.patterns;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedPatternCase {

  private final TypedFunctionBody typedFunctionBody;
  private final List<TypedCaseArg> typedCaseArgs;

  public CompileablePatternCase toCompileablePatternCase() {
    return CompileablePatternCase.builder()
        .compileableCaseArgs(typedCaseArgs.stream()
            .map(TypedCaseArg::toCompileableCaseArg)
            .collect(Collectors.toList()))
        .build();
  }
}

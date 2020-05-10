package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableStaticPattern;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedStaticPattern {
  private final String matcherIdentifier;

  public CompileableStaticPattern toCompileableStaticPattern(CompileableCaseArg caseArg) {
    return CompileableStaticPattern.builder()
        .typedStaticPattern(this)
        .compileableCaseArg(caseArg)
        .build();
  }
}

package lambda.rodeo.lang.s3compileable.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileableStaticPattern {
  private final CompileableCaseArg compileableCaseArg;
  private final TypedStaticPattern typedStaticPattern;
}

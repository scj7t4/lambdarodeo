package lambda.rodeo.lang.s3compileable.functions.patterns;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileableCaseArgContext {
  private final int argIndex;
  private final String internalModuleName;
}

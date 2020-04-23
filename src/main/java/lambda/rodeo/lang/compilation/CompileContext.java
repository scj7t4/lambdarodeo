package lambda.rodeo.lang.compilation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileContext {

  private final String source;
  private final CompileErrorCollector compileErrorCollector = new CompileErrorCollector();
}

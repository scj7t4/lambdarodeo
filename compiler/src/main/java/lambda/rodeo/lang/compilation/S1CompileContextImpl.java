package lambda.rodeo.lang.compilation;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class S1CompileContextImpl implements S1CompileContext {

  private final String sourcePath;

  @Builder.Default
  private final CompileErrorCollector compileErrorCollector = new CompileErrorCollector();
}

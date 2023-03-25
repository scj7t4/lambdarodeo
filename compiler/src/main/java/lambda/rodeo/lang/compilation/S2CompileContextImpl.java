package lambda.rodeo.lang.compilation;

import java.util.HashMap;
import java.util.Map;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class S2CompileContextImpl implements S2CompileContext {

  @NonNull
  private final String sourcePath;

  @NonNull
  @Builder.Default
  private final Map<String, ModuleAst> modules = new HashMap<>();

  @Builder.Default
  @NonNull
  private final CompileErrorCollector compileErrorCollector = new CompileErrorCollector();
}

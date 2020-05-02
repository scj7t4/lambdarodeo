package lambda.rodeo.lang.types;

import lambda.rodeo.lang.typed.TypedModule;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class CompileableModuleType implements CompileableType {
  @NonNull
  private final TypedModule typedModule;
  private final ModuleType type;

}

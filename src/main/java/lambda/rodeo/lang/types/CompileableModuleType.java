package lambda.rodeo.lang.types;

import lambda.rodeo.lang.typed.TypedModule;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableModuleType implements CompileableType {
  @NonNull
  private final TypedModule typedModule;
  @NonNull
  private final ModuleType type;

}

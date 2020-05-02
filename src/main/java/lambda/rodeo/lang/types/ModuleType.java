package lambda.rodeo.lang.types;

import lambda.rodeo.lang.ast.ModuleAst;
import lambda.rodeo.lang.typed.TypedModule;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ModuleType implements Type {
  @NonNull
  private final ModuleAst moduleAst;

  @Override
  public Class<?> javaType() {
    return null;
  }

  @Override
  public String descriptor() {
    return moduleAst.getModuleJVMDescriptor();
  }

  @Override
  public CompileableType toCompileableType(TypedModule typedModule) {
    return CompileableModuleType.builder()
        .typedModule(typedModule)
        .build();
  }

  @Override
  public boolean allocateSlot() {
    return false;
  }
}

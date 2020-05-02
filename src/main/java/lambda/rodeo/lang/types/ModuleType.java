package lambda.rodeo.lang.types;

import lambda.rodeo.lang.ast.ModuleAst;
import lombok.Builder;
import lombok.NonNull;

@Builder
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
  public boolean allocateSlot() {
    return false;
  }
}

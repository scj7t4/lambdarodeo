package lambda.rodeo.lang.types;

import lambda.rodeo.lang.functions.FunctionAst;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class FunctionType implements Type {
  @NonNull
  private final FunctionAst functionAst;

  @Override
  public Class<?> javaType() {
    return null;
  }

  @Override
  public String descriptor() {
    return functionAst.getName();
  }

  @Override
  public boolean allocateSlot() {
    return false;
  }
}

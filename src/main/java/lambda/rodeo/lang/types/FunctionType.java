package lambda.rodeo.lang.types;

import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Builder
@EqualsAndHashCode
@Getter
public class FunctionType implements Type, CompileableType {
  @NonNull
  private final TypedFunction functionAst;

  @Override
  public Class<?> javaType() {
    return null;
  }

  @Override
  public String descriptor() {
    return functionAst.getName();
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public boolean allocateSlot() {
    return false;
  }

  @Override
  public Type getType() {
    return this;
  }
}

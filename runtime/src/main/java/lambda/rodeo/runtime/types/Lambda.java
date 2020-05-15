package lambda.rodeo.runtime.types;

import java.util.List;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Lambda implements Type, CompileableType {
  private final List<? extends Type> args;
  private final Type returnType;

  @Override
  public Class<?> javaType() {
    return Object.class;
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public Type getType() {
    return this;
  }
}

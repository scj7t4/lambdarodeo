package lambda.rodeo.runtime.types;

import java.util.List;
import java.util.function.Supplier;
import lambda.rodeo.runtime.types.asm.AsmType;
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
  public String getDescriptor() {
    return AsmType.getDescriptor(Supplier.class);
  }

  public String getFunctionDescriptor() {
    return "(" + ")" + returnType.getDescriptor();
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

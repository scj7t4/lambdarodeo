package lambda.rodeo.lang.util;

import lombok.AllArgsConstructor;
import org.objectweb.asm.Type;

public class FunctionDescriptorBuilder {

  @AllArgsConstructor
  public static class FunctionDescriptorFinish {

    private final Class<?> args[];

    public String returns(Class<?> returnType) {
      StringBuilder sb = new StringBuilder();
      sb.append('(');
      for (Class<?> type : args) {
        sb.append(Type.getDescriptor(type));
      }
      sb.append(')').append(Type.getDescriptor(returnType));
      return sb.toString();
    }
  }

  public static FunctionDescriptorFinish args(Class<?>... args) {
    return new FunctionDescriptorFinish(args);
  }
}

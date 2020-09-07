package lambda.rodeo.lang.types;

import java.util.List;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableLambdaType implements CompileableType {

  @NonNull
  private final List<CompileableType> args;

  @NonNull
  private final CompileableType returnType;

  private final LambdaRodeoType from;

  @Override
  public LambdaRodeoType getType() {
    return from;
  }

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(functionalRep());
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(functionalRep());
  }

  @Override
  public String getSignature() {
    StringBuilder sb = new StringBuilder("L");
    sb.append(Type.getInternalName(functionalRep()));
    sb.append("<");
    for (CompileableType arg : args) {
      sb.append(arg.getSignature());
    }
    sb.append(returnType.getSignature());
    sb.append(">;");
    return sb.toString();
  }

  public String getFunctionDescriptor() {
    StringBuilder sb = new StringBuilder("(");
    for (CompileableType arg : args) {
      sb.append(arg.getDescriptor());
    }
    sb.append(")").append(returnType.getDescriptor());
    return sb.toString();
  }

  public String getGenericFunctionDescriptor() {
    StringBuilder sb = new StringBuilder("(");
    for (CompileableType arg : args) {
      // Object because type erasure erases args.
      sb.append(Type.getDescriptor(Object.class));
    }
    sb.append(")").append(Type.getDescriptor(Object.class));
    return sb.toString();
  }

  public Class<?> functionalRep() {
    switch (args.size()) {
      case 0:
        return Lambda0.class;
      case 1:
        return Lambda1.class;
    }
    throw new RuntimeCriticalLanguageException(
        "Lambda function had too many arguments (" + args.size() + ")");
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("<*lambda>(");
    for(int i = 0; i < args.size(); i++) {
      CompileableType arg = args.get(i);
      sb.append(arg);
      if(i < args.size() - 1) {
        sb.append(",");
      }
    }
    sb.append(")=>").append(returnType);
    return sb.toString();
  }
}

package lambda.rodeo.runtime.types;

import java.util.List;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lambda.rodeo.runtime.types.asm.AsmType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class Lambda implements LambdaRodeoType, CompileableType {

  private final List<? extends LambdaRodeoType> args;
  private final LambdaRodeoType returnType;

  @Override
  public String getDescriptor() {
    return AsmType.getDescriptor(functionalRep());
  }

  @Override
  public String getSignature() {
    StringBuilder sb = new StringBuilder("L");
    sb.append(AsmType.getInternalName(functionalRep()));
    sb.append("<");
    for (LambdaRodeoType arg : args) {
      sb.append(arg.getSignature());
    }
    sb.append(returnType.getSignature());
    sb.append(">;");
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

  public String getFunctionDescriptor() {
    StringBuilder sb = new StringBuilder("(");
    for (LambdaRodeoType arg : args) {
      sb.append(arg.getDescriptor());
    }
    sb.append(")").append(returnType.getDescriptor());
    return sb.toString();
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }
}

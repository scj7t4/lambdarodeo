package lambda.rodeo.lang.types;


import lambda.rodeo.runtime.types.asm.AsmType;

public class StringType implements LambdaRodeoType, CompileableType {
  public static final StringType INSTANCE = new StringType();

  @Override
  public String getDescriptor() {
    return AsmType.getDescriptor(String.class);
  }

  @Override
  public String getInternalName() {
    return AsmType.getInternalName(String.class);
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

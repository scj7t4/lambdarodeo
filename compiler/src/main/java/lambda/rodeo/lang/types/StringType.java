package lambda.rodeo.lang.types;


import org.objectweb.asm.Type;

public class StringType implements LambdaRodeoType, CompileableType {
  public static final StringType INSTANCE = new StringType();

  @Override
  public String getDescriptor() {
    return Type.getDescriptor(String.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(String.class);
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

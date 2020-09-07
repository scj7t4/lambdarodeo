package lambda.rodeo.lang.types;

public interface LambdaRodeoType {

  CompileableType toCompileableType();

  default boolean assignableFrom(LambdaRodeoType other) {
    return this.equals(other);
  }
}

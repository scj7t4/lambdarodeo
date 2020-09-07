package lambda.rodeo.lang.types;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class DefinedType implements LambdaRodeoType {

  private final String declaration;

  @Override
  public CompileableType toCompileableType() {
    return null;
  }

  @Override
  public String toString() {
    return "Type<" + declaration + ">";
  }
}

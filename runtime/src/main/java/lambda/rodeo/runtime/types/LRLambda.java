package lambda.rodeo.runtime.types;

import java.util.List;
import lambda.rodeo.runtime.lambda.Lambda0;
import lambda.rodeo.runtime.lambda.Lambda1;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@AllArgsConstructor
@Getter
public class LRLambda implements LRType {

  private final List<LRType> arguments;
  private final LRType returnType;

  @Override
  public boolean assignableFrom(LRType type) {
    if (!(type instanceof LRLambda)) {
      return false;
    }
    LRLambda asLambda = (LRLambda) type;

    if(!returnType.assignableFrom(asLambda.getReturnType())) {
      return false;
    }

    List<LRType> otherArgs = asLambda.getArguments();

    if(otherArgs.size() != arguments.size()) {
      return false;
    }

    for (int i = 0; i < otherArgs.size(); i++) {
      if(!arguments.get(i).assignableFrom(otherArgs.get(i))) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean isObjectOfType(Object object) {
    if (arguments.size() == 0 && object instanceof Lambda0) {
      return true;
    } else if (arguments.size() == 1 && object instanceof Lambda1) {
      return true;
    }
    return false;
  }
}

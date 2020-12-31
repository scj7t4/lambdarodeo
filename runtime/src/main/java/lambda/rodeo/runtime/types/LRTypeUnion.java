package lambda.rodeo.runtime.types;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LRTypeUnion implements LRType {
  private final LRType left;
  private final LRType right;


  @Override
  public boolean assignableFrom(LRType type) {
    return left.assignableFrom(type) || right.assignableFrom(type);
  }

  public static LRTypeUnion make(LRType left, LRType right) {
    return new LRTypeUnion(left, right);
  }
}

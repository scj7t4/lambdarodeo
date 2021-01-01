package lambda.rodeo.runtime.types;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class LRTypeUnion implements LRType {
  private final LRType[] types;


  @Override
  public boolean assignableFrom(LRType type) {
    return Arrays.stream(types)
        .anyMatch(t -> t.assignableFrom(type));
  }

  @Override
  public boolean isObjectOfType(Object object) {
    return Arrays.stream(types)
        .anyMatch(t -> t.isObjectOfType(object));
  }

  public static LRTypeUnion make(LRType left, LRType right) {
    return new LRTypeUnion(new LRType[]{left, right});
  }
}

package lambda.rodeo.lang.s2typed.types;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.types.LambdaRodeoType;

public interface TypedInterface extends LambdaRodeoType {

  java.util.List<S2TypedVar> getMembers();

  // TODO: Test me
  // TODO: Refactor to make good error reporting possible.
  default boolean assignableFrom(LambdaRodeoType other) {
    if (!(other instanceof TypedInterface)) {
      return false;
    }
    List<S2TypedVar> members = getMembers();
    List<S2TypedVar> otherMembers = ((TypedInterface) other).getMembers();
    for (S2TypedVar member : members) {
      Optional<S2TypedVar> fieldInOther = otherMembers.stream()
          .filter(x -> Objects.equals(x.getName(), member.getName()))
          .findFirst();
      if (fieldInOther.isEmpty()) {
        return false;
      }
      S2TypedVar otherField = fieldInOther.get();
      if (!member.getType().assignableFrom(otherField.getType())) {
        return false;
      }
    }
    return true;
  }
}

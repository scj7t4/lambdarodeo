package lambda.rodeo.lang.s2typed.types;


import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AnonymousTypedInterface implements TypedInterface {
  private final List<S2TypedVar> members;
}

package lambda.rodeo.lang;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ModuleAst {

  @NonNull
  private final String name;
}
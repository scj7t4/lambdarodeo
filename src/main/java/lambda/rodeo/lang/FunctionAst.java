package lambda.rodeo.lang;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FunctionAst {
  private String name;
  private List<TypedVarAst> arguments;

}

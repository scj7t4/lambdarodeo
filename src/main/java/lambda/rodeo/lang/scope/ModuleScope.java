package lambda.rodeo.lang.scope;

import java.util.List;
import lambda.rodeo.lang.ast.ModuleAst;
import lambda.rodeo.lang.typed.functions.TypedFunction;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ModuleScope {

  private final ModuleAst thisModule;
  private final List<TypedFunction> functions;
}

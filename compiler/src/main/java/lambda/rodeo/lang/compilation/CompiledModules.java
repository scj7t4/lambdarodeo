package lambda.rodeo.lang.compilation;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.ModuleAst;

public class CompiledModules {

  private final Map<String, ModuleAst> modules = new HashMap<>();

  public void put(ModuleAst module) {
    modules.put(module.getName(), module);
  }

  public Optional<ModuleAst> getModule(String moduleName) {
    return Optional.ofNullable(modules.get(moduleName));
  }
}

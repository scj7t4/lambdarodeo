package lambda.rodeo.lang.scope;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileableModuleScope {
  private final ModuleScope thisScope;
  private final List<ModuleScope> importedModules;
}

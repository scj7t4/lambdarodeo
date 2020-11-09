package lambda.rodeo.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.S1Compiler.ModuleResult;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.CompileErrorCollector;
import lambda.rodeo.lang.compilation.S2CompileContextImpl;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.s1ast.ModuleImportAst;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
public class S2Compiler {

  @NonNull
  private final S1Compiler.FinalResult s1CompileResult;

  @NonNull
  private final CompileErrorCollector errorCollector;

  public TypedFinalResult compile() {
    // TODO: Test referencing Ifaces from other modules
    Map<String, ModuleAst> modules = s1CompileResult.getModules()
        .stream()
        .collect(Collectors.toMap(
            result -> result.getModuleAst().getName(),
            ModuleResult::getModuleAst)
        );
    Map<String, ModuleScope> moduleScopes = new HashMap<>();

    List<TypedModuleResult> typedModules = new ArrayList<>();

    for (S1Compiler.ModuleResult s1Module : s1CompileResult.getModules()) {
      S2CompileContextImpl context = S2CompileContextImpl.builder()
          .source(s1Module.getSource())
          .modules(modules)
          .build();

      @NonNull ModuleAst moduleAst = s1Module.getModuleAst();
      ModuleScope moduleScope = moduleAst.getModuleScope(context, null);

      List<ModuleImportAst> moduleImports = moduleAst.getImports();

      List<ModuleScope> imported = new ArrayList<>();

      for (ModuleImportAst moduleImportAst : moduleImports) {
        @NonNull String source = moduleImportAst.getSource();
        ModuleAst targetModule = modules.get(source);
        if (targetModule == null) {
          errorCollector.collect(CompileError.badImport(moduleImportAst, source));
          continue;
        }
        ModuleScope importedModuleScope = targetModule.getModuleScope(context, moduleImportAst.getAlias());
        imported.add(importedModuleScope);
      }

      TypedModuleScope typedModuleScope = moduleScope.toTypedModuleScope(imported);
      TypedModule typedModule = moduleAst.toTypedModule(context, typedModuleScope);
      typedModules.add(TypedModuleResult.builder()
          .module(typedModule)
          .source(s1Module.getSource())
          .success(context.getCompileErrorCollector().getCompileErrors().isEmpty())
          .build());
      errorCollector.collectAll(context.getCompileErrorCollector());
    }

    return TypedFinalResult.builder()
        .success(errorCollector.getCompileErrors().isEmpty())
        .typedModules(typedModules)
        .modules(modules)
        .errorCollector(errorCollector)
        .build();
  }

  @Builder
  @Getter
  public static class TypedModuleResult {

    @NonNull
    private final String source;
    @NonNull
    private final TypedModule module;

    private final boolean success;
  }

  @Builder
  @Getter
  public static class TypedFinalResult {

    private final boolean success;
    @NonNull
    private final List<TypedModuleResult> typedModules;
    @NonNull
    private final Map<String, ModuleAst> modules;
    @NonNull
    private final CompileErrorCollector errorCollector;
  }
}

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
import lambda.rodeo.lang.s1ast.ImportAst;
import lambda.rodeo.lang.s1ast.ImportAst.ImportType;
import lambda.rodeo.lang.s1ast.ModuleAst;
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
    // TODO: Compile each unit with its own error collector, deterimine if module errored,
    // TODO: Pass enough for S3 to make an S2 context.
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
      ModuleScope moduleScope = moduleAst.getModuleScope(context);

      List<ImportAst> moduleImports = moduleAst.getImports()
          .stream()
          .filter(imported -> imported.getImportType().equals(ImportType.MODULE))
          .collect(Collectors.toList());

      List<ModuleScope> imported = new ArrayList<>();

      for (ImportAst importAst : moduleImports) {
        @NonNull String source = importAst.getSource();
        ModuleAst targetModule = modules.get(source);
        if (targetModule == null) {
          errorCollector.collect(CompileError.badImport(importAst, source));
          continue;
        }
        ModuleScope importedModuleScope = moduleScopes
            .computeIfAbsent(source, (missingSource) -> targetModule.getModuleScope(context));
        imported.add(importedModuleScope);
      }

      TypedModuleScope typedModuleScope = moduleScope.toTypedModuleScope(imported);
      TypedModule typedModule = moduleAst.toTypedModuleAst(context, typedModuleScope);
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
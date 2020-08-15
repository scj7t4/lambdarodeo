package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@Builder
@EqualsAndHashCode
public class ModuleAst implements AstNode {

  public static final String THIS_MODULE = "$this";
  @NonNull
  private final String name;

  @Builder.Default
  private final List<FunctionAst> functionAsts = new ArrayList<>();

  @Builder.Default
  private final List<ImportAst> imports = new ArrayList<>();

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedModule toTypedModuleAst(
      CompileContext compileContext,
      TypedModuleScope typedModuleScope) {

    final TypeScope initialModuleScope = TypeScope.EMPTY;

    // Check for functions with same name an same airty
    Map<String, List<FunctionAst>> airtyCheck = new HashMap<>();
    for (FunctionAst fn : functionAsts) {
      String elixirName = fn.getSignature();
      airtyCheck.computeIfAbsent(elixirName, k -> new ArrayList<>()).add(fn);
    }

    airtyCheck.entrySet().stream()
        .filter(entry -> entry.getValue().size() > 1)
        .forEach(entry -> {
          List<FunctionAst> conflicts = entry.getValue();
          String signature = entry.getKey();
          conflicts.forEach(fn -> compileContext.getCompileErrorCollector()
              .collect(CompileError.identifierAlreadyDeclaredInScope(fn, signature))
          );
        });

    List<TypedFunction> typedFunctions = functionAsts
        .stream()
        .map(fn -> fn.toTypedFunctionAst(initialModuleScope, typedModuleScope, compileContext))
        .collect(Collectors.toList());

    final Map<TypedCaseArg, TypedStaticPattern> staticPatterns = new HashMap<>();
    final Set<TypedCaseArg> allArgsAllFns = new HashSet<>();
    for (TypedFunction fn : typedFunctions) {
      List<TypedCaseArg> caseArgs = fn.getFunctionBody().getAllCaseArgs();
      allArgsAllFns.addAll(caseArgs);
    }
    Iterator<TypedCaseArg> it = allArgsAllFns.iterator();
    for (int i = 0; it.hasNext(); i++) {
      TypedCaseArg caseArg = it.next();
      staticPatterns.put(caseArg, TypedStaticPattern.builder()
          .matcherIdentifier("$MATCHER_" + i)
          .build());
    }

    return TypedModule.builder()
        .moduleAst(this)
        .typedModuleScope(typedModuleScope)
        .functionAsts(typedFunctions)
        .staticPatterns(staticPatterns)
        .build();
  }

  public ModuleScope getModuleScope(CompileContext compileContext) {
    return ModuleScope.builder()
        .functions(functionAsts)
        .thisModule(this)
        .build();
  }

  public String getInternalJavaName() {
    return getName().replace(".", "/");
  }

  public String getModuleJVMDescriptor() {
    return "L" + getInternalJavaName() + ";";
  }
}

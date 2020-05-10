package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.s2typed.TypedModule;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.FunctionAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypeScopeImpl;
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

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedModule toTypedModuleAst(CompileContext compileContext,
      TypedModuleScope typedModuleScope) {

    final TypeScope initialModuleScope = TypeScope.EMPTY;

    List<TypedFunction> typedFunctions = functionAsts
        .stream()
        .map(fn -> fn.toTypedFunctionAst(initialModuleScope, typedModuleScope, compileContext))
        .collect(Collectors.toList());

    return TypedModule.builder()
        .moduleAst(this)
        .typedModuleScope(typedModuleScope)
        .functionAsts(typedFunctions)
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

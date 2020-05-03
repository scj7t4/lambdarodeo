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

  public TypedModule toTypedModuleAst(CompileContext compileContext) {
    final TypeScope initialModuleScope = TypeScope.EMPTY;

    List<TypedFunction> typedFunctions = functionAsts
        .stream()
        .map(fn -> fn.toTypedFunctionAst(initialModuleScope, compileContext))
        .collect(Collectors.toList());

    ModuleScope moduleScope = ModuleScope.builder()
        .functions(typedFunctions)
        .thisModule(this)
        .build();

    return TypedModule.builder()
        .moduleAst(this)
        .moduleScope(moduleScope)
        .functionAsts(typedFunctions)
        .build();
  }

  public String getInternalJavaName() {
    return getName().replace(".", "/");
  }

  public String getModuleJVMDescriptor() {
    return "L" + getInternalJavaName() + ";";
  }
}

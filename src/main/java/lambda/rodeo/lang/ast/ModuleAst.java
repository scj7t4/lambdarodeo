package lambda.rodeo.lang.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.typed.TypedModule;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.functions.FunctionAst;
import lambda.rodeo.lang.typed.functions.TypedFunction;
import lambda.rodeo.lang.types.FunctionType;
import lambda.rodeo.lang.types.ModuleType;
import lambda.rodeo.lang.types.TypeScope;
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
    final TypeScope initialModuleScope = TypeScope.EMPTY.declare(
        THIS_MODULE, ModuleType.builder().moduleAst(this).build());

    List<TypedFunction> typedFunctions = functionAsts
        .stream()
        .map(fn -> fn.toTypedFunctionAst(initialModuleScope, compileContext))
        .collect(Collectors.toList());

    TypeScope moduleScope = initialModuleScope;
    for(TypedFunction functionAst : typedFunctions) {
      moduleScope = moduleScope.declare(
          functionAst.getName(),
          FunctionType.builder().functionAst(functionAst).build());
    }

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

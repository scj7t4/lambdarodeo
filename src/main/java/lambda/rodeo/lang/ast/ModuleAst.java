package lambda.rodeo.lang.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.typed.TypedModuleAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.functions.FunctionAst;
import lambda.rodeo.lang.types.FunctionType;
import lambda.rodeo.lang.types.ModuleType;
import lambda.rodeo.lang.types.TypeScope;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ModuleAst implements AstNode {

  @NonNull
  private final String name;

  @Builder.Default
  private final List<FunctionAst> functionAsts = new ArrayList<>();

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedModuleAst toTypedModuleAst(CompileContext compileContext) {
    TypeScope moduleScope = TypeScope.EMPTY.declare(
        "$this", ModuleType.builder().moduleAst(this).build());
    for(FunctionAst functionAst : functionAsts) {
      moduleScope = moduleScope.declare(
          functionAst.getName(),
          FunctionType.builder().functionAst(functionAst).build());
    }

    return TypedModuleAst.builder()
        .moduleAst(this)
        .moduleScope(moduleScope)
        .functionAsts(functionAsts
            .stream()
            .map(fn -> fn.toTypedFunctionAst(compileContext))
            .collect(Collectors.toList()))
        .build();
  }

  public String getInternalJavaName() {
    return getName().replace(".", "/");
  }

  public String getModuleJVMDescriptor() {
    return "L" + getInternalJavaName() + ";";
  }
}

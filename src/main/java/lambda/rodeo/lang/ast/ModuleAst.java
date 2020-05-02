package lambda.rodeo.lang.ast;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.typed.TypedModuleAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.functions.FunctionAst;
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
    return TypedModuleAst.builder()
        .moduleAst(this)
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

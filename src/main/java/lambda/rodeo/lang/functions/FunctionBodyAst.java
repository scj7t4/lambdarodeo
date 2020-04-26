package lambda.rodeo.lang.functions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
public class FunctionBodyAst {

  private final List<StatementAst> statements;
  private final TypeScope initialTypeScope;

  public Type getReturnType() {
    return statements.get(statements.size() - 1).getType();
  }

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    for (StatementAst statement : statements) {
      statement.compile(methodVisitor, compileContext);
    }
  }

  public TypeScope getFinalTypeScope() {
    return statements.get(statements.size() - 1).getScopeAfter();
  }

  public static FunctionBodyAst of(List<StatementAst> statements, TypeScope initialTypeScope) {
    return builder()
        .initialTypeScope(initialTypeScope)
        .statements(statements)
        .build();
  }
}

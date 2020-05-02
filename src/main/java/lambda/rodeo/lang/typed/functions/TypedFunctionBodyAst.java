package lambda.rodeo.lang.typed.functions;

import java.util.List;
import lambda.rodeo.lang.ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.TypeScope;
import lambda.rodeo.lang.ast.statements.TypedStatementAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class TypedFunctionBodyAst {

  private final TypeScope initialTypeScope;
  private final List<TypedStatementAst> statements;
  private final FunctionBodyAst functionBodyAst;


  public Type getReturnType() {
    return statements.get(statements.size() - 1).getTypedExpressionAst().getType();
  }

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    for (TypedStatementAst statement : statements) {
      statement.compile(methodVisitor, compileContext);
    }
  }

  public TypeScope getFinalTypeScope() {
    return statements.get(statements.size() - 1).getAfterTypeScope();
  }

}

package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class StatementAst {

  private final TypeScope scopeBefore;
  private final TypeScope scopeAfter;
  private final ExpressionAst expression;
  private final AssigmentAst assignment;

  public Type getType() {
    return getExpression().getType();
  }

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    expression.compile(methodVisitor,
        compileContext); // So this should hopefully mean that the result of the
    // computation is on the top of the stack...
    if (assignment != null) {
      assignment.compile(methodVisitor);
    }
  }
}

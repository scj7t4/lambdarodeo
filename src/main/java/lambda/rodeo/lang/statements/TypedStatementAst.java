package lambda.rodeo.lang.statements;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.expressions.TypedExpressionAst;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class TypedStatementAst {

  private final StatementAst statementAst;
  private final TypeScope beforeTypeScope;
  private final TypeScope afterTypeScope;
  private final TypedExpressionAst typedExpressionAst;
  private final TypedAssignmentAst typedAssignmentAst;

  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    typedExpressionAst.compile(methodVisitor,
        compileContext); // So this should hopefully mean that the result of the
    // computation is on the top of the stack...
    if (typedAssignmentAst != null) {
      typedAssignmentAst.compile(methodVisitor, compileContext);
    }
  }
}

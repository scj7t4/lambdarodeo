package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
@Getter
@Builder
public class AddAst implements BiNumericExpressionAst {

  private final ExpressionAst lhs;
  private final ExpressionAst rhs;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public String operationName() {
    return "addition (+)";
  }

  public void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      CompileContext compileContext) {
    lhs.compile(methodVisitor, compileContext);
    rhs.compile(methodVisitor, compileContext);
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/math/BigInteger",
        "add",
        "(Ljava/math/BigInteger;)Ljava/math/BigInteger;",
        false);
  }
}

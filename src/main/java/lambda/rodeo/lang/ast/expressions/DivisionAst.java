package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
@Builder
@Getter
public class DivisionAst implements BiNumericExpressionAst {

  private final ExpressionAst lhs;
  private final ExpressionAst rhs;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      CompileContext compileContext) {
    lhs.compile(methodVisitor, compileContext);
    rhs.compile(methodVisitor, compileContext);
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/math/BigInteger",
        "divide",
        "(Ljava/math/BigInteger;)Ljava/math/BigInteger;",
        false);
  }
}

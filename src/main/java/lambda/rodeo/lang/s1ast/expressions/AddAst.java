package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
@Getter
@Builder
@EqualsAndHashCode
public class AddAst implements BiNumericExpressionAst {

  @NonNull
  private final ExpressionAst lhs;

  @NonNull
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

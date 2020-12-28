package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.fn.IntegerFunctions;
import lambda.rodeo.runtime.lambda.Lambda0;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@ToString
@Builder
@Getter
@EqualsAndHashCode
public class DivisionAst implements BiNumericExpressionAst {

  private final ExpressionAst lhs;
  private final ExpressionAst rhs;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public String operationName() {
    return "division (/)";
  }

  @Override
  public void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
    lhs.compile(methodVisitor, compileContext);
    rhs.compile(methodVisitor, compileContext);
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(IntegerFunctions.class), "makeDivide",
        FunctionDescriptorBuilder.args(Lambda0.class, Lambda0.class)
            .returns(Lambda0.class), false);
  }
}

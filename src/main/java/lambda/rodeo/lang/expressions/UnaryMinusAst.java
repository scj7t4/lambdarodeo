package lambda.rodeo.lang.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
public class UnaryMinusAst implements ExpressionAst {

  private final Type type;
  private final ExpressionAst operand;

  public UnaryMinusAst(ExpressionAst operand,
      CompileContext compileContext) {
    this.operand = operand;
    if (AstUtils.isAnyUndefined(operand)) {
      type = Atom.UNDEFINED_VAR;
    } else if (AstUtils.isIntType(operand)) {
      type = operand.getType();
    } else {
      throw new TypeException("Cannot negate type " + operand.getType());
    }
  }

  @Override
  public Type getType() {
    return type;
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    operand.compile(methodVisitor, compileContext);
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/math/BigInteger",
        "negate",
        "()Ljava/math/BigInteger;",
        false);
  }

}

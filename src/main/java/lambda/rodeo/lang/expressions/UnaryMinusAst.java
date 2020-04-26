package lambda.rodeo.lang.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.math.BigInteger;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
public class UnaryMinusAst implements ExpressionAst {

  private final Type type;
  private final ExpressionAst operand;

  public UnaryMinusAst(ExpressionAst operand, TypeScope typeScope,
      CompileContext compileContext) {
    this.operand = operand;
    if (AstUtils.isAnyUndefined(typeScope, operand)) {
      type = Atom.UNDEFINED_VAR;
    } else if (AstUtils.isIntType(operand, typeScope)) {
      type = operand.getType(typeScope);
    } else {
      throw new TypeException("Cannot negate type " + operand.getType(typeScope));
    }
  }

  @Override
  public Type getType(TypeScope typeScope) {
    return type;
  }

  @Override
  public void compile(MethodVisitor methodVisitor) {
    operand.compile(methodVisitor);
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/math/BigInteger",
        "negate",
        "()Ljava/math/BigInteger;",
        false);
  }

}

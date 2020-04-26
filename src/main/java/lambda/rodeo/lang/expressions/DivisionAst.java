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
public class DivisionAst implements ExpressionAst {

  private final Type type;
  private final ExpressionAst lhs;
  private final ExpressionAst rhs;

  public DivisionAst(ExpressionAst lhs, ExpressionAst rhs, TypeScope typeScope,
      CompileContext compileContext) {
    this.lhs = lhs;
    this.rhs = rhs;
    if (AstUtils.isAnyUndefined(typeScope, lhs, rhs)) {
      type = Atom.UNDEFINED_VAR;
    } else if (AstUtils.bothIntType(lhs, rhs, typeScope)) {
      type = lhs.getType(typeScope);
    } else {
      throw new TypeException(
          "Cannot divide types " + lhs.getType(typeScope) + " and " + rhs.getType(typeScope));
    }
  }

  @Override
  public Type getType(TypeScope typeScope) {
    return type;
  }

  @Override
  public void compile(MethodVisitor methodVisitor) {
    lhs.compile(methodVisitor);
    rhs.compile(methodVisitor);
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/math/BigInteger",
        "divide",
        "(Ljava/math/BigInteger;)Ljava/math/BigInteger;",
        false);
  }

}

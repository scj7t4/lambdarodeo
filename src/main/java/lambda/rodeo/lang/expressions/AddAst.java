package lambda.rodeo.lang.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
public class AddAst implements ExpressionAst {

  private final Type type;
  private final ExpressionAst lhs;
  private final ExpressionAst rhs;

  public AddAst(ExpressionAst lhs, ExpressionAst rhs,
      TypeScope typeScope, CompileContext compileContext) {
    this.lhs = lhs;
    this.rhs = rhs;
    if (AstUtils.isAnyUndefined(lhs, rhs)) {
      type = Atom.UNDEFINED_VAR;
    } else if (AstUtils.bothIntType(lhs, rhs, typeScope)) {
      type = lhs.getType();
    } else {
      throw new TypeException("Cannot add types " + lhs.getType()
          + " and " + rhs.getType());
    }
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
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

  @Override
  public Type getType() {
    return type;
  }
}

package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
@Builder
@Getter
@EqualsAndHashCode
public class UnaryMinusAst implements ExpressionAst {

  private final ExpressionAst operand;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpression toTypedExpression(TypeScope typeScope,
      CompileContext compileContext) {
    TypedExpression typedOperand = operand
        .toTypedExpression(typeScope, compileContext);
    Type type = typedOperand.getType();

    if (AstUtils.isAnyUndefined(type)) {
      return SimpleTypedExpression.builder()
          .type(Atom.UNDEFINED)
          .expr(this)
          .compileable((mv, cc) -> compile(typedOperand.toCompileableExpr(), mv, cc))
          .build();
    } else if (AstUtils.isIntType(type)) {
      return SimpleTypedExpression.builder()
          .compileable((mv, cc) -> this.compile(typedOperand.toCompileableExpr(), mv, cc))
          .type(type)
          .build();
    } else {
      //TODO Compile error instead.
      throw new TypeException("Cannot negate type " + type);
    }
  }

  public void compile(
      Compileable operand,
      MethodVisitor methodVisitor,
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

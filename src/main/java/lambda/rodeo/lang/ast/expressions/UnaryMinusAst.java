package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.ast.statements.TypeScope;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpressionAst;
import lambda.rodeo.lang.typed.expressions.TypedExpressionAst;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@ToString
@Builder
@Getter
public class UnaryMinusAst implements ExpressionAst {

  private final ExpressionAst operand;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpressionAst toTypedExpressionAst(TypeScope typeScope,
      CompileContext compileContext) {
    TypedExpressionAst typedOperand = operand
        .toTypedExpressionAst(typeScope, compileContext);
    Type type = typedOperand.getType();

    if (AstUtils.isAnyUndefined(type)) {
      return SimpleTypedExpressionAst.builder()
          .type(Atom.UNDEFINED_VAR)
          .expr(this)
          .compileableExpr((mv, cc) -> compile(typedOperand, mv, cc))
          .build();
    } else if (AstUtils.isIntType(type)) {
      return SimpleTypedExpressionAst.builder()
          .compileableExpr((mv, cc) -> this.compile(typedOperand, mv, cc))
          .type(type)
          .build();
    } else {
      //TODO Compile error instead.
      throw new TypeException("Cannot negate type " + type);
    }
  }

  public void compile(
      TypedExpressionAst operand,
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

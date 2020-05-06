package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.Type;
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
      TypedModuleScope typedModuleScope, CompileContext compileContext) {
    TypedExpression typedOperand = operand
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    Type type = typedOperand.getType();

    if (AstUtils.isAnyUndefined(type)) {
      return SimpleTypedExpression.builder()
          .type(Atom.UNDEFINED)
          .expr(this)
          .toCompileable(() -> (mv, cc) -> compile(typedOperand.toCompileableExpr(), mv, cc))
          .build();
    } else if (AstUtils.isIntType(type)) {
      return SimpleTypedExpression.builder()
          .toCompileable(() -> (mv, cc) -> this.compile(typedOperand.toCompileableExpr(), mv, cc))
          .type(type)
          .expr(this)
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
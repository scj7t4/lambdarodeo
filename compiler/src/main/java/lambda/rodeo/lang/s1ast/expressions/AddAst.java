package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.util.Objects;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedStringConcat;
import lambda.rodeo.lang.s2typed.expressions.TypedStringConcat.TypedStringConcatBuilder;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lambda.rodeo.runtime.types.StringType;
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

  @Override
  public TypedExpression toTypedExpression(TypeScope typeScope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {
    TypedExpression typedLhs = getLhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    CompileableType left = typedLhs.getType();
    TypedExpression typedRhs = getRhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    CompileableType right = typedRhs.getType();
    if (Objects.equals(StringType.INSTANCE, left) || Objects.equals(StringType.INSTANCE, right)) {
      TypedStringConcatBuilder builder = TypedStringConcat.builder()
          .expr(this)
          .lhs(typedLhs)
          .rhs(typedRhs);
      return builder.build();
    } else {
      return BiNumericExpressionAst.super
          .getTypedExpression(compileContext, typedLhs, left, typedRhs, right);
    }
  }

  public void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
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

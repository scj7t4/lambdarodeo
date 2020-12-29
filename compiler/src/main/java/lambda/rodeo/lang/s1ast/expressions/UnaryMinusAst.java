package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;

import java.math.BigInteger;
import java.util.Set;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
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
public class UnaryMinusAst implements ExpressionAst {

  private final ExpressionAst operand;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpression toTypedExpression(TypeScope typeScope,
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext) {
    TypedExpression typedOperand = operand
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    CompileableType type = typedOperand.getType();

    if (AstUtils.isAnyUndefined(type)) {
      return AtomAst.undefinedAtomExpression();
    } else if (AstUtils.isIntType(type)) {
      return SimpleTypedExpression.builder()
          .toCompileable(() -> (mv, cc) -> this.compile(
              typedOperand.toCompileableExpr(compileContext.getCompileContext()), mv, cc))
          .type(type)
          .expr(this)
          .build();
    } else {
      compileContext.getCompileErrorCollector().collect(
          CompileError.mathOperationWithNonNumeric(this, "unary minus (-)", type)
      );
      return AtomAst.undefinedAtomExpression();
    }
  }

  @Override
  public Set<String> getReferencedVariables() {
    return operand.getReferencedVariables();
  }

  public void compile(
      CompileableExpression operand,
      MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
    operand.compile(methodVisitor, compileContext);
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(IntegerFunctions.class), "makeNegate",
        FunctionDescriptorBuilder.args(Lambda0.class)
            .returns(Lambda0.class), false);
  }

}

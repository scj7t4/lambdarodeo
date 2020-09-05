package lambda.rodeo.lang.s1ast.expressions;

import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import org.objectweb.asm.MethodVisitor;

/**
 * Represents a mathematical operation that has two operands.
 */
public interface BiNumericExpressionAst extends ExpressionAst {

  ExpressionAst getLhs();

  ExpressionAst getRhs();

  String operationName();

  /**
   * Compile this expression to Java Byte Code.
   *
   * @param lhs The left hand side of the operation.
   * @param rhs The right hand side of the operation.
   * @param methodVisitor The ASM code writer.
   * @param compileContext The compile context.
   */
  void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      S1CompileContext compileContext);

  static Supplier<Compileable> toCompilable(
      TypedExpression lhs,
      TypedExpression rhs,
      BiNumericExpressionAst expr) {

    return () ->
        (methodVisitor, compileContext) -> expr.compile(
            lhs.toCompileableExpr(),
            rhs.toCompileableExpr(),
            methodVisitor, compileContext);
  }

  /**
   * Converts this to a TypedExpression
   *
   * @param typeScope The type scope.
   * @param typedModuleScope
   * @param compileContext The compile context.
   * @return A typed version of this expression.
   */
  @Override
  default TypedExpression toTypedExpression(TypeScope typeScope,
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext) {
    TypedExpression typedLhs = getLhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    CompileableType left = typedLhs.getType();
    TypedExpression typedRhs = getRhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    CompileableType right = typedRhs.getType();

    return getTypedExpression(compileContext, typedLhs, left, typedRhs, right);
  }

  default TypedExpression getTypedExpression(ToTypedFunctionContext compileContext,
      TypedExpression typedLhs, CompileableType left, TypedExpression typedRhs,
      CompileableType right) {
    if (AstUtils.isAnyUndefined(left, right)) {
      return AtomAst.undefinedAtomExpression();
    } else if (AstUtils.bothIntType(left, right)) {
      return SimpleTypedExpression.builder()
          .expr(this)
          .type(left)
          .toCompileable(toCompilable(typedLhs, typedRhs, this))
          .build();
    } else {
      compileContext.getCompileErrorCollector()
          .collect(CompileError.mathOperationWithNonNumeric(
              this,
              this.operationName(),
              left, right));
      return AtomAst.undefinedAtomExpression();
    }
  }

  @Override
  default Set<String> getReferencedVariables() {
    return Stream.concat(
        getLhs().getReferencedVariables().stream(),
        getRhs().getReferencedVariables().stream())
        .collect(Collectors.toSet());
  }
}

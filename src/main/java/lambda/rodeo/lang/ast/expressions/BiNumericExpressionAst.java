package lambda.rodeo.lang.ast.expressions;

import java.util.function.Function;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
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
      CompileContext compileContext);

  static Function<CompileableModuleScope, Compileable> toCompilable(
      TypedExpression lhs,
      TypedExpression rhs,
      BiNumericExpressionAst expr) {

    return (CompileableModuleScope compileableModuleScope) ->
        (methodVisitor, compileContext) -> expr.compile(
            lhs.toCompileableExpr(compileableModuleScope),
            rhs.toCompileableExpr(compileableModuleScope),
            methodVisitor, compileContext);
  }

  /**
   * Converts this to a TypedExpression
   *
   * @param typeScope The type scope.
   * @param compileContext The compile context.
   * @return A typed version of this expression.
   */
  @Override
  default TypedExpression toTypedExpression(TypeScope typeScope, CompileContext compileContext) {
    TypedExpression typedLhs = getLhs()
        .toTypedExpression(typeScope, compileContext);
    Type left = typedLhs.getType();
    TypedExpression typedRhs = getRhs()
        .toTypedExpression(typeScope, compileContext);
    Type right = typedRhs.getType();

    if (AstUtils.isAnyUndefined(left, right)) {
      return Atom.UNDEFINED.toTypedExpressionAst();
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
      return Atom.UNDEFINED.toTypedExpressionAst();
    }
  }


}

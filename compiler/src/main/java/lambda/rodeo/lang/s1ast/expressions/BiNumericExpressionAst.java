package lambda.rodeo.lang.s1ast.expressions;

import java.util.function.Supplier;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.Type;
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
      TypedModuleScope typedModuleScope, CompileContext compileContext) {
    TypedExpression typedLhs = getLhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    Type left = typedLhs.getType();
    TypedExpression typedRhs = getRhs()
        .toTypedExpression(typeScope, typedModuleScope, compileContext);
    Type right = typedRhs.getType();

    if (AstUtils.isAnyUndefined(left, right)) {
      return AtomAst
          .builder()
          .atom(Atom.UNDEFINED)
          .build()
          .toTypedExpression();
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
      return AtomAst
          .builder()
          .atom(Atom.UNDEFINED)
          .build()
          .toTypedExpression();
    }
  }


}

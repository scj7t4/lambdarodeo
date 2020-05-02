package lambda.rodeo.lang.ast.expressions;

import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import org.objectweb.asm.MethodVisitor;

public interface BiNumericExpressionAst extends ExpressionAst {

  ExpressionAst getLhs();
  ExpressionAst getRhs();

  void compile(CompileableExpr lhs, CompileableExpr rhs, MethodVisitor methodVisitor,
      CompileContext compileContext);

  static Compileable toCompilable(TypedExpression lhs, TypedExpression rhs,
      BiNumericExpressionAst expr) {
    return (methodVisitor, compileContext) -> expr.compile(
        lhs.toCompileableExpr(),
        rhs.toCompileableExpr(),
        methodVisitor, compileContext);
  }

  @Override
  default TypedExpression toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    TypedExpression typedLhs = getLhs()
        .toTypedExpressionAst(typeScope, compileContext);
    Type left = typedLhs.getType();
    TypedExpression typedRhs = getRhs()
        .toTypedExpressionAst(typeScope, compileContext);
    Type right = typedRhs.getType();

    if (AstUtils.isAnyUndefined(left, right)) {
      return Atom.UNDEFINED_VAR.toTypedExpressionAst();
    } else if (AstUtils.bothIntType(left, right)) {
      return SimpleTypedExpression.builder()
          .expr(this)
          .type(left)
          .compileable(toCompilable(typedLhs, typedRhs, this))
          .build();
    } else {
      //TODO: Compile error instead
      throw new TypeException("Cannot add types " + left + " and " + right);
    }
  }


}

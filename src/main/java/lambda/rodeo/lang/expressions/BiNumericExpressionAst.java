package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.TypeException;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import org.objectweb.asm.MethodVisitor;

public interface BiNumericExpressionAst extends ExpressionAst {

  ExpressionAst getLhs();
  ExpressionAst getRhs();

  void compile(TypedExpressionAst lhs, TypedExpressionAst rhs, MethodVisitor methodVisitor,
      CompileContext compileContext);

  static CompileableExpr toCompilable(TypedExpressionAst lhs, TypedExpressionAst rhs,
      BiNumericExpressionAst expr) {
    return (methodVisitor, compileContext) -> expr.compile(lhs, rhs, methodVisitor, compileContext);
  }

  @Override
  default TypedExpressionAst toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    TypedExpressionAst typedLhs = getLhs()
        .toTypedExpressionAst(typeScope, compileContext);
    Type left = typedLhs.getType();
    TypedExpressionAst typedRhs = getRhs()
        .toTypedExpressionAst(typeScope, compileContext);
    Type right = typedRhs.getType();

    if (AstUtils.isAnyUndefined(left, right)) {
      return Atom.UNDEFINED_VAR.toTypedExpressionAst();
    } else if (AstUtils.bothIntType(left, right)) {
      return SimpleTypedExpressionAst.builder()
          .expr(this)
          .type(left)
          .compileableExpr(toCompilable(typedLhs, typedRhs, this))
          .build();
    } else {
      //TODO: Compile error instead
      throw new TypeException("Cannot add types " + left + " and " + right);
    }
  }


}

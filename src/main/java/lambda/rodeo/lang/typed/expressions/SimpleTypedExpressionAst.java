package lambda.rodeo.lang.typed.expressions;

import lambda.rodeo.lang.ast.expressions.CompileableExpr;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.TypedExpressionAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@ToString
public class SimpleTypedExpressionAst implements TypedExpressionAst {
  private final ExpressionAst expr;
  private final Type type;
  private final CompileableExpr compileableExpr;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    compileableExpr.compile(methodVisitor, compileContext);
  }
}

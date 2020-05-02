package lambda.rodeo.lang.typed.expressions;

import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@ToString
public class SimpleTypedExpression implements TypedExpression, Compileable {
  private final ExpressionAst expr;
  private final Type type;
  private final Compileable compileable;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    compileable.compile(methodVisitor, compileContext);
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .build();
  }
}

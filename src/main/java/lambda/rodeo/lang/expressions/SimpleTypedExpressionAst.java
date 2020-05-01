package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.compilation.CompileContext;
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

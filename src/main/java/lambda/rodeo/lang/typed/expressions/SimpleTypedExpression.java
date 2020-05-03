package lambda.rodeo.lang.typed.expressions;

import java.util.function.Function;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@ToString
@EqualsAndHashCode
public class SimpleTypedExpression implements TypedExpression {
  private final ExpressionAst expr;
  private final Type type;
  private final Function<CompileableModuleScope, Compileable> toCompileable;


  @Override
  public CompileableExpr toCompileableExpr(
      CompileableModuleScope compileableModuleScope) {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(toCompileable.apply(compileableModuleScope))
        .build();
  }
}
